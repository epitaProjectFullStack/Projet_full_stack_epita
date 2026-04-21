# Projet Full Stack EPITA

Application full stack composée de:
- un backend Spring Boot 3 / Java 21
- un frontend Angular
- PostgreSQL pour la persistance
- Kafka pour la diffusion d'événements
- WebSocket pour certaines mises à jour temps réel

## Vue d'ensemble

Le projet expose une API REST documentée via Swagger/OpenAPI et une interface web Angular.
La sécurité repose sur:
- un `accessToken` JWT pour les routes protégées
- un `refreshToken` stocké côté backend, hashé en base et rotatif

Les rôles métier actuellement gérés sont:
- `USER`
- `MODERATOR`
- `ADMINISTRATOR`

Les statuts métier des jeux/articles sont:
- `OK`
- `TO_REVIEW`
- `DISCARD`

## Documentation

- [Documentation d'exploitation](./docs/exploitation.md)
- [Documentation utilisateur](./docs/utilisateur.md)
- [Documentation développeur](./docs/developpeur.md)

## Accès utiles

- Swagger UI: [http://localhost/swagger-ui/index.html](http://localhost/swagger-ui/index.html)
- OpenAPI JSON: [http://localhost/v3/api-docs](http://localhost/v3/api-docs)
- Frontend en Docker: [http://localhost](http://localhost)
- Frontend en local Angular: [http://localhost:4200](http://localhost:4200)
- Backend local hors Docker: [http://localhost:8080](http://localhost:8080)
- Kafka UI: [http://localhost:8080/ui/clusters/local](http://localhost:8080/ui/clusters/local)

## Commandes rapides

### Backend

```bash
cd backend
./mvnw clean verify
./mvnw test
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm ci
npm run build
npm run build:prod
npm run coverage
npm start
```

### Docker Compose

```bash
docker compose down -v
docker compose up --build
```

## CI/CD

Le projet contient actuellement trois workflows GitHub Actions:
- [Java CI avec Maven](./.github/workflows/maven.yml)
- [Angular CI](./.github/workflows/angular.yml)
- [Release CI](./.github/workflows/release.yml)

Le détail du comportement attendu et des limites actuelles est décrit dans la [documentation développeur](./docs/developpeur.md).

## POURCENTAGE DE TESTS BACKEND REALISER
- cd backend
- xdg-open target/site/jacoco/index.html
(chemin d'acces = backend/target/site/jacoco/index.html)
