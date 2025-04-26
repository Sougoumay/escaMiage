package karate.enigme;

import com.intuit.karate.junit5.Karate;

public class EnigmeScenarioTest {

    @Karate.Test
    Karate testConnexionScenario() {
        return Karate.run("enigme").relativeTo(getClass()).systemProperty("karate.env", "dev");
    }

}
