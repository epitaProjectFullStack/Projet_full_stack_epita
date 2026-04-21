# Documentation développeur

## Structure du projet

### Racine

- `backend/`: application Spring Boot
- `frontend/`: application Angular
- `docker-compose.yaml`: stack locale
- `.github/workflows/`: CI/CD

### Backend

Organisation principale:
- `config/`: configuration Spring, sécurité, OpenAPI, Kafka, WebSocket
- `controller/`: endpoints REST et DTOs request/response
- `converter/`: mapping controller/domain/data
- `data/`: modèles JPA et repositories
- `domain/`: entités, services, événements
- `utils/`: rôles, codes d'erreur, constantes

### Frontend

Organisation principale:
- `page/`: pages Angular
- `components/`: composants réutilisables
- `services/`: accès backend, cache, websocket, erreurs
- `interceptor/`: intercepteurs HTTP
- `interface/` et `enum/`: modèles TypeScript

## Architecture actuelle

Le backend suit aujourd'hui une architecture en couches:
- controller
- service
- repository

Ce n'est pas une architecture hexagonale stricte:
- les services métier dépendent encore des repositories Spring Data
- les modèles JPA sont encore proches de la logique applicative

Patterns réellement utilisés:
- architecture en couches Spring Boot classique
- DTO `request/response` côté contrôleurs REST
- converters dédiés entre controller, domain et data
- services applicatifs qui centralisent les règles métier
- repositories Spring Data JPA pour l'accès persistant
- configuration par composants Spring (`@Configuration`, `@Bean`, `CommandLineRunner`)
- sécurité Spring Security avec JWT Resource Server plus filtre hybride métier

## Sécurité

### Authentification

Le système utilise:
- un `accessToken` JWT court
- un `refreshToken` opaque, hashé en base, avec rotation

Principaux fichiers:
- [`SecurityConfig.java`](../backend/src/main/java/fr/epita/backend/config/SecurityConfig.java)
- [`TokenService.java`](../backend/src/main/java/fr/epita/backend/domain/service/TokenService.java)
- [`RefreshTokenService.java`](../backend/src/main/java/fr/epita/backend/domain/service/RefreshTokenService.java)
- [`AuthService.java`](../backend/src/main/java/fr/epita/backend/domain/service/AuthService.java)
- [`UserStatusAuthenticationFilter.java`](../backend/src/main/java/fr/epita/backend/config/UserStatusAuthenticationFilter.java)

### Choix d'implémentation

- validation JWT portée par Spring Security Resource Server
- rechargement du user en base après authentification JWT
- effet immédiat du bannissement et du changement de rôle

### Séquence JWT / refresh token

1. `POST /api/auth`
2. `AuthService` valide le login et le mot de passe
3. `TokenService` génère un `accessToken` JWT
4. `RefreshTokenService` génère un `refreshToken`, stocke son hash en base et renvoie le token brut
5. le client appelle les routes protégées avec `Authorization: Bearer <accessToken>`
6. Spring Security valide le JWT, puis `UserStatusAuthenticationFilter` recharge l'utilisateur courant
7. si l'`accessToken` expire, le client appelle `POST /api/auth/refresh` avec le `refreshToken`
8. `RefreshTokenService` valide puis révoque l'ancien refresh token et en émet un nouveau
9. `POST /api/auth/logout` révoque le refresh token courant

### Bootstrap admin

Un administrateur est créé ou mis à jour au démarrage via:
- [`AdminBootstrapConfig.java`](../backend/src/main/java/fr/epita/backend/config/AdminBootstrapConfig.java)

Variables utilisées:
- `ADMIN_LOGIN`
- `ADMIN_PASSWORD`
- `ADMIN_MAIL`

## Gestion d'erreur

### Couches impliquées

- [`ErrorCode.java`](../backend/src/main/java/fr/epita/backend/utils/ErrorCode.java)
  - catalogue des erreurs métier
- [`ApiErrorResponse.java`](../backend/src/main/java/fr/epita/backend/controller/api/response/ApiErrorResponse.java)
  - format JSON de sortie
- [`GlobalExceptionHandler.java`](../backend/src/main/java/fr/epita/backend/config/GlobalExceptionHandler.java)
  - erreurs MVC / contrôleurs / services
- [`ApiAuthenticationEntryPoint.java`](../backend/src/main/java/fr/epita/backend/config/ApiAuthenticationEntryPoint.java)
  - erreurs `401`
- [`ApiAccessDeniedHandler.java`](../backend/src/main/java/fr/epita/backend/config/ApiAccessDeniedHandler.java)
  - erreurs `403`

## Kafka et WebSocket

### Kafka

Rôle dans le produit:
- diffuser des événements backend de manière asynchrone
- découpler la production d'événements métier de leur consommation
- préparer l'intégration avec d'autres composants ou traitements différés

Fichiers principaux:
- [`KafkaConfig.java`](../backend/src/main/java/fr/epita/backend/config/KafkaConfig.java)
- [`KafkaProducerService.java`](../backend/src/main/java/fr/epita/backend/domain/service/KafkaProducerService.java)
- [`KafkaConsumerService.java`](../backend/src/main/java/fr/epita/backend/domain/service/KafkaConsumerService.java)

### WebSocket

Rôle dans le produit:
- pousser des mises à jour temps réel vers le frontend
- éviter un polling HTTP permanent pour certains écrans réactifs

Fichiers principaux:
- [`WebSocketConfig.java`](../backend/src/main/java/fr/epita/backend/config/WebSocketConfig.java)
- [`websocket.service.ts`](../frontend/src/app/services/websocket.service.ts)

## API et documentation

Swagger/OpenAPI est disponible via:
- `http://localhost/swagger-ui/index.html`
- `http://localhost/v3/api-docs`

Config OpenAPI:
- [`OpenApiConfig.java`](../backend/src/main/java/fr/epita/backend/config/OpenApiConfig.java)

## Tests

### Backend

Types de tests actuellement présents:
- test de contexte Spring
- tests de converters
- tests de services
- tests de controllers slice MVC

Commandes:

```bash
cd backend
./mvnw test
./mvnw clean verify
```

Couverture:
- Jacoco disponible dans `backend/target/site/jacoco/`

### Frontend

Tests Angular/Vitest sur:
- pages
- composants
- services
- intercepteurs

Commandes:

```bash
cd frontend
npm ci
npm run coverage
```

## CI/CD

### CI backend

Workflow:
- [`maven.yml`](../.github/workflows/maven.yml)

Fait actuellement:
- build Maven
- tests backend
- publication du rapport Jacoco

### CI frontend

Workflow:
- [`angular.yml`](../.github/workflows/angular.yml)

Fait actuellement:
- installation npm
- build Angular production
- tests frontend avec couverture

### Release

Workflow:
- [`release.yml`](../.github/workflows/release.yml)

Fait actuellement:
- incrément de version
- tag Git
- build et push image Docker

## Limites actuelles

- architecture hexagonale non stricte
- pas de tests e2e backend+frontend
- pas d'analyse statique industrialisée dans la CI
- externalisation des secrets encore incomplète selon les environnements

## Ajouter une feature

Flux conseillé pour le backend:
1. créer ou adapter le DTO request/response
2. ajouter le controller REST
3. implémenter la logique dans le service
4. adapter repository et modèle si nécessaire
5. ajouter tests service + controller
6. documenter l'endpoint dans Swagger si besoin
