# PROJET ESCAMIAGE - EQUIPE 1



## En place sur le poste

### a) Mettre en place le projet localement (attention c'est long ...)
1) On lance Docker Desktop
2) On se place à la racine du projet (quand on fait un ls on est censé voir les différents modules front, service-authentification)
3) `docker compose up --build`
4) `cd front`
5) `npm install`
6) `npm start`
7) Pour eteindre les containers `docker compose down`


### b) Pour seulement lancer un service en cas dev
* Dans ce cas on lancera la version dockeriser de la bdd, consul et la gateway
1) On lance Docker Desktop
2) On se place dans un module (service-authentification, service-jeu ...)
3) `docker compose up --build`
4) On lance le service spring boot gateway
4) On decommente la ligne `port: 808X` du fichier src/main/resources/application.yaml du service qu'on dev
5) On lance le service spring boot et c'est parti pour le dev
6) Pour eteindre les containers `docker compose down`
7) /!\ AVANT DE PUSH ON NOUBLIE PAS DE RECOMMENTER LA LIGNE `port: 8081` du service qu'on dev

### c) Seulement lancer les bdds (les service seront lancés manuellement)
1) Se placer dans le dossier utils
2) On lance Docker Desktop
3) `docker compose up --build`
4) La gateway prend le port 8080, donc pour lancer les autres services normalement une ligne est à décommenter
   `port: 808X` dans le fichier application.yaml respectif du service à dev
5) Pour eteindre les containers `docker compose down`
6) /!\ AVANT DE PUSH ON NOUBLIE PAS DE RECOMMENTER LA LIGNE `port: 808X` du service qu'on dev

### d) Dans le cas du lancement des tests d'intégration
1) On lance Docker Desktop
2) ``docker-compose -f docker-compose-test.yaml up --build -d``
3) `cd gateway`
4) `mvn test`

### e) Accés au différents Swagger
1) basePath:PORT/escamiage/service-name/docs
2) basePath:PORT/escamiage/service-name/swagger-ui.html

### f) Accés à la documentation
1) Le pdf envoyé
2) Pour lancer Docusaurus et avoir une version plus jolie et interactive, il faut avoir NodeJS d'installé avec une version supérieur à 18.0.0  (vérifier votre version avec "node -v" et si besoin installez la bonne version)
3) Placez vous à la racine du projet "/documentation/escamiage" et lancez le avec "npm run start" le site de la documentation va se lancer authomatiquement par la suite

## Utilisataire pour profiter de l'experience sur le front
* Compte ADMIN : admin@dmin.com / Admin0101@
* Compte JOUEUR : joueur@joueur.com / miage