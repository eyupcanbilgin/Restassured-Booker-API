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

public class AuthSteps {

    private static final Logger logger = LogManager.getLogger(AuthSteps.class);
    private final TestContext context;
    private Map<String, String> credentials;

    public AuthSteps(TestContext context) {
        this.context = context;
    }

    @Given("I have valid user credentials")
    public void i_have_valid_user_credentials() {
        credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "password123");
        logger.info("User credentials set: {}", credentials);
    }

    @When("I send POST request to create token")
    public void i_send_post_request_to_create_token() {
        try {
            logger.info("POST /auth with credentials...");
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
            logger.error("Error while POST /auth", e);
            throw e;
        }
    }

    @Then("response status code should be {int}")
    public void response_status_code_should_be(int expectedStatusCode) {
        int actualStatusCode = context.getResponse().getStatusCode();
        logger.info("Asserting status code: expected {}, actual {}", expectedStatusCode, actualStatusCode);
        assertThat(actualStatusCode, is(expectedStatusCode));
    }

    @Then("I save the token from response")
    public void i_save_the_token_from_response() {
        String token = context.getResponse().jsonPath().getString("token");
        logger.info("Token extracted: {}", token);
        assertThat("Token should not be empty", token, not(emptyString()));
        context.setToken(token);
    }
}
