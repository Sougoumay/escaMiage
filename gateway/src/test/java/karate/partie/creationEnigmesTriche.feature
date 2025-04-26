Feature: Création dynamique des enigmes

  Background:
    * url serviceJeuUrl
    * def creator = read('creationEnigme.feature')
    * def enigmesFn =
    """
    function(count,difficulte) {
      var out = [];
      for (var i = 0; i < count; i++) {
        out.push({ question: 'Question', reponse: 'miage', difficulte : difficulte, indice : 'indice',  theme : 'CRYPTO'  });
      }
      return out;
    }
    """

  Scenario: Creer les différentes enigmes des diiferentes difficulte
    * def enigmes = enigmesFn(5,'LICENCE3')
    * def result1 = call creator enigmes
    * def created = $result1[*].response
    * print 'created id is: ', created
    #* assert created.length == 5

    * def enigmes = enigmesFn(5,'MASTER1')
    * def result2 = call creator enigmes

    * def enigmes = enigmesFn(5,'MASTER2')
    * def result3 = call creator enigmes

