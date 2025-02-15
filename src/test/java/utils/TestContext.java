package utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Holds data shared across steps in a single scenario.
 */
public class TestContext {

    private static final Logger logger = LogManager.getLogger(TestContext.class);

    private Response response;
    private String token;
    private int bookingId;

    public TestContext() {
        logger.debug("TestContext created");
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
        logger.debug("Response is set in TestContext. Status code: {}",
                response != null ? response.getStatusCode() : "null");
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        logger.debug("Token set in context: {}", token);
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
        logger.debug("BookingId set in context: {}", bookingId);
    }
}
