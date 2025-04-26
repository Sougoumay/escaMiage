Feature: Creation d'un post

  Scenario: Création d'un post à partir d'un parametre
    # connexion en mode admin
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization

    # création du badge
    Given url serviceActualiteUrl
    And header Authorization = token
    And request data
    When method post
