package karate.recompense.badge;

import com.intuit.karate.junit5.Karate;

public class BadgeScenarioTest {

    @Karate.Test
    Karate testBadgeScenario() {
        return Karate.run("badge").relativeTo(getClass()).systemProperty("karate.env", "dev");
    }

}
