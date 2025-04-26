Feature: Post endpoint

  Background:
    * url serviceActualiteUrl

  Scenario: Créer, récupérer un post puis le supprimer
    # connexion Admin
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization

    # création Post OK
    * def jsonData = read('postOK.json')
    * def result = karate.call('creationPost.feature', { data: jsonData })
    * match result.responseHeaders.Location == '#notnull'
    * match result.responseStatus == 201
    * def id = result.response.idPost

    # mettre à jour post  OK
    Given path id
    And header Authorization = token
    And request read('postMAJ.json')
    When method put
    Then status 200

    # récupération du badge existant
    Given path id
    And header Authorization = token
    When method get
    Then status 200

    # récupération des badges
    Given header Authorization = token
    When method get
    Then status 200

    # récuperation badge inexistant
    Given path 0
    And header Authorization = token
    When method get
    Then status 404

    # supprimer un post
    Given path id
    And header Authorization = token
    When method delete
    Then status 204

  Scenario: Créer post puis le récupérer et supprimer sans token - ERREUR 401
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization

     # création Badge OK
    * def jsonData = read('postOK.json')
    * def result = karate.call('creationPost.feature', { data: jsonData })
    * match result.responseHeaders.Location == '#notnull'
    * match result.responseStatus == 201
    * def id = result.response.idPost

    # récupération
    Given path id
    When method get
    Then status 401

   # supprimer un badge (en cas ou on relance les test directs)
    Given path id
    When method delete
    Then status 401


  Scenario: Création d'un post BAD FORMAT - ERREUR 400
    # création enigme KO
    * def jsonData = read('postKO.json')
    * def result = karate.call('creationPost.feature', { data: jsonData })
    * match result.responseStatus == 400
