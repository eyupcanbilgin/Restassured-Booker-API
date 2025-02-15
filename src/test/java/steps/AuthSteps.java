package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.TestContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * AuthSteps handles the authentication-related API calls.
 *
 * This class is responsible for sending a POST request to the /auth endpoint to generate an authentication token.
 * It uses a shared TestContext to store the API response and the token, which can then be used by other step definitions.
 *
 * Although our project later uses Basic Auth for other operations, this class demonstrates how to create a token
 * using the provided credentials.
 */
public class AuthSteps {

    private static final Logger logger = LogManager.getLogger(AuthSteps.class);

    // Shared context for passing data (like responses and tokens) between steps
    private final TestContext context;

    // Map to hold user credentials (username & password)
    private Map<String, String> credentials;

    /**
     * Constructor: TestContext is injected using PicoContainer,
     * allowing us to share data between different step definitions.
     *
     * @param context the shared TestContext.
     */
    public AuthSteps(TestContext context) {
        this.context = context;
    }

    /**
     * Sets up valid user credentials.
     * Here we define the username and password that will be used to generate the authentication token.
     */
    @Given("I have valid user credentials")
    public void i_have_valid_user_credentials() {
        credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "password123");
        logger.info("User credentials set: {}", credentials);
    }

    /**
     * Sends a POST request to the /auth endpoint using the provided credentials.
     * The response (which includes the token) is stored in the TestContext.
     */
    @When("I send POST request to create token")
    public void i_send_post_request_to_create_token() {
        try {
            logger.info("Sending POST request to /auth with credentials...");
            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .body(credentials)
                    .when()
                    .post("/auth")
                    .then()
                    .extract()
                    .response();
            context.setResponse(response);
            logger.info("Response status code: {}", response.getStatusCode());
        } catch (Exception e) {
            logger.error("Error while sending POST request to /auth", e);
            throw e;
        }
    }

    /**
     * Asserts that the API response status code matches the expected value.
     *
     * @param expectedStatusCode the expected HTTP status code.
     */
    @Then("response status code should be {int}")
    public void response_status_code_should_be(int expectedStatusCode) {
        int actualStatusCode = context.getResponse().getStatusCode();
        logger.info("Asserting that status code is {}. Actual status code: {}", expectedStatusCode, actualStatusCode);
        assertThat(actualStatusCode, is(expectedStatusCode));
    }

    /**
     * Extracts the authentication token from the API response and stores it in the TestContext.
     * This token can later be used by other tests if token-based authentication is required.
     */
    @Then("I save the token from response")
    public void i_save_the_token_from_response() {
        String token = context.getResponse().jsonPath().getString("token");
        logger.info("Token extracted: {}", token);
        assertThat("Token should not be empty", token, not(emptyString()));
        context.setToken(token);
    }
}
