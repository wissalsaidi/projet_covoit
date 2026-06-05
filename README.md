# CovoURCA

Application web de covoiturage pour l'Université de Reims Champagne-Ardenne — Projet RT0809.

## Stack technique

- **Backend :** Spring Boot 3.5 (Java 21), Spring Security, Spring Data JPA, Spring WebSocket
- **Frontend :** Thymeleaf, Bootstrap 5, SockJS + STOMP
- **Base de données :** MySQL 8
- **Build :** Maven

## Lancer avec Docker (recommandé)

```bash
docker compose up -d
```

| Service     | URL                   |
| ----------- | --------------------- |
| Application | http://localhost:8080 |
| phpMyAdmin  | http://localhost:9090 |

La base de données et les données de démo (3 lignes de covoiturage) sont créées automatiquement au premier lancement.

## Lancer sans Docker

Prérequis : Java 21, Maven, MySQL 8.

```bash
# Configurer MySQL (base "covourca", user root / mot de passe dans application.properties)
.\mvnw.cmd spring-boot:run
```

## Routes publiques

| Route              | Page                         |
| ------------------ | ---------------------------- |
| `/`                | Accueil                      |
| `/inscription`     | Création de compte           |
| `/connexion`       | Connexion                    |
| `/confidentialite` | Politique de confidentialité |
| `/conditions`      | Conditions d'utilisation     |

## Fonctionnalités

- Inscription avec rôle conducteur / passager
- Création et gestion de demandes de covoiturage
- Acceptation de demande par un conducteur
- Code de confirmation à 4 chiffres pour valider un trajet
- Système de notation
- Notifications temps réel (WebSocket)
- Historique des trajets
- Profil utilisateur et gestion des véhicules
