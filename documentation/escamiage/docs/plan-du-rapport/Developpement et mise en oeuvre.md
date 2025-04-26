---
title: Développement et mise en oeuvre
sidebar_position: 3
---

# 2- Développement et mise en oeuvre

### a) Architecture et conception

#### a. Découpage des services et interactions

Le découpage des services a été une première étape complexe, car il ne fallait pas trop découper les fonctionnalités tout en respectant les principes d'une architecture microservices. Étant donné que le projet porte sur un escape game en ligne, nous avons dû réfléchir à la manière d’organiser les différents services tout en gardant à l’esprit la modularité et la scalabilité de l’application.
Nous avons donc identifié plusieurs services distincts : une partie gestion par l'administrateur, une partie inscription et connexion des utilisateurs, une partie dédiée au jeu lui-même où les utilisateurs peuvent interagir et jouer, et enfin une partie pour la gestion des données administratives et des sessions de jeu.

#### b. Modèle de données
L’architecture du projet est une étape cruciale dans la mise en place du système, surtout avec l'intégration de 5 microservices qui communiquent entre eux. Il était donc essentiel de définir un modèle de données clair et cohérent. Nous avons choisi d’intégrer ce modèle directement dans le schéma d'architecture pour visualiser plus facilement les interactions entre les différents services.


Le schéma des interactions avec et entre les différents services :
<img src="/img/schema-architecture-interop.png" alt="Texte alternatif" />

Le modèle des données :
<img src="/img/modele-des-donnees.png" alt="Texte alternatif" />

Un aperçu du tableau des URIs :
<img src="/img/apercu-tableau-uri.png" alt="Texte alternatif" />

