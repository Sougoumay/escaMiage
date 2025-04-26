Feature: Répondre à une énigme

  Scenario: Envoyer une réponse à une énigme donnée
    Given url serviceJeuUrl
    And path 'parties', partieId, 'enigmes', idEnigme
    And header Authorization = token
    And request { answer: 'miage' }
    When method put
    Then status 200