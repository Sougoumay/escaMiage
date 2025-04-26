---
title: Intégration et perspectives
sidebar_position: 4
---

# 3- Intégration et perspectives

### a) Mise en place de GitLab CI/CD

Nous avons mis en place une pipeline CI/CD avec GitLab pour automatiser les étapes de construction, de test et de déploiement de notre application. Cette approche nous permet de garantir que chaque modification du code est immédiatement testée, et que les versions déployées sont toujours stables. Grâce à cette intégration continue, nous avons pu détecter rapidement les erreurs et maintenir une qualité constante du code tout au long du développement.

#### Fonctionnement de la pipeline :

- Tests unitaires : À chaque Merge Request, les tests unitaires sont automatiquement lancés pour vérifier le bon fonctionnement des différentes méthodes et composants du backend.


- Tests d'intégration : De même, les tests d'intégration sont exécutés afin de garantir que la communication entre les services via la gateway fonctionne comme prévu.


- Gestion des secrets : Pour garantir la sécurité des informations sensibles (comme les mots de passe, les clés API, etc.), nous avons mis en place un système de gestion des secrets, par exemple pour le Docker Compose. Bien que nous soyons en environnement local, nous avons anticipé la nécessité de sécuriser ces informations en prévision de futures mises en production.


- Déploiement automatique sur le dépôt principal : Une fois que les tests sont réussis et que les modifications sont validées, le code est automatiquement poussé sur la branche master de notre dépôt, via une pipeline dédiée.


Cette pipeline CI/CD garantit non seulement la qualité du code, mais aussi une gestion sécurisée des informations sensibles et un déploiement fluide des nouvelles versions de l'application.


### b) Lancement du projet avec Docker et le README

Le projet a été configuré pour être facilement exécuté en local, en version de développement, via Docker. Un README détaillé a été fourni pour guider les utilisateurs dans le processus de mise en place de l'environnement local. 

Ce guide inclut les instructions nécessaires pour récupérer le projet, configurer Docker et lancer les différents services de l’application (backend, frontend, bases de données, etc.). Ce processus vise à permettre à tout développeur ou utilisateur de tester rapidement le projet dans son environnement local, sans avoir à se soucier de la configuration des services ou de l'infrastructure.


### c) Problèmes rencontrés et solutions apportées

L’une des principales difficultés rencontrées durant cette phase a été de définir correctement les variables d’environnement et de s’assurer que tous les services fonctionnaient ensemble via Docker. Nous avons dû faire quelques ajustements dans le fichier docker-compose.yml pour garantir que tous les services se connectent correctement entre eux et avec la base de données. Ces ajustements ont été documentés dans le README pour faciliter l’expérience des utilisateurs.

### d) Axes d'améliorations

Parmi les axes d’amélioration identifiés, la mise en place d’un mécanisme de refresh token constitue une évolution importante du système d’authentification. Actuellement, l’accès repose uniquement sur un token d’authentification classique, ce qui limite la gestion fine des sessions utilisateurs. L’intégration d’un refresh token permettrait de prolonger les sessions de manière sécurisée, sans avoir à redemander les identifiants de l’utilisateur, tout en renforçant la sécurité via un cycle de vie contrôlé des tokens.

En parallèle, dans une logique DevOps et de fiabilisation des processus, nous envisageons également d’améliorer le monitoring et l’automatisation au sein du dépôt GitLab. L’objectif serait d’intégrer davantage d’indicateurs de suivi (tests, qualité de code, couverture, alertes...) directement dans les pipelines CI/CD, et de pousser l’automatisation sur des tâches comme les déploiements, les tests d’intégration ou le linting, afin de gagner en efficacité et en réactivité tout au long du cycle de développement.