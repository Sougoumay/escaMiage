Feature: Récupération des deux types de classement


  Background:
    * url serviceRecompenseUrl
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'joueur@joueur.com', password: 'miage' }
    * def token = result.responseHeaders.Authorization

  Scenario: Récuperation classement HEBDO
    Given path 'classement'
    And header Authorization = token
    When method get
    Then status 200


  Scenario: Récuperation classement eternel
    Given path 'classement-hebdo'
    And header Authorization = token
    When method get
    Then status 200
