# Documentation utilisateur

Cette documentation décrit les usages principaux par profil fonctionnel.

## Profils

Le système distingue actuellement trois rôles applicatifs:
- `USER`
- `MODERATOR`
- `ADMINISTRATOR`

## Utilisateur non authentifié

Fonctionnalités principales:
- consulter l'interface publique
- consulter les jeux/articles validés (`OK`)
- créer un compte
- se connecter

Pages frontend utiles:
- accueil: `/`
- connexion: `/signin`
- inscription: `/register`

Routes backend utiles:
- `POST /api/auth/register`
- `POST /api/auth`
- `GET /api/games`

## Utilisateur authentifié

Fonctionnalités principales:
- consulter les ressources accessibles au rôle `USER`
- créer un jeu/article
- modifier ses contenus selon les règles métier exposées par l'application
- rafraîchir sa session avec le refresh token

Pages frontend utiles:
- création d'article: `/new`
- consultation d'article: `/article/:id`

Routes backend utiles:
- `GET /api/user`
- `GET /api/user/{id}`
- `PUT /api/user/{id}`
- `DELETE /api/user/{id}`
- `POST /api/games`
- `PUT /api/games/{id}`
- `POST /api/auth/refresh`
- `POST /api/auth/logout`

## Modérateur

Fonctionnalités principales:
- consulter les jeux/articles en attente de revue
- valider ou rejeter des contenus

Pages frontend utiles:
- tableau reviewer: `/reviewer`
- revue d'article: `/article/review/:id`

Routes backend utiles:
- `GET /api/games/review`
- `PATCH /api/games/{id}/status`

Statuts manipulés:
- `TO_REVIEW`
- `OK`
- `DISCARD`

## Administrateur

Fonctionnalités principales:
- gérer les utilisateurs
- créer un utilisateur avec un rôle explicite
- modifier un rôle
- bannir ou débannir un utilisateur
- accéder à l'ensemble des contenus

Page frontend utile:
- administration: `/admin`

Routes backend utiles:
- `GET /api/admin/user`
- `GET /api/admin/user/{id}`
- `POST /api/admin/user`
- `PUT /api/admin/user/{id}`
- `DELETE /api/admin/user/{id}`
- `GET /api/games/all`

## Authentification

Le backend renvoie après connexion:
- `accessToken`
- `refreshToken`

Usage:
- `accessToken` pour appeler les routes protégées avec `Authorization: Bearer ...`
- `refreshToken` uniquement pour `/api/auth/refresh` et `/api/auth/logout`

## Erreurs fréquentes côté utilisateur

### `Bad credentials`

Le login ou le mot de passe est incorrect.

### `User is banned`

Le compte existe mais est bloqué côté administration.

### `Invalid refresh token`

Le refresh token a déjà été utilisé, révoqué, ou n'existe pas.
