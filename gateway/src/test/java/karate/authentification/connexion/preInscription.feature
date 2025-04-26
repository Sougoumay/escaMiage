@ignore
Feature: Inscription d'un utilisateur (utile pour tester la connexion après)

  Scenario: Inscription d'un nouvel utilisateur
    Given url serviceAuthUrl
    And request read('registerI.json')
    When method post
    Then status 201
