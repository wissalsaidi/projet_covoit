# CovoURCA

CovoURCA est une petite application de covoiturage. L'idée de départ est simple : beaucoup de gens font le même trajet au quotidien — vers une gare, un centre-ville, une zone d'activité — mais n'ont aucun moyen simple de se regrouper. CovoURCA permet à n'importe qui de publier une demande de trajet, de se faire mettre en relation avec un conducteur disponible, et de suivre le trajet jusqu'à destination, avec un système de confirmation et de notation pour garder un minimum de confiance entre inconnus.

Ce n'est pas un produit fini avec une vraie communauté d'utilisateurs derrière, mais il a été construit comme une vraie application : authentification, base de données relationnelle, notifications en temps réel, et une interface qui reste simple à utiliser.

## Comment ça fonctionne, concrètement

Il y a deux façons de demander un trajet :

1. **Via une ligne prédéfinie** : trois lignes de covoiturage existent par défaut (Campus → Gare, Campus → Centre-Ville, Campus → Tinqueux), chacune avec ses points de rendez-vous affichés sur une carte. Un passager choisit un point de départ et d'arrivée sur la même ligne.
2. **Via une adresse libre** : si aucune ligne ne correspond, le passager peut saisir directement une adresse de départ et d'arrivée. Dans ce cas, on calcule la distance (formule de Haversine) entre ce point de départ et les arrêts des lignes existantes, pour ne notifier que les conducteurs dont une ligne passe à moins de 2 km.

Quand une demande est créée, tous les conducteurs concernés (ceux qui ont encore une place disponible dans leur véhicule) reçoivent une notification en temps réel via WebSocket. Le premier qui accepte récupère le trajet ; les autres voient la demande disparaître. Pour éviter qu'un conducteur n'accepte deux fois la même demande (cas de double-clic ou de requêtes concurrentes), on s'appuie sur un verrou optimiste côté base de données.

Une fois la demande acceptée, un trajet est créé avec un **code à 4 chiffres** généré aléatoirement. Ce code doit être communiqué de vive voix entre le conducteur et le passager au moment de la prise en charge, et confirmé dans l'application — une façon basique mais efficace de s'assurer qu'on monte bien dans la bonne voiture. Le trajet ne peut être marqué "terminé" qu'après cette confirmation.

À la fin du trajet, chacun peut noter l'autre. La moyenne des notes d'un utilisateur est ensuite visible par les futurs passagers/conducteurs, ce qui sert un peu de réputation.

## Stack technique

- **Backend :** Spring Boot 3.5 (Java 21), Spring Security pour l'authentification, Spring Data JPA pour la persistance, Spring WebSocket (STOMP/SockJS) pour les notifications en temps réel
- **Frontend :** Thymeleaf (rendu côté serveur) + Bootstrap 5 pour le style, Leaflet pour l'affichage des lignes et des points sur une carte
- **Base de données :** MySQL 8
- **Build :** Maven (le wrapper `mvnw` est inclus, pas besoin d'installer Maven séparément)

## Lancer le projet

### Avec Docker (le plus simple)

```bash
docker compose up -d
```

Ça démarre trois conteneurs : MySQL, phpMyAdmin (pour aller fouiller dans la base si besoin) et l'application elle-même.

| Service     | URL                    |
| ----------- | ---------------------- |
| Application | http://localhost:8080  |
| phpMyAdmin  | http://localhost:9090  |

Au premier démarrage, la base est créée automatiquement et les trois lignes de covoiturage de démo sont insérées (voir `DataInitializer`) — pas besoin de remplir quoi que ce soit à la main pour avoir quelque chose à tester.

À noter : en mode Docker, les dossiers `templates` et `static` sont montés en volume avec le cache Thymeleaf désactivé. Concrètement ça veut dire qu'on peut modifier une page HTML et rafraîchir le navigateur sans avoir à reconstruire l'image.

### Sans Docker

Il faut avoir Java 21, Maven (ou utiliser le wrapper fourni) et une instance MySQL 8 qui tourne en local.

```bash
# créer une base "covourca" dans MySQL, ou laisser createDatabaseIfNotExist le faire pour vous
.\mvnw.cmd spring-boot:run
```

Par défaut l'application essaie de se connecter à `localhost:3306` avec l'utilisateur `root` et le mot de passe `root` (voir `application.properties`). Si votre installation MySQL est configurée différemment, surchargez les variables d'environnement `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` et `SPRING_DATASOURCE_PASSWORD`.

## Pages et routes principales

| Route              | Description                                            |
| ------------------ | -------------------------------------------------------|
| `/`                | Page d'accueil                                          |
| `/inscription`     | Création de compte (choix du rôle conducteur/passager) |
| `/connexion`       | Connexion                                               |
| `/dashboard`       | Tableau de bord : demandes en cours, trajets, carte     |
| `/trajets/{id}`    | Détail d'un trajet (code de confirmation, notation...)  |
| `/historique`      | Historique des trajets effectués                        |
| `/profil`          | Profil utilisateur et gestion des véhicules             |
| `/confidentialite` | Politique de confidentialité                            |
| `/conditions`      | Conditions d'utilisation                                |

## Fonctionnalités

- Inscription avec choix du rôle (conducteur et/ou passager)
- Création de demande de covoiturage, soit via une ligne existante, soit via une adresse libre
- Notification en temps réel des conducteurs compatibles dès qu'une demande est créée
- Acceptation d'une demande par un conducteur, avec gestion des places disponibles dans son véhicule
- Code de confirmation à 4 chiffres pour valider la prise en charge du passager
- Annulation possible côté passager (avant acceptation) ou côté conducteur (avec notification du passager)
- Système de notation après chaque trajet terminé
- Historique complet des trajets (en tant que conducteur ou passager)
- Gestion du profil et des véhicules associés
- Carte interactive (Leaflet) affichant les lignes et points de rendez-vous

## Limites connues

Quelques choses restent volontairement simples :

- Pas de paiement intégré, le covoiturage se fait sur la base de la confiance/arrangement entre étudiants
- La notification "adresse libre" se base sur une distance à vol d'oiseau (Haversine) et non sur un vrai calcul d'itinéraire
- Un conducteur ne peut être notifié que s'il a déjà déclaré un véhicule avec au moins une place disponible
