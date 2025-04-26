# Étape 1 : Construction
FROM eclipse-temurin:21-jdk AS build

# Définir le répertoire de travail
WORKDIR /app

# Installer Maven (si nécessaire)
RUN apt-get update && apt-get install -y maven

# Copier les fichiers du projet dans l'image
COPY . .

# Construire le projet et générer le fichier JAR
RUN mvn clean package

# Étape 2 : Exécution
FROM eclipse-temurin:21-jdk

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

COPY wait-for-db.sh /wait-for-db.sh

RUN chmod +x /wait-for-db.sh

# Exposer le port de l'application
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar","/wait-for-db.sh"]
