Feature: Jouer une partie

  Background:
    * url serviceJeuUrl

    * def result = call read('classpath:karate/utils/connexionParam.feature') { email: 'joueur@joueur.com', password: 'miage' }

    * def token = result.responseHeaders.Authorization
    # Boucle sur chaque id et appelle la feature "repondreEnigme"

    * def repondreEnigme =
    """
      function(partieId, enigmes, token,reponse) {
        for (var i = 0; i < enigmes.length; i++) {
          karate.call('repondreEnigme.feature', {
            partieId: partieId,
            idEnigme: enigmes[i].id,
            token: token,
            reponse : reponse
          });
        }
      }
     """

    * def recupererCesar =
    """
      function(partieId) {
          let result = karate.call('cesar.feature', {
            id: partieId,
          });
          return result.response;
      }
     """

    * def repondreMasterQuestion =
    """
      function(cesar, isCorrect, token, id) {
          let code;
          let reponsesPossibles = {
              1: 'nnnnnnnnnnnnnnn',
              2: 'ooooooooooooooo',
              3: 'ppppppppppppppp',
              4: 'qqqqqqqqqqqqqqq',
              5: 'rrrrrrrrrrrrrrr'}
          if (isCorrect) {
              // Le code clair est 'mmmmmmmmmmmmmmm'
              code = reponsesPossibles[cesar]
          } else {
              // Mauvaise réponse volontaire
              code = 'pinoupinoupinou';
          }
          karate.log('Code final envoyé =', code);
          var result = karate.call('master.feature', {
              id: id,
              token: token,
              reponse: code,
          });

          return result.response;
      }
    """
    * def repondreMasterQuestion2 =
    """
      function(cesar, isCorrect, token, id) {
          let code;
          let reponsesPossibles = {
              1: 'nnnnnnnnnnnnnnn',
              2: 'ooooooooooooooo',
              3: 'ppppppppppppppp',
              4: 'qqqqqqqqqqqqqqq',
              5: 'rrrrrrrrrrrrrrr'}
          if (isCorrect) {
              // Le code clair est 'mmmmmmmmmmmmmmm'
              code = reponsesPossibles[cesar]
          } else {
              // Mauvaise réponse volontaire
              code = 'pinoupinoupinou';
          }
          karate.log('Code final envoyé =', code);
          var result = karate.call('master2.feature', {
              id: id,
              token: token,
              reponse: code,
          });

          return result.response;
      }
    """

  Scenario: Jouer une partie et la finir !
    * call read('creationEnigmesTriche.feature')

    # Créer une partie
    Given path 'parties'
    And header Authorization = token
    Given method post
    Then status 201
    And def id = response[0].partieId
    And def enigmes = response

    # Répondre faux à toutes les questions
    * eval repondreEnigme(id,enigmes,token,'miage2')

    # Répondre bon à toutes les questions
    * eval repondreEnigme(id,enigmes,token,'miage')

    # récupère la partie
    Given path 'parties', id
    And header Authorization = token
    Given method get
    Then status 200

    # Récupère le décalage de césar {idPartie}/enigmes/{idReponse}/indice
    * def cesar =  recupererCesar(id)

    # Répondre à la master question - faux
    * def resultat = repondreMasterQuestion(cesar,false,token,id,200)
    * match resultat.etat == 'ENCOURS'

    # Répondre à la master question - vrai
    * def resultat = repondreMasterQuestion(cesar,true,token,id,200)
    * match resultat.etat == 'TERMINE'

    # récuperer ses stats de la partie 200
    Given path 'parties', id, 'stats'
    And header Authorization = token
    Given method get
    Then status 200

    # re répondre à la master question 406
    * def resultat = repondreMasterQuestion2(cesar,true,token,id)

  Scenario: Jouer une partie et l'annuler !
    # Créer une partie
    Given path 'parties'
    And header Authorization = token
    Given method post
    Then status 201
    And def id = response[0].partieId

    # Annuler partie
    Given path 'parties', id
    And header Authorization = token
    Given method put
    Then status 200


  Scenario: Chrono terminé la partie se termine
    # Créer une partie
    Given path 'parties'
    And header Authorization = token
    Given method post
    Then status 201
    And def id = response[0].partieId

    # Terminer partie
    Given path 'parties', id, 'fin'
    And header Authorization = token
    Given method put
    Then status 200

    # Terminer partie 2
    Given path 'parties', id, 'fin'
    And header Authorization = token
    Given method put
    Then status 406




