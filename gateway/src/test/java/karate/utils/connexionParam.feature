@ignore
Feature: Connexion d'un administrateur

  Scenario: Connexion user admin
    Given url serviceAuthUrl
    And path 'connexion'
    And request { "email": '#(email)', "password": '#(password)' }
    When method post
    Then status 200
