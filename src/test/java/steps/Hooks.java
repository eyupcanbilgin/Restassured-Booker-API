package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.TestContext;

/**
 * Hooks with PicoContainer
 */
public class Hooks {

    private static final Logger logger = LogManager.getLogger(Hooks.class);

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp(Scenario scenario) {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        logger.info("=== Starting Scenario: {} ===", scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("=== Scenario FAILED: {} ===", scenario.getName());
        } else {
            logger.info("=== Scenario PASSED: {} ===", scenario.getName());
        }
    }
}
