package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.TestContext;

/**
 * The Hooks class is responsible for setting up and tearing down each test scenario.
 *
 * Before every scenario, it sets the base URI for the API so that all HTTP requests
 * are sent to the correct endpoint. After each scenario, it logs whether the scenario
 * passed or failed.
 *
 * This class uses PicoContainer for dependency injection, meaning that the shared TestContext
 * (which holds data like API responses, tokens, and booking IDs) is automatically provided.
 *
 * In simple terms, think of Hooks as the pre- and post-conditions for each test: it prepares
 * the environment before a test runs and cleans up (or logs results) afterward.
 */
public class Hooks {

    private static final Logger logger = LogManager.getLogger(Hooks.class);
    private final TestContext context;

    /**
     * Constructs a new Hooks instance.
     * Note: Even if you don't see this constructor being called directly,
     * it is invoked by the dependency injection framework (PicoContainer).
     *
     * @param context the shared TestContext for the scenario.
     */
    public Hooks(TestContext context) {
        this.context = context;
    }

    /**
     * This method is executed before each test scenario.
     * It sets the API's base URI so that subsequent requests are directed to the correct server.
     *
     * @param scenario the current scenario being executed.
     */
    @Before
    public void setUp(Scenario scenario) {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        logger.info("=== Starting Scenario: {} ===", scenario.getName());
    }

    /**
     * This method is executed after each test scenario.
     * It logs the outcome of the scenario—whether it passed or failed.
     *
     * @param scenario the current scenario that has just finished.
     */
    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("=== Scenario FAILED: {} ===", scenario.getName());
        } else {
            logger.info("=== Scenario PASSED: {} ===", scenario.getName());
        }
    }
}
