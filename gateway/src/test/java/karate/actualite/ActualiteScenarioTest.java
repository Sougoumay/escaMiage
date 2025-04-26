package karate.actualite;

import com.intuit.karate.junit5.Karate;

public class ActualiteScenarioTest {

    @Karate.Test
    Karate testPostScenario() {
        return Karate.run("crudPost").relativeTo(getClass()).systemProperty("karate.env", "dev");
    }

    @Karate.Test
    Karate testReactionPostScenario() {
        return Karate.run("reactionUtilisateur").relativeTo(getClass()).systemProperty("karate.env", "dev");
    }

}
