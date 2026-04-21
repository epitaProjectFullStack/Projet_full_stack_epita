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

Fichiers principaux:
- [`KafkaConfig.java`](../backend/src/main/java/fr/epita/backend/config/KafkaConfig.java)
- [`KafkaProducerService.java`](../backend/src/main/java/fr/epita/backend/domain/service/KafkaProducerService.java)
- [`KafkaConsumerService.java`](../backend/src/main/java/fr/epita/backend/domain/service/KafkaConsumerService.java)

### WebSocket

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
- secrets encore partiellement définis dans les properties

## Ajouter une feature

Flux conseillé pour le backend:
1. créer ou adapter le DTO request/response
2. ajouter le controller REST
3. implémenter la logique dans le service
4. adapter repository et modèle si nécessaire
5. ajouter tests service + controller
6. documenter l'endpoint dans Swagger si besoin
