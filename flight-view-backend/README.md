# Flight-View-Backend

## Fonctionnalités

Les fonctionnalités suivantes ont été implémentées et des exemples de requêtes HTTP testées sont disponibles dans le fichier Requests.http :

### Recherche des avis
Recherche (paginée) de tous les avis ou filtre suivant les critères "note", "compagnie aérienne", "date de création", "non répondus".
Les avis sont triés suivant l'élément "sort" transmis dans le paramètre "pageable" (tri par défaut sur la date). 

### Recherche d'un avis par son identifiant

### Ajout d'une réponse sur un avis

### Marquage d'un avis comme "publié", "rejeté" ou "traité"

## Modules

Le projet est structuré de la manière suivante :

### flight-view-business
- Entités persistantes
- DTOs transmis aux clients
- Exceptions métiers
- Repositories pour effectuer les requêtes
- Spécifications JPA pour les requêtes personnalisées

### flight-view-service
- Services métiers mis à disposition des contrôleurs
- Mappers Entités <-> DTOs

### flight-view-publication
- Contrôleurs REST
- Gestion des exceptions ("Controller Advice")
- Configuration Spring

### flight-view-application
- "Runner"
- Configuration yaml de l'application

## Modélisation des données
Les entités suivantes ont été créées :

### Airport
Données d'un aéroport

### Company
Compagnie aérienne

### Customer
Passager d'un vol

### Flight
Données d'un vol

### FlightView
Avis d'un passager pour un vol

### Liens entre les entités
- un aéroport est associé à plusieurs vols (en tant que "point de départ"), le vol n'est associé qu'à un aéroport de départ : lien "one to many"
- un aéroport est associé à plusieurs vols (en tant que "point d'arrivée"), le vol n'est associé qu'à un aéroport d'arrivée : lien "one to many"
- une compagnie aérienne est associé à plusieurs vols, un vol n'est associé qu'à une seule compagnie : lien "one to many"
- un vol est associé à plusieurs avis, un avis n'est associé qu'à un vol : lien "one to many"
- un vol est associé à plusieurs passager, un avis n'est associé qu'à un passager : lien "one to many"

## Base de données et chargement des données de tests
Le choix de la base de données s'est porté sur Postgresql qui figurait parmi les prérequis pour le poste.

Le répertoire init-database met à disposition 2 fichiers :
### un script init.sql (DDL et DML) pour créer les tables (et les contraintes d'intégrité) et charger quelques données de tests

### un fichier docker-compose.yml pour charger une image Docker officielle de postgres en local et exécuter ce script
Pour effectuer ce chargement, il faut exécuter la commande suivante : docker-compose up -d
