Feature: Enigme end-point

  Background:
    * url serviceJeuUrl

  Scenario: Créer, récupérer une enigme, la mettre à jour puis la supprimer
    # connexion Admin
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization

    # création enigme OK
    Given path 'enigmes'
    And header Authorization = token
    And request read('enigmeOK.json')
    When method post
    Then status 201
    And match responseHeaders.Location == '#notnull'

    * def id = response.id

    # récupération de l'enigme existante
    Given path 'enigmes', id
    And header Authorization = token
    When method get
    Then status 200

    # récupération des énigmes
    Given path 'enigmes'
    And header Authorization = token
    When method get
    Then status 200

    # récuperation énigmes inexistante
    Given path 'enigmes', 0
    And header Authorization = token
    When method get
    Then status 404

    # mettre à jour une énigme
    Given path 'enigmes', id
    And header Authorization = token
    And request read('enigmeMAJ.json')
    When method put
    Then status 200

    # supprimer une enigme
    Given path 'enigmes', id
    And header Authorization = token
    When method delete
    Then status 204

  Scenario: Créer énigme puis la récupérer sans token - ERREUR 401
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization
      # création
    Given path 'enigmes'
    And header Authorization = token
    And request read('enigmeOK.json')
    When method post
    Then status 201

    * def id = response.id

    # récupération
    Given path 'enigmes', id
    When method get
    Then status 401

  Scenario: Créer énigme et récupérer des engimes avec un token role par défaut (JOUEUR) - ERREUR 403
    # connexion User
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'joueur@joueur.com', password: 'miage' }
    * def token = result.responseHeaders.Authorization

    # création
    Given path 'enigmes'
    And header Authorization = token
    And request read('enigmeOK.json')
    When method post
    Then status 403

    # récuperer des énigmes
    Given path 'enigmes'
    And header Authorization = token
    When method get
    Then status 403

  Scenario: Création d'un enigme BAD FORMAT - ERREUR 400
    # connexion Admin
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization

    # création enigme KO
    Given path 'enigmes'
    And header Authorization = token
    And request read('enigmeKO.json')
    When method post
    Then status 400
