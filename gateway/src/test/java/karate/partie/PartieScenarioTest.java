package karate.partie;

import com.intuit.karate.junit5.Karate;

public class PartieScenarioTest {

    @Karate.Test
    Karate testJouerPartieScenario() {
        return Karate.run("jouerPartie").relativeTo(getClass()).systemProperty("karate.env", "dev");
    }

    @Karate.Test
    Karate testConnexionScenario() {
        return Karate.run("feedback").relativeTo(getClass()).systemProperty("karate.env", "dev");
    }

}
