Feature: Ajouter un feedback

  Background:
    * url serviceJeuUrl
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'joueur@joueur.com', password: 'miage' }
    * def token = result.responseHeaders.Authorization

  Scenario: Petite question d'un utilisateur - OK
    # Créer une partie
    Given path 'feedback'
    And header Authorization = token
    And request read('feedbackOK.json')
    Given method post
    Then status 201


  Scenario: Petite question d'un utilisateur - KO
    # Créer une partie
    Given path 'feedback'
    And header Authorization = token
    And request read('feedbackKO.json')
    Given method post
    Then status 400
