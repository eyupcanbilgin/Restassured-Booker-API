package steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.TestContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BookingSteps {

    private static final Logger logger = LogManager.getLogger(BookingSteps.class);

    private final TestContext context;
    private Map<String, Object> bookingPayload;
    private Map<String, Object> partialPayload;

    public BookingSteps(TestContext context) {
        this.context = context;
    }

    // ---------------------------
    //  CREATE NEW BOOKING (POST)
    // ---------------------------

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

    @When("^I send POST request to /booking$")
    public void i_send_post_request_to_booking() {
        try {
            logger.info("POST /booking...");

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
            logger.info("Response status: {}", response.statusCode());
        } catch (Exception e) {
            logger.error("Error in POST /booking", e);
            throw e;
        }
    }

    @Then("I save the bookingid from response")
    public void i_save_the_bookingid_from_response() {
        Response response = context.getResponse();
        int bookingId = response.jsonPath().getInt("bookingid");
        logger.info("Extracted bookingId: {}", bookingId);
        assertThat("BookingId should be > 0", bookingId, greaterThan(0));
        context.setBookingId(bookingId);
    }

    // ---------------------------
    //  GET BOOKING
    // ---------------------------

    // Örnek: "I send GET request to the stored booking"
    // ya da istersen parametreli step vs.
    @When("I send GET request to the stored booking")
    public void i_send_get_request_to_the_stored_booking() {
        int id = context.getBookingId();
        logger.info("GET /booking/{}", id);

        try {
            Response response = RestAssured
                    .given()
                    .when()
                    .get("/booking/" + id)
                    .then()
                    .extract()
                    .response();

            context.setResponse(response);
            logger.info("GET /booking/{} => status {}", id, response.statusCode());
        } catch (Exception e) {
            logger.error("Error in GET /booking/" + id, e);
            throw e;
        }
    }

    @Then("response should contain the correct firstname {string}")
    public void response_should_contain_the_correct_firstname(String expectedFirstName) {
        String actualFirstName = context.getResponse().jsonPath().getString("firstname");
        logger.info("Asserting firstname -> exp: {}, act: {}", expectedFirstName, actualFirstName);
        assertThat(actualFirstName, is(expectedFirstName));
    }

    @Then("response should contain the correct lastname {string}")
    public void response_should_contain_the_correct_lastname(String expectedLastName) {
        String actualLastName = context.getResponse().jsonPath().getString("lastname");
        logger.info("Asserting lastname -> exp: {}, act: {}", expectedLastName, actualLastName);
        assertThat(actualLastName, is(expectedLastName));
    }

    // ---------------------------
    //  UPDATE BOOKING (PUT) - Basic Auth
    // ---------------------------

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

    @When("I send PUT request to the stored booking")
    public void i_send_put_request_to_the_stored_booking() {
        try {
            int id = context.getBookingId();
            logger.info("PUT /booking/{} with updated payload...", id);

            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .accept("application/json")
                    // Basic Auth
                    .auth().preemptive().basic("admin", "password123")
                    .body(bookingPayload)
                    .when()
                    .put("/booking/" + id)
                    .then()
                    .extract()
                    .response();

            context.setResponse(response);
            logger.info("PUT /booking/{} status code: {}", id, response.statusCode());
        } catch (Exception e) {
            logger.error("Error in PUT request to the stored booking", e);
            throw e;
        }
    }

    @Then("response should contain the updated firstname {string}")
    public void response_should_contain_the_updated_firstname(String expectedName) {
        String actualFirstName = context.getResponse().jsonPath().getString("firstname");
        logger.info("Asserting updated firstname -> exp: {}, act: {}", expectedName, actualFirstName);
        assertThat(actualFirstName, is(expectedName));
    }

    // ---------------------------
    //  PARTIAL UPDATE BOOKING (PATCH) - Basic Auth
    // ---------------------------

    @Given("I have a partial update booking payload")
    public void i_have_a_partial_update_booking_payload() {
        partialPayload = new HashMap<>();
        partialPayload.put("lastname", "Brown");
        logger.info("Partial payload: {}", partialPayload);
    }

    @When("I send PATCH request to the stored booking")
    public void i_send_patch_request_to_the_stored_booking() {
        try {
            int id = context.getBookingId();
            logger.info("PATCH /booking/{} with partial payload...", id);

            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .accept("application/json")
                    // Basic Auth
                    .auth().preemptive().basic("admin", "password123")
                    .body(partialPayload)
                    .when()
                    .patch("/booking/" + id)
                    .then()
                    .extract()
                    .response();

            context.setResponse(response);
            logger.info("PATCH /booking/{} status: {}", id, response.statusCode());
        } catch (Exception e) {
            logger.error("Error in PATCH request to the stored booking", e);
            throw e;
        }
    }

    @Then("response should contain the updated lastname {string}")
    public void response_should_contain_the_updated_lastname(String expectedLastName) {
        String actualLastName = context.getResponse().jsonPath().getString("lastname");
        logger.info("Asserting updated lastname -> exp: {}, act: {}", expectedLastName, actualLastName);
        assertThat(actualLastName, is(expectedLastName));
    }

    // ---------------------------
    //  DELETE BOOKING - Basic Auth
    // ---------------------------

    @When("I send DELETE request to the stored booking")
    public void i_send_delete_request_to_the_stored_booking() {
        try {
            int id = context.getBookingId();
            logger.info("DELETE /booking/{} with Basic Auth...", id);

            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .accept("application/json")
                    // Basic Auth
                    .auth().preemptive().basic("admin", "password123")
                    .when()
                    .delete("/booking/" + id)
                    .then()
                    .extract()
                    .response();

            context.setResponse(response);
            logger.info("DELETE /booking/{} status code: {}", id, response.statusCode());
        } catch (Exception e) {
            logger.error("Error in DELETE request to the stored booking", e);
            throw e;
        }
    }
}
