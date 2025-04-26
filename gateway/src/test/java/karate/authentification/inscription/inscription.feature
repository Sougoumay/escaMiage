Feature: Test d'inscription avec validation des données

  Background:
    * url serviceAuthUrl

  Scenario: Tester l'inscription + suppression si doublon
    Given request read('register.json')
    When method post
    Then status 201
    And match response.id == '#number'
    And match karate.response.header('Authorization') != null
    And def id = response.id

    # On teste le conflit ensuite (email déjà utilisé)
    Given request read('register.json')
    When method post
    Then status 409

    # Et on supprime l'utilisateur
    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'admin@admin.com', password: 'Admin0101@' }
    * def token = result.responseHeaders.Authorization
    Given path id
    And header Authorization = token
    When method delete
    Then status 204

  Scenario: Tester l'inscription - Erreur de validation 400 (mot de passe trop court)
    Given request read('registerMotDePasse.json')
    When method post
    Then status 400
    #And match response.body.message == 'Le mot de passe doit contenir au moins 12 caractères'

  Scenario: Tester l'inscription - Erreur de validation 400 (nom invalide)
    Given request read('registerNomInvalide.json')
    When method post
    Then status 400
    #And match response.body.message == 'Le nom ne doit contenir que des lettres, espaces, apostrophes ou traits d\'union'

  Scenario: Tester l'inscription - Erreur de validation 400 (prénom invalide)
    Given request read('registerPrenomInvalide.json')
    When method post
    Then status 400
    #And match response.body.message == 'Le prénom ne doit contenir que des lettres, espaces, apostrophes ou traits d\'union'

  Scenario: Tester l'inscription - Erreur de validation 400 (pseudo trop court)
    Given request read('registerPseudoCourt.json')
    When method post
    Then status 400
    #And match response.body.message == 'Le pseudo doit contenir au moins 2 caractères'

  Scenario: Tester l'inscription - Erreur de validation 400 (date de naissance trop jeune)
    Given request read('registerDateNaissanceTropJeune.json')
    When method post
    Then status 400
    #And match response.body.message == 'L\'utilisateur doit avoir au moins 18 ans'

  Scenario: Tester l'inscription - Erreur de validation 400 (email invalide)
    Given request read('registerEmailInvalide.json')
    When method post
    Then status 400
    #And match response.body.message == 'E-mail invalide'
