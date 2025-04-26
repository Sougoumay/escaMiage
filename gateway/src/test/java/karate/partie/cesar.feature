Feature: Encore de la triche on va chercher le decalage de cesar d'une partie donné

  Scenario: Recuperer l'indice de la master question
    # connexion en mode admin
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization

    # récupération de l'indice
    Given url serviceJeuUrl
    And path 'parties', id, 'master', 'indice'
    And header Authorization = token
    Given method get
    Then status 200
    # match response == '#number'
