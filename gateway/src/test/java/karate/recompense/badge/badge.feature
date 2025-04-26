Feature: Badge endpoint

  Background:
    * url serviceRecompenseUrl

  Scenario: Créer, récupérer un badge puis le supprimer
    # connexion Admin
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization

    # création Badge OK
    * def jsonData = read('badgeOK.json')
    * def result = karate.call('creationBadge.feature', { data: jsonData })
    * match result.responseHeaders.Location == '#notnull'
    * match result.responseStatus == 201
    * def id = result.response.id

    # création Badge CONFLICT
    * def jsonData = read('badgeOK.json')
    * def result = karate.call('creationBadge.feature', { data: jsonData })
    * match result.responseStatus == 409

    # récupération du badge existant
    Given path 'badge', id
    And header Authorization = token
    When method get
    Then status 200

    # récupération des badges
    Given path 'badge'
    And header Authorization = token
    When method get
    Then status 200

    # récuperation badge inexistant
    Given path 'badge', 0
    And header Authorization = token
    When method get
    Then status 404

    # supprimer un badge
    Given path 'badge', id
    And header Authorization = token
    When method delete
    Then status 204

  Scenario: Créer badge puis la récupérer sans token - ERREUR 401
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization

     # création Badge OK
    * def jsonData = read('badgeOK.json')
    * def result = karate.call('creationBadge.feature', { data: jsonData })
    * match result.responseHeaders.Location == '#notnull'
    * match result.responseStatus == 201
    * def id = result.response.id

    # récupération
    Given path 'badge', id
    When method get
    Then status 401

   # supprimer un badge (en cas ou on relance les test directs)
    Given path 'badge', id
    And header Authorization = token
    When method delete
    Then status 204

  Scenario: Créer badge et récupérer des badge avec un token role par défaut (JOUEUR) - ERREUR 403
    # connexion User
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'joueur@joueur.com', password: 'miage' }
    * def token = result.responseHeaders.Authorization

     # création Badge OK
    * def jsonData = read('badgeOK2.json')
    * def result = karate.call('creationBadge.feature', { data: jsonData })
    * match result.responseHeaders.Location == '#notnull'
    * match result.responseStatus == 201
    * def id = result.response.id

    # récuperer le badge créer sans token
    Given path 'badge', id
    And header Authorization = token
    When method get
    Then status 403

  Scenario: Création d'un badge BAD FORMAT - ERREUR 400
    # création enigme KO
    * def jsonData = read('badgeKO.json')
    * def result = karate.call('creationBadge.feature', { data: jsonData })
    * match result.responseStatus == 400
