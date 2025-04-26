Feature: Repondre à la master question d'une partie

  Scenario: Soit on repond bon ou faux (appel via un autre fichier)
    Given url serviceJeuUrl
    And path 'parties', id, 'master'
    And header Authorization = token
    And request {answer : '#(reponse)'}
    Given method put
    Then status 406