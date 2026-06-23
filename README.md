# Your Car Your Way — PoC

Application de location de véhicules — Preuve de concept réalisée dans le cadre du projet P13 (OpenClassrooms, formation Développeur Full-Stack Java/Angular Bac+5).

Le PoC démontre trois parcours fonctionnels connectés à une vraie base de données PostgreSQL, sécurisés par authentification JWT :

- **Recherche d'offres** — recherche de véhicules disponibles par ville et dates (accessible sans connexion)
- **Réservation** — création, consultation de l'historique, et annulation d'une réservation
- **Messagerie support** — chat en temps réel via WebSocket (STOMP/SockJS), avec historique persistant en base

## Stack technique

| Couche | Technologie |
|---|---|
| Frontend | Angular 19 (standalone components), Angular Material, RxJS |
| Backend | Java 21, Spring Boot 3.5.x, Spring Security (JWT), Spring Data JPA, Spring WebSocket |
| Base de données | PostgreSQL 15 |
| Mapping DTO ↔ Entités | MapStruct |
| Documentation API | springdoc-openapi (Swagger UI) |
| Conteneurisation | Docker / Docker Compose |

## Architecture du dépôt

```
yourcaryourway-api-finalproject-oc/
├── yourcaryourway-api/     → Backend Spring Boot
│   ├── src/main/java/com/yourcaryourway/api/
│   │   ├── model/          → Entités JPA (User, Agency, Vehicle, Offer, Reservation, Message)
│   │   ├── repository/     → Spring Data JPA repositories
│   │   ├── dto/            → Objets de transfert (request/response)
│   │   ├── mapper/         → MapStruct (DTO ↔ Entité)
│   │   ├── service/        → Logique métier
│   │   ├── controller/     → Endpoints REST + WebSocket
│   │   ├── config/         → Configuration (WebSocket, OpenAPI)
│   │   ├── security/       → JWT, Spring Security
│   │   └── seed/           → Données de démonstration (DataSeeder)
│   └── docker-compose.yml
└── front/                  → Frontend Angular
    └── src/app/
        ├── core/            → Services, interfaces, guards, intercepteurs
        └── features/        → Composants par fonctionnalité (auth, offers, reservations, support)
```

## Prérequis

- Java 21 (JDK)
- Node.js 18+ et npm
- Docker Desktop (pour PostgreSQL)
- Angular CLI : `npm install -g @angular/cli`

## Installation et lancement

### 1. Backend

```bash
cd yourcaryourway-api

# Démarre PostgreSQL + l'API Spring Boot dans Docker
docker-compose up --build
```

L'API démarre sur **http://localhost:8080**

Documentation Swagger disponible sur : **http://localhost:8080/swagger-ui.html**

> Au premier démarrage, un `DataSeeder` insère automatiquement des données de démonstration (agences, véhicules, offres). Le seeder est idempotent — il ne duplique pas les données aux démarrages suivants.

### 2. Frontend

Dans un second terminal :

```bash
cd front
npm install
ng serve
```

L'application est disponible sur **http://localhost:4200**

## Compte de test

Pour tester directement sans passer par l'inscription :

```
Email : test@ycyw.fr
Mot de passe : Test1234!
```

Ou créer un nouveau compte via la page d'inscription (`/register`).

## Parcours de démonstration recommandé

1. Se connecter (ou s'inscrire)
2. Page d'accueil → **Rechercher un véhicule**
3. Renseigner : ville de départ `Paris`, ville de retour `Paris`, dates futures, catégorie ACRISS `ECMR` (optionnel)
4. Cliquer sur une offre → **Voir le détail** → **Réserver ce véhicule**
5. **Mes réservations** → vérifier le statut `CONFIRMED` → tester l'annulation
6. **Support client** → ouvrir la page dans deux onglets pour observer la messagerie en temps réel

## Points techniques notables

- **Database per Service (conceptuel)** : chaque domaine métier dispose de ses propres entités et repositories, préparant une future décomposition en microservices (détaillée dans l'Architecture Definition Document V4)
- **JWT stateless** : aucune session serveur, le token contient toute l'information nécessaire à l'authentification
- **WebSocket (STOMP + SockJS)** : endpoint `/ws`, messages publiés sur `/app/chat.send`, diffusés sur `/topic/conversations/{conversationId}`. Voir `WebSocketConfig.java`
- **Sécurité** : tous les endpoints sont protégés par JWT à l'exception de `/api/auth/**`, `/api/v1/offers/**` (recherche publique), `/ws/**` (simplification volontaire du PoC) et la documentation Swagger

## Endpoints principaux

| Méthode | Endpoint | Description | Auth requise |
|---|---|---|---|
| POST | `/api/auth/register` | Inscription | Non |
| POST | `/api/auth/login` | Connexion | Non |
| POST | `/api/v1/offers/search` | Recherche d'offres | Non |
| GET | `/api/v1/offers/{id}` | Détail d'une offre | Non |
| POST | `/api/v1/reservations` | Créer une réservation | Oui |
| GET | `/api/v1/reservations/me` | Historique des réservations | Oui |
| PATCH | `/api/v1/reservations/{id}/cancel` | Annuler une réservation | Oui |
| GET | `/api/v1/support/messages/{conversationId}` | Historique du chat | Oui |
| WS | `/ws` (STOMP) | Connexion WebSocket pour le chat | Non (voir note sécurité) |

## Structure de données

Le script SQL de structure des tables ainsi que le modèle conceptuel de données complet sont détaillés dans l'**Architecture Definition Document V4**, section 5.2.

## Auteur

Fatoumata — Développeuse Full Stack Bac+5, formation OpenClassrooms — Projet P13, juin 2026
