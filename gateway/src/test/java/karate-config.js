function fn() {
  var env = karate.env; // Récupérer la variable système 'karate.env'
  karate.log('karate.env system property was:', env);
  if (!env) {
    env = 'dev';
  }

  var config = {
    env: env,
  }

  if (env == 'dev') {
    // Configuration spécifique pour 'dev'
    config.serviceAuthUrl = 'http://localhost:8080/escamiage/utilisateur';
    config.serviceJeuUrl = 'http://localhost:8080/api/jeu/';
    config.serviceRecompenseUrl = 'http://localhost:8080/escamiage/recompense';
    config.serviceActualiteUrl = 'http://localhost:8080/escamiage/actualite';
  }
  return config;
}

