Feature: Test CRUD sur l'utilisateur

  Background:
    * url serviceAuthUrl

  Scenario: Inscription d'un joueur, récupère son profil, le modifie et le supprime, essaie daccéder à un autre profil et tous les utilisateurs (réservé ADMIN)
    # La petite inscription CREATED
    Given request read('classpath:karate/authentification/inscription/register.json')
    When method post
    Then status 201
    And def id = response.id
    And def token = responseHeaders.Authorization

    # On récupere le profil OK
    Given path id
    And header Authorization = token
    When method get
    Then status 200

    # On modifie le profil OK
    Given path id
    And header Authorization = token
    And request read('modificationProfil.json')
    When method put
    Then status 200

    # On récupere le profil OK
    Given path id
    And header Authorization = token
    When method get
    Then status 200

    # On supprime son compte NO CONTENT
    Given path id
    And header Authorization = token
    When method delete
    Then status 204

    # On récupere le profil NOT FOUND
    Given path id
    And header Authorization = token
    When method get
    Then status 404

    # On récupere le profil UNFORBIDDEN
    Given header Authorization = token
    When method get
    Then status 403

    Given path 'admin'
    And header Authorization = token
    And request read('creationAdmin.json')
    When method post
    Then status 403
    And def idAdmin = response.id


  Scenario: Connexion d'un admin, récupère tous les users, créer un ADMIN
    # connexion Admin
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization

    # recuperation de tous les users
    Given header Authorization = token
    When method get
    Then status 200

    # créer un admin
    Given path 'admin'
    And header Authorization = token
    And request read('creationAdmin.json')
    When method post
    Then status 201
    And def idAdmin = response.id

    # supprimer l'admin
    Given path idAdmin
    And header Authorization = token
    When method delete
    Then status 204


