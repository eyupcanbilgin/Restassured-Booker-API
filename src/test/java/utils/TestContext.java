package utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TestContext, senaryo bazında adımlar arasında ortak verilerin (response, token, bookingId, vb.) paylaşılmasını sağlar.
 */
public class TestContext {

    private static final Logger logger = LogManager.getLogger(TestContext.class);

    private Response response;
    private String token;
    private int bookingId;

    public TestContext() {
        logger.debug("TestContext oluşturuldu.");
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
        logger.debug("Response TestContext'e ayarlandı. StatusCode: {}",
                response != null ? response.getStatusCode() : "null");
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        logger.debug("Token TestContext'e ayarlandı: {}", token);
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
        logger.debug("BookingId TestContext'e ayarlandı: {}", bookingId);
    }
}
