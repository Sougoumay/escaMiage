package karate.authentification.inscription;

import com.intuit.karate.junit5.Karate;

public class InscriptionScenarioTest {

    @Karate.Test
    Karate testInscriptionScenario() {
        return Karate.run("inscription").relativeTo(getClass()).systemProperty("karate.env", "dev");
    }

}
