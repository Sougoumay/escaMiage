package karate.recompense.classement;

import com.intuit.karate.junit5.Karate;

public class ClassementScenarioTest {

    @Karate.Test
    Karate testConnexionScenario() {
        return Karate.run("classement").relativeTo(getClass()).systemProperty("karate.env", "dev");
    }

}