Lien vers le tableau des URIs et le modèle de données :
[tableau-des-uri-et-modele-de-donnees](https://docs.google.com/spreadsheets/d/1jWPU676QsItZb8noCxyTAq0A_B6VG-T7nyCnkC7anDQ/edit?gid=0#gid=0)

Un aperçu des premières maquettes réalisées :
<img src="/img/maquette-premiere-version.png" alt="Texte alternatif" />


### b) Implémentation

#### a. Backend

##### API REST
Pour le backend, nous avons adopté une architecture API REST permettant à chaque service de gérer des requêtes HTTP de manière claire et structurée.

##### SPRING BOOT
Pour assurer la sécurité de l’application, Spring Security a été utilisé pour gérer l’authentification et l’autorisation des utilisateurs, et nous avons intégré JWT (JSON Web Tokens) pour sécuriser les échanges entre le client et les différents services.

##### Communication avec RabbitMQ
Les services sont organisés sous forme de microservices, chacun étant responsable d'une fonctionnalité spécifique. Grâce à l’utilisation de RabbitMQ, les microservices échangent des messages asynchrones pour traiter les tâches et coordonner les actions sans dépendre d’appels synchronisés bloquants. Chaque service est autonome, ce qui permet de maintenir une architecture modulaire, facilement scalable et flexible.

Les microservices backend communiquent entre eux via RabbitMQ, un système de messagerie asynchrone qui facilite l’échange d'informations entre les services de manière fiable et scalable. Cela permet une gestion fluide de la communication inter-service, en déchargeant les services des appels synchronisés bloquants et en garantissant une meilleure performance.

##### Consul

##### Worker .NET
Le service de notification est développé en .NET. Étant donné qu’il ne fait qu’envoyer des e-mails — c’est-à-dire des actions s’exécutant en arrière-plan en réponse à des messages asynchrones (par exemple via RabbitMQ), ou pour réaliser des tâches planifiées comme l’envoi d’e-mails d’anniversaire — le Worker Service proposé par .NET est parfaitement adapté à ce type de besoin.

Des exemples mails gérés par ce service : 
Mail le jour de l'anniversaire :
<img src="/img/mail-anniversaire.png" alt="Texte alternatif" />

Mail des statistiques hebdomadaire :
<img src="/img/mail-stats-hebdo.png" alt="Texte alternatif" />

Mail de réinitialisation du mot de passe :
<img src="/img/mail-reinit-mdp.png" alt="Texte alternatif" />


#### b. Frontend

Pour le frontend, nous avons opté pour Angular, un framework puissant et flexible pour créer des applications web dynamiques. Le projet a été structuré en composants Angular permettant une gestion modulaire de l'interface utilisateur. Les données sont récupérées via des appels HTTP aux API backend et via des messages RabbitMQ pour assurer une communication fluide avec le backend.

La structure des composants de l’application est pensée de manière modulaire et fonctionnelle, favorisant la lisibilité, la maintenabilité et l’évolutivité du projet. Chaque fonctionnalité ou domaine métier est isolé dans un dossier propre situé sous src/app, ce qui permet un découplage logique du code et facilite le travail en équipe. Par exemple, le dossier acces regroupe tous les composants liés à l’authentification des utilisateurs, notamment login et register, responsables de la connexion et de l’inscription. Le module actualite est plus riche, car il gère toute la logique autour des publications : création, affichage, modification, réaction aux posts, ainsi que les repositories et services permettant d'assurer la persistance et l'interaction avec le backend.

De manière similaire, le dossier badge centralise les composants permettant la création, visualisation et modification des badges, avec une séparation claire entre la logique métier et la présentation via les services et repositories dédiés. Le module enigme est plus ciblé, avec des composants orientés vers la gestion des énigmes, leur création et leur édition. Le dossier jeu, quant à lui, regroupe l’ensemble des composants nécessaires à la logique du jeu, à sa page de règles et à la communication avec les données via services et repositories. Cela permet de maintenir une logique métier cohérente.

Le module user contient plusieurs sous-fonctionnalités comme l’affichage des utilisateurs, la gestion des feedbacks et suggestions, le dashboard utilisateur, ainsi que le profil, qui est suffisamment riche pour être extrait dans un sous-module à part entière. Le dossier profil encapsule les composants liés à l’affichage et à la modification du profil utilisateur, tout en disposant de ses propres services et repositories pour gérer la logique métier de manière indépendante.

Cette organisation favorise une approche scalable et modulaire. Elle permet également une réutilisabilité des composants, une séparation des responsabilités, et s’aligne avec les bonnes pratiques des architectures modernes orientées composants.


#### c. Documentation API avec Swagger

La documentation de l'API a été réalisée avec Swagger, qui génère automatiquement une interface interactive et permet de tester directement les différents endpoints de l'API. Cela facilite la compréhension de l'API par d'autres développeurs et permet une intégration plus rapide et plus fluide des services.

### c) Tests et assurance qualité

#### a. Test unitaires (JUnit, Mockito)

Pour garantir la fiabilité du code, nous avons mis en place des tests unitaires à l'aide de JUnit, combinés avec Mockito pour le mocking des dépendances externes. Ces tests ont été réalisés pour chaque méthode et composant du backend afin de vérifier leur bon fonctionnement de manière isolée. L'objectif était de s'assurer que chaque fonction se comporte comme prévu dans un environnement contrôlé, sans interagir avec des composants externes ou des bases de données réelles.

Avec Mockito, nous avons pu simuler des comportements de dépendances (par exemple, des services externes ou des objets complexes) et vérifier que les interactions avec ces dépendances se passent comme prévu. Cela nous a permis de tester la logique interne des composants sans avoir besoin d'implémenter des appels réels, rendant les tests plus rapides et plus ciblés.

#### b. Tests d'intégration (Karaté)

Pour les tests d'intégration, nous avons utilisé Karaté pour tester l'interaction entre les différents microservices. Ces tests se sont concentrés sur la communication entre les services via la gateway, en passant par les endpoints exposés par cette dernière. Contrairement aux tests unitaires qui isolent les composants, ces tests simulent des appels réels entre les microservices, en vérifiant que l'ensemble du système fonctionne comme prévu.

Les tests d'intégration ont permis de vérifier le bon fonctionnement des flux de données entre les services, tout en simulant des requêtes HTTP réelles et en validant les réponses des microservices. Cela a garanti que les microservices interagissent correctement, en tenant compte des différents cas d'usage dans un environnement de production.

Pour éviter d’impacter la base de données en production, une base de données de test a été mise en place, permettant de simuler des interactions réelles sans toucher aux données réelles du système.

Cette base est une base de données MySQL et est initialisée avec un jeu de données de test. Lors du démarrage des tests, le schéma de la base est généré automatiquement, puis des données pertinentes sont insérées pour permettre de valider les interactions entre les composants applicatifs et la base de données. 

Un docker compose spécifique pour cette effet a été crée afin de lancer les spring boot avec le profil test, pour garantir un fonctionnement indépendant et une gestions plus précise des fichiers de test.

