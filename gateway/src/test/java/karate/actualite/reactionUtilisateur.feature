Feature: Réagir à un post

  Background:
    * url serviceActualiteUrl
    # creation des posts
    * def creationPost = read('postOK.json')
    * def repeatCreationPost =
      """
      function() {
        for (var i = 0; i < 5; i++) {
          var result = karate.call('creationPost.feature', { data: creationPost });
          karate.log('Result for iteration ' + i + ':', result.response);
        }
      }
      """
    * repeatCreationPost()


  Scenario: Voir tous les posts + réagir à un post OK et KO
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'joueur@joueur.com', password: 'miage' }
    * def token = result.responseHeaders.Authorization

    Given header Authorization = token
    When method get
    Then status 200
    * def id = response[0].idPost

    Given path id, 'reaction'
    And header Authorization = token
    And request {typeReaction : 'FIRE' }
    When method put
    Then status 200

    Given path id, 'reaction'
    And header Authorization = token
    And request {typeReaction : 'FIREEEE' }
    When method put
    Then status 400



