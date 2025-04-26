package karate.authentification.crud;

import com.intuit.karate.junit5.Karate;

public class CrudUtilisateurScenarioTest {

    @Karate.Test
    Karate testInscriptionScenario() {
        return Karate.run("crudUtilisateur").relativeTo(getClass()).systemProperty("karate.env", "dev");
    }

}
