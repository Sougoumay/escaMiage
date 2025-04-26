Feature: Création d'un badge

  Scenario: Connexion en tant qu admin et creation du badge
    # connexion en mode admin
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization

    # création du badge
    Given url serviceRecompenseUrl
    And path 'badge'
    And header Authorization = token
    And request data
    When method post