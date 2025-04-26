# Étape 1 : Build de l'application Angular
FROM node:22.12.0 AS build

WORKDIR /app

# Copier les fichiers de configuration npm
COPY package.json package-lock.json ./
RUN npm install --legacy-peer-deps

# Copier le reste du code source
COPY . .

# Build de l'application Angular
RUN npm run build --prod

# Étape 2 : Servir l'application Angular via le serveur de développement Angular
FROM node:22.12.0

WORKDIR /app

# Copier les fichiers générés par le build
COPY --from=build /app/dist/ /app/dist/

# Exposer le port 4200 pour accéder à l'application Angular
EXPOSE 4200

# Démarrer le serveur de développement Angular
CMD ["npx", "http-server", "dist/frontend", "-p", "4200"]
