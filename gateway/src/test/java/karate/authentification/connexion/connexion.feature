Feature: Tester la connexion

  Background:
    * url serviceAuthUrl
    * def email = 'rberrekam2@gmail.com'
    * def password = 'OUmay31122014..@'


  Scenario: Inscription d'un utilisateur
    * def result = call read('preInscription.feature')
    * def email = result.email
    * def id = result.id

  Scenario: Connexion OK
    Given path 'connexion'
    And request { "email": '#(email)', "password": '#(password)' }
    When method post
    Then status 200
    And match responseHeaders.Authorization == '#notnull'

  Scenario: Connexion email KO
    Given path 'connexion'
    And request { "email": 'rberrekam', "password": '#(password)' }
    When method post
    Then status 401

  Scenario: Connexion password KO
    Given path 'connexion'
    And request { "email": '#(email)', "password": '123456' }
    When method post
    Then status 401