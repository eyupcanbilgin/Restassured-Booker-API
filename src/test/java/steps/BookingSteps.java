package steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.TestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * BookingSteps contains step definitions for testing all booking-related API calls.
 *
 * Each endpoint (POST, GET, PUT, PATCH, DELETE, and the /ping health check) is tested in its own step.
 * If an endpoint requires data from a previous call (e.g., bookingId), the scenario creates a booking
 * and saves the ID into the TestContext.
 *
 * Basic Authentication (admin/password123) is used for update, partial update, and delete operations.
 */
public class BookingSteps {

    private static final Logger logger = LogManager.getLogger(BookingSteps.class);

    // TestContext is used to share data (like response, token, bookingId) between steps in a scenario.
    private final TestContext context;
    // Payload for creating or updating a booking.
    private Map<String, Object> bookingPayload;
    // Payload for partial updates.
    private Map<String, Object> partialPayload;

    /**
     * Constructor: TestContext is injected by PicoContainer, which allows sharing data across steps.
     *
     * @param context the shared TestContext for the scenario.
     */
    public BookingSteps(TestContext context) {
        this.context = context;
    }

    // ---------------------------
    // CREATE NEW BOOKING (POST)
    // ---------------------------

    /**
     * Prepares a new booking payload with sample data.
     * This step sets up the necessary information to create a booking.
     */
    @Given("I have a new booking payload")
    public void i_have_a_new_booking_payload() {
        bookingPayload = new HashMap<>();
        bookingPayload.put("firstname", "Eyup");
        bookingPayload.put("lastname", "Can");
        bookingPayload.put("totalprice", 123);
        bookingPayload.put("depositpaid", true);

        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2025-05-01");
        bookingDates.put("checkout", "2025-05-10");
        bookingPayload.put("bookingdates", bookingDates);

        bookingPayload.put("additionalneeds", "Breakfast");

        logger.info("New booking payload: {}", bookingPayload);
    }

    /**
     * Sends a POST request to /booking with the new booking payload.
     * The response is stored in TestContext.
     */
    @When("^I send POST request to /booking$")
    public void i_send_post_request_to_booking() {
        try {
            logger.info("Sending POST request to /booking...");
            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(bookingPayload)
                    .when()
                    .post("/booking")
                    .then()
                    .extract()
                    .response();
            context.setResponse(response);
            logger.info("POST /booking response status: {}", response.getStatusCode());
        } catch (Exception e) {
            logger.error("Error while sending POST request to /booking", e);
            throw e;
        }
    }

    /**
     * Extracts the bookingId from the response and saves it in TestContext.
     * This bookingId is used by subsequent GET, PUT, PATCH, and DELETE calls.
     */
    @Then("I save the bookingid from response")
    public void i_save_the_bookingid_from_response() {
        Response response = context.getResponse();
        int bookingId = response.jsonPath().getInt("bookingid");
        logger.info("Extracted bookingId: {}", bookingId);
        assertThat("BookingId should be greater than 0", bookingId, greaterThan(0));
        context.setBookingId(bookingId);
    }

    // ---------------------------
    // GET BOOKING (Single)
    // ---------------------------

    /**
     * Sends a GET request to retrieve the stored booking using its bookingId.
     * The bookingId is fetched from TestContext.
     */
    @When("I send GET request to the stored booking")
    public void i_send_get_request_to_the_stored_booking() {
        int id = context.getBookingId();
        logger.info("Sending GET request to /booking/{}", id);
        try {
            Response response = RestAssured
                    .given()
                    .when()
                    .get("/booking/" + id)
                    .then()
                    .extract()
                    .response();
            context.setResponse(response);
            logger.info("GET /booking/{} returned status: {}", id, response.getStatusCode());
        } catch (Exception e) {
            logger.error("Error while sending GET request to /booking/" + id, e);
            throw e;
        }
    }

    /**
     * Asserts that the response contains the expected firstname.
     *
     * @param expectedFirstName the expected first name.
     */
    @Then("response should contain the correct firstname {string}")
    public void response_should_contain_the_correct_firstname(String expectedFirstName) {
        String actualFirstName = context.getResponse().jsonPath().getString("firstname");
        logger.info("Asserting firstname -> expected: {}, actual: {}", expectedFirstName, actualFirstName);
        assertThat(actualFirstName, is(expectedFirstName));
    }

    /**
     * Asserts that the response contains the expected lastname.
     *
     * @param expectedLastName the expected last name.
     */
    @Then("response should contain the correct lastname {string}")
    public void response_should_contain_the_correct_lastname(String expectedLastName) {
        String actualLastName = context.getResponse().jsonPath().getString("lastname");
        logger.info("Asserting lastname -> expected: {}, actual: {}", expectedLastName, actualLastName);
        assertThat(actualLastName, is(expectedLastName));
    }

    // ---------------------------
    // GET BOOKING IDs (List)
    // ---------------------------

    /**
     * Sends a GET request to /booking to retrieve a list of booking IDs.
     */
    @When("^I send GET request to /booking$")
    public void i_send_get_request_to_all_bookings() {
        try {
            logger.info("Sending GET request to /booking for all booking IDs...");
            Response response = RestAssured
                    .given()
                    .when()
                    .get("/booking")
                    .then()
                    .extract()
                    .response();
            context.setResponse(response);
            logger.info("GET /booking returned status: {}", response.getStatusCode());
        } catch (Exception e) {
            logger.error("Error while sending GET request to /booking (all IDs)", e);
            throw e;
        }
    }

    /**
     * Asserts that the response from GET /booking contains a non-empty list of booking IDs.
     */
    @Then("the response should contain booking IDs")
    public void the_response_should_contain_booking_ids() {
        Response response = context.getResponse();
        List<Map<String, Integer>> bookingIds = response.jsonPath().getList("");
        logger.info("Booking IDs found: {}", bookingIds);
        assertThat("Booking IDs list should not be empty", bookingIds, is(not(empty())));
    }

    // ---------------------------
    // PING (Health Check)
    // ---------------------------

    /**
     * Sends a GET request to /ping to perform a health check on the API.
     */
    @When("^I send GET request to /ping$")
    public void i_send_get_request_to_ping() {
        try {
            logger.info("Sending GET request to /ping...");
            Response response = RestAssured
                    .given()
                    .when()
                    .get("/ping")
                    .then()
                    .extract()
                    .response();
            context.setResponse(response);
            logger.info("GET /ping returned status: {}", response.getStatusCode());
        } catch (Exception e) {
            logger.error("Error while sending GET request to /ping", e);
            throw e;
        }
    }

    // ---------------------------
    // UPDATE BOOKING (PUT) - Basic Auth
    // ---------------------------

    /**
     * Prepares an updated booking payload with new values.
     * This payload is used to completely update the booking details.
     */
    @Given("I have an updated booking payload")
    public void i_have_an_updated_booking_payload() {
        bookingPayload = new HashMap<>();
        bookingPayload.put("firstname", "Ali");
        bookingPayload.put("lastname", "Can");
        bookingPayload.put("totalprice", 999);
        bookingPayload.put("depositpaid", false);

        Map<String, String> dates = new HashMap<>();
        dates.put("checkin", "2025-06-01");
        dates.put("checkout", "2025-06-10");
        bookingPayload.put("bookingdates", dates);

        bookingPayload.put("additionalneeds", "None");
        logger.info("Updated booking payload: {}", bookingPayload);
    }

    /**
     * Sends a PUT request to update the stored booking using Basic Authentication.
     */
    @When("I send PUT request to the stored booking")
    public void i_send_put_request_to_the_stored_booking() {
        try {
            int id = context.getBookingId();
            logger.info("Sending PUT request to /booking/{} with updated payload...", id);
            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .accept("application/json")
                    // Use Basic Auth for update operations.
                    .auth().preemptive().basic("admin", "password123")
                    .body(bookingPayload)
                    .when()
                    .put("/booking/" + id)
                    .then()
                    .extract()
                    .response();
            context.setResponse(response);
            logger.info("PUT /booking/{} returned status: {}", id, response.getStatusCode());
        } catch (Exception e) {
            logger.error("Error in PUT request to the stored booking", e);
            throw e;
        }
    }

    /**
     * Asserts that the updated booking's firstname matches the expected value.
     *
     * @param expectedName the expected first name after update.
     */
    @Then("response should contain the updated firstname {string}")
    public void response_should_contain_the_updated_firstname(String expectedName) {
        String actualFirstName = context.getResponse().jsonPath().getString("firstname");
        logger.info("Asserting updated firstname -> expected: {}, actual: {}", expectedName, actualFirstName);
        assertThat(actualFirstName, is(expectedName));
    }

    // ---------------------------
    // PARTIAL UPDATE BOOKING (PATCH) - Basic Auth
    // ---------------------------

    /**
     * Prepares a payload for a partial update, in this case updating only the lastname.
     */
    @Given("I have a partial update booking payload")
    public void i_have_a_partial_update_booking_payload() {
        partialPayload = new HashMap<>();
        partialPayload.put("lastname", "Brown");
        logger.info("Partial update payload: {}", partialPayload);
    }

    /**
     * Sends a PATCH request to partially update the stored booking using Basic Authentication.
     */
    @When("I send PATCH request to the stored booking")
    public void i_send_patch_request_to_the_stored_booking() {
        try {
            int id = context.getBookingId();
            logger.info("Sending PATCH request to /booking/{} with partial update payload...", id);
            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .accept("application/json")
                    // Use Basic Auth for partial update operations.
                    .auth().preemptive().basic("admin", "password123")
                    .body(partialPayload)
                    .when()
                    .patch("/booking/" + id)
                    .then()
                    .extract()
                    .response();
            context.setResponse(response);
            logger.info("PATCH /booking/{} returned status: {}", id, response.getStatusCode());
        } catch (Exception e) {
            logger.error("Error in PATCH request to the stored booking", e);
            throw e;
        }
    }

    /**
     * Asserts that the booking's lastname has been updated to the expected value after the partial update.
     *
     * @param expectedLastName the expected last name.
     */
    @Then("response should contain the updated lastname {string}")
    public void response_should_contain_the_updated_lastname(String expectedLastName) {
        String actualLastName = context.getResponse().jsonPath().getString("lastname");
        logger.info("Asserting updated lastname -> expected: {}, actual: {}", expectedLastName, actualLastName);
        assertThat(actualLastName, is(expectedLastName));
    }

    // ---------------------------
    // DELETE BOOKING - Basic Auth
    // ---------------------------

    /**
     * Sends a DELETE request to remove the stored booking using Basic Authentication.
     */
    @When("I send DELETE request to the stored booking")
    public void i_send_delete_request_to_the_stored_booking() {
        try {
            int id = context.getBookingId();
            logger.info("Sending DELETE request to /booking/{} with Basic Auth...", id);
            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .accept("application/json")
                    // Use Basic Auth for delete operations.
                    .auth().preemptive().basic("admin", "password123")
                    .when()
                    .delete("/booking/" + id)
                    .then()
                    .extract()
                    .response();
            context.setResponse(response);
            logger.info("DELETE /booking/{} returned status: {}", id, response.getStatusCode());
        } catch (Exception e) {
            logger.error("Error in DELETE request to the stored booking", e);
            throw e;
        }
    }
}
