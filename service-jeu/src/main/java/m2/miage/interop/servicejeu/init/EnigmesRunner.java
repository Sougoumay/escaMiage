package m2.miage.interop.servicejeu.init;

import m2.miage.interop.servicejeu.dao.EnigmeRepository;
import m2.miage.interop.servicejeu.entity.Enigme;
import m2.miage.interop.servicejeu.entity.enums.Difficulte;
import m2.miage.interop.servicejeu.entity.enums.Theme;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("!test")
@Component
public class EnigmesRunner implements CommandLineRunner {

    public final EnigmeRepository enigmeRepository;

    public EnigmesRunner(EnigmeRepository enigmeRepository) {
        this.enigmeRepository = enigmeRepository;
    }

    @Override
    public void run(String... args) {
        initEnigme();
    }

    private void initEnigme() {
        if (enigmeRepository.count() == 0) { // Vérifie si la table est vide avant d'insérer
            System.out.println("Insertion des énigmes dans la base de données...");

            List<Enigme> enigmes = List.of(
                    // 🔹 5 ÉNIGMES LICENCE3
                    new Enigme("Quel est l'algorithme de chiffrement symétrique utilisé dans AES ?",
                            Difficulte.LICENCE3, Theme.CRYPTO, "rijndael", "Il remplace le DES."),
                    new Enigme("Dans une base de données relationnelle, quelle est la forme normale qui élimine les dépendances fonctionnelles partielles ?",
                            Difficulte.LICENCE3, Theme.BDD, "2nf", "Elle suit la 1nf."),
                    new Enigme("Quelle est la complexité temporelle moyenne d’une recherche dans une table de hachage bien équilibrée ?",
                            Difficulte.LICENCE3, Theme.PROGRAMMATION, "o(1)", "C'est la meilleure complexité."),
                    new Enigme("Quel est le protocole utilisé pour sécuriser les communications sur Internet (HTTPS) ?",
                            Difficulte.LICENCE3, Theme.CRYPTO, "tls", "Succesheur de ssl."),
                    new Enigme("Dans le modèle OSI, à quelle couche appartient le protocole TCP ?",
                            Difficulte.LICENCE3, Theme.DATA, "transport", "C'est la 4eme couche."),

                    // 🔹 5 ÉNIGMES MASTER1
                    new Enigme("Quelle loi de probabilité suit un phénomène rare et indépendant ?",
                            Difficulte.MASTER1, Theme.STATISTIQUE, "poisson", "Elle modele le nombre d'evenements."),
                    new Enigme("Quel service cloud permet d'exécuter du code sans gérer l’infrastructure ?",
                            Difficulte.MASTER1, Theme.CLOUD, "serverless (ex: aws lambda)", "Utilise dans l'architecture faas."),
                    new Enigme("Quelle propriété assure qu’une transaction dans une base de données est totalement exécutée ou totalement annulée ?",
                            Difficulte.MASTER1, Theme.BDD, "atomicite", "C'est le a de acid."),
                    new Enigme("Quel est l’algorithme principal utilisé dans le chiffrement asymétrique ?",
                            Difficulte.MASTER1, Theme.CRYPTO, "rsa", "Base sur la factorisation."),
                    new Enigme("Quel type de partitionnement consiste à répartir les données en fonction d’un critère logique (ex: par date) ?",
                            Difficulte.MASTER1, Theme.BDD, "partitionnement horizontal", "Il segmente les lignes."),

                    // 🔹 5 ÉNIGMES MASTER2
                    new Enigme("Quel est le principal objectif de l’algorithme de Backpropagation en machine learning ?",
                            Difficulte.MASTER2, Theme.STATISTIQUE, "minimiser l'erreur", "Utilise pour entrainer les reseaux de neurones."),
                    new Enigme("Quel est le modèle économique qui permet aux entreprises de payer uniquement les ressources cloud utilisées ?",
                            Difficulte.MASTER2, Theme.CLOUD, "pay-as-you-go", "Reductions des couts inutiles."),
                    new Enigme("Quelle méthode de calcul est utilisée pour optimiser les requêtes dans une base de données relationnelle ?",
                            Difficulte.MASTER2, Theme.BDD, "algorithme de jointure par hachage", "Alternative a la jointure tri-fusion."),
                    new Enigme("Quel est le principe clé du modèle CAP dans les bases de données distribuées ?",
                            Difficulte.MASTER2, Theme.BDD, "consistance, disponibilite, tolerance au partitionnement", "Un systeme ne peut en garantir que deux."),
                    new Enigme("Dans un modèle comptable, quelle est l’équation fondamentale de la comptabilité ?",
                            Difficulte.MASTER2, Theme.COMPTABILITE, "actif = passif + capitaux propres", "C'est la base du bilan comptable.")
            );



            enigmeRepository.saveAll(enigmes);
            System.out.println("15 énigmes insérées avec succès !");
        } else {
            System.out.println("Les énigmes existent déjà, pas d'insertion nécessaire.");
        }
    }
}
