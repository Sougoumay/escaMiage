package karate.authentification.connexion;

import com.intuit.karate.junit5.Karate;

public class ConnexionScenarioTest {

    @Karate.Test
    Karate testConnexionScenario() {
        return Karate.run("connexion").relativeTo(getClass()).systemProperty("karate.env", "dev");
    }

}
