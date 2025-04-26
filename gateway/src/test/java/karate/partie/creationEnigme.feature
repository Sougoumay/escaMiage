Feature: Création d'une enigme dynamiquement

  Scenario: Création de l'énigme
    # connexion en mode admin
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization
    * match __arg == enigmes[__loop]

    # création de l'enigme
    Given url serviceJeuUrl
    And path 'enigmes'
    And header Authorization = token
    And request
    """
    {
      question: '#(__arg.question)',
      reponse: '#(__arg.reponse)',
      difficulte: '#(__arg.difficulte)',
      indice: '#(__arg.indice)',
      theme: '#(__arg.theme)'
    }
    """
    When method post
    Then status 201
