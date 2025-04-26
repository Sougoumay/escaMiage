---
title: Initialisation du projet
sidebar_position: 2
---

# 1- Initialisation du projet

### a) Genèse et objectifs : Contexte et enjeux du projet

L'objectif principal de ce projet était de le traiter comme un véritable projet d'entreprise. Il s'inscrit dans le cadre du cours d'interopérabilité, mais mobilise en réalité l’ensemble des enseignements acquis tout au long du parcours en MIAGE. Nous avons cherché à appliquer des concepts théoriques à un cas pratique concret, en respectant les contraintes techniques et les exigences imposées par le cahier des charges.

Afin de rendre l’expérience plus ludique et motivante, nous avons choisi de développer une plateforme d'escape game en ligne, sur le thème de la MIAGE. Ce choix, à la fois original et en lien avec notre cursus, a permis de garder une dimension fun et engageante, même lors des moments de challenge technique. L'idée était de créer un projet qui allie l'utile à l'agréable, et qui soit suffisamment motivant pour l'ensemble de l'équipe tout au long du développement.

Notre vision du projet était de créer une plateforme innovante qui reflète l'esprit de la MIAGE, tout en offrant une expérience immersive et divertissante. L’escape game en ligne était une manière de rendre la mise en pratique des concepts abordés pendant la formation plus interactive et engageante, tout en intégrant des mécanismes de sécurité et de gestion de données réels. Nous avons souhaité construire un projet qui pourrait être utilisé non seulement comme une application fun, mais aussi comme une véritable démonstration de notre expertise technique et de notre capacité à gérer des projets complexes.

- Les consignes imposées étaient les suivantes :

    - Une architecture microservices pour une meilleure modularité et évolutivité.


    - La mise en place de mécanismes de sécurité et d’authentification, afin de garantir la protection des données utilisateurs et des interactions sur la plateforme.


    - L’implémentation d’un service en .NET, afin d’appliquer et d’expérimenter une technologie différente de celle que nous utilisons habituellement (Spring Boot).


    - La nécessité de tester l’application, pour garantir la qualité du code et la fiabilité des fonctionnalités mises en place.


    - L’utilisation obligatoire de Docker, pour assurer la portabilité et la reproductibilité de l’environnement de développement.

Dans le but de construire la base de notre escape game et de poser les fondations du cœur de notre projet, nous avons fait le choix d’architecturer notre système autour de microservices indépendants. Ces microservices interagissent de manière indirecte, favorisant ainsi la modularité et la scalabilité de notre application. Parmi ces services.

- On retrouve notamment :


    - Le microservice authentification : Ce microservice joue un rôle central puisqu’il est chargé de la gestion des comptes utilisateurs et de leurs données personnelles. Il permet à un nouvel utilisateur de s’inscrire sur la plateforme, puis de se connecter à son compte. Une fois connecté, l’utilisateur peut consulter et modifier ses informations personnelles. Ce microservice prend également en charge la réinitialisation du mot de passe, grâce à un système de vérification et de validation du code envoyé à l’utilisateur. Enfin, il offre la possibilité de créer un compte administrateur, disposant de droits pour superviser et gérer l’ensemble de l’application.

    - Le microservice Jeu : Ce microservice est dédié à la gestion de l’escape game dans sa globalité. Il se compose de trois contrôleurs, chacun responsable d’un aspect essentiel du jeu : la gestion des parties, des énigmes, et de la question finale (ou "question master"). Le contrôleur en charge des parties permet notamment de démarrer et de terminer une session de jeu, assurant ainsi le bon déroulement de l’expérience utilisateur. Du côté des énigmes, le microservice offre une gestion complète allant de leur création jusqu’à leur suppression. Enfin, il fournit également des fonctionnalités liées à la consultation des indices disponibles ainsi qu’au suivi du nombre de tentatives effectuées pour répondre à la question finale, concluant ainsi la partie. L’utilisateur a également la possibilité de faire remonter ses problèmes ou ses questions grâce à un système de gestion des retours, qu’il s’agisse de feedbacks ou de suggestions.

    - Le microservice  actualité : Ce microservice est chargé de la gestion des publications sur la plateforme, communément appelées posts. Il offre un ensemble de fonctionnalités permettant de créer, consulter, modifier ou supprimer des posts. En complément de cette gestion, le microservice permet également aux utilisateurs d’interagir avec les publications en y réagissant à travers différentes réactions disponibles.

    - Le microservice récompense : Ce microservice est dédié à la gestion des éléments liés à la valorisation et au classement des utilisateurs sur la plateforme. Il regroupe principalement deux aspects fonctionnels : la gestion des badges et celle du classement. À travers ses différents contrôleurs, le service permet la gestion des badges attribués aux utilisateurs. Ces badges, symboles de progression ou de réussite, sont attribués automatiquement par RabbitMQ. Par ailleurs, ce microservice expose également des endpoints permettant de consulter les classements globaux ou hebdomadaires, offrant ainsi une visibilité sur la performance des joueurs.

    - Le microservice notification : Ce microservice est chargé de la gestion de l’ensemble des envois de courriels au sein de la plateforme. Son rôle est d'assurer la communication par mail avec les utilisateurs à différents moments clés et dans divers cas d’usage. Il permet notamment l’envoi d’un mail de bienvenue lors de l’inscription, d’un mail d’anniversaire le jour concerné, ou encore d’un mail de confirmation suite à une réponse reçue à un feedback précédemment envoyé. Ce service prend également en charge l’envoi de mails de réinitialisation de mot de passe ainsi que l’envoi régulier de statistiques hebdomadaires.


Ce projet a donc permis de conjuguer plusieurs compétences techniques tout en suivant un processus de développement Agile, avec une forte collaboration entre les membres de l’équipe. En choisissant de créer une plateforme d’escape game, nous avons réussi à allier travail sérieux et motivation personnelle, ce qui nous a permis de maintenir notre enthousiasme et notre investissement, même dans les moments les plus complexes du développement.

### b) Organisation et outils de gestion

Étant donné le temps limité et les défis rencontrés, une organisation rigoureuse était essentielle. Nous avons structuré l’équipe en deux sous-groupes : backend et frontend. Cette division nous a permis de concentrer nos efforts tout en assurant une communication constante via Discord, surtout lors des points réguliers pour ajuster notre travail et avancer rapidement.

####  a- Mise en place de l'organisation

Lors des premières réunions, nous avons rapidement défini notre vision du projet et clarifié nos objectifs. Cela a débouché sur la mise en place d'un tableau d'URIs et d'un schéma d’architecture que nous détaillerons plus tard. Pour organiser nos tâches sur le long terme, nous avons opté pour Trello, ce qui a facilité le suivi de l'avancement et assuré une répartition claire du travail.

####  b- Gestion du code avec GitLab

Initialement sur Pdicost, nous avons migré notre code vers GitLab pour bénéficier d'une meilleure gestion avec CI/CD et Merge Requests. Cette migration nous a permis de structurer le travail avec des branches dédiées et des revues de code régulières, garantissant ainsi une gestion rigoureuse du code.

Les merge requests ont véritablement été un atout majeur tout au long du développement du projet. Elles ont permis d’instaurer un processus de vérification rigoureux avant chaque intégration de code, garantissant ainsi la stabilité et la cohérence de l’ensemble du système. Grâce à elles, chaque membre de l’équipe pouvait proposer des modifications du code sur sa branche tout en bénéficiant de retours constructifs de la part des autres développeurs. Ce système de revue de code a non seulement permis de corriger les éventuelles erreurs ou incohérences en amont, mais aussi d’échanger sur les bonnes pratiques et d’améliorer continuellement la qualité du code. En somme, les merge requests ont été un outil essentiel pour maintenir une dynamique collaborative et viser un code propre, lisible et maintenable.

####  c- Qualité du code

L'objectif principal côté backend était de produire un code propre et maintenable, en respectant les bonnes pratiques de développement vues en MIAGE. Nous avons intégré des tests unitaires et veillé à la documentation adéquate, pour faciliter l'intégration avec le frontend et maintenir une qualité de code constante tout au long du développement.

Lien vers le Trello : [escamiage-Trello](https://trello.com/b/3Lh7adst/escape-game-miage)

Une capture du Trello pendant la dernière semaine du projet :
<img src="/img/capture-trello.png" alt="Texte alternatif" />

Un exemple d'une Issue créée sur GitLab :
<img src="/img/exemple-issue.png" alt="Texte alternatif" />

### c) Choix des technologies

Le cahier des charges spécifie que l'architecture devait reposer sur cinq microservices. Quatre d'entre eux sont développés en Spring Boot et utilisent Spring Security ainsi que JPA pour la gestion des données. Un des services est quant à lui développé en .NET et plus précisément sous forme de worker. Le choix du framework frontend s'est porté sur Angular, en raison de son efficacité pour le développement d'applications web dynamiques et réactives.

En ce qui concerne les bases de données, nous avons opté pour MySQL en raison de ses performances, de sa robustesse et de sa compatibilité avec notre architecture.Afin de garantir la qualité et la fiabilité du système, des tests unitaires ont été mis en place à l'aide de JUnit et Mockito. De plus, des tests d'intégration ont été réalisés en utilisant le framework Karate, permettant ainsi de tester l'ensemble des interactions entre les composants.

Tous les services, y compris le frontend, ont été dockerisés. Ils sont lancés via Docker Compose, qui orchestre également les bases de données, permettant une gestion simplifiée et cohérente de l'environnement. Concernant la communication entre les microservices, RabbitMQ a été intégré comme système de messagerie. De plus, un autre service Spring a été mis en place en tant que load balancer / gateway. Ce service centralise les demandes des utilisateurs, les redirigeant vers les microservices appropriés, et assure ainsi la gestion des requêtes et la haute disponibilité du système.

Enfin, pour gérer la découverte des services, chaque microservice, au démarrage, s'enregistre sur Consul, permettant ainsi une gestion dynamique de l'infrastructure. Nous détaillerons plus en profondeur les interactions entre ces composants dans les sections suivantes, cette partie servant simplement d'introduction aux choix technologiques.


