package utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TestContext is a shared container used to pass important data (such as the API response,
 * authentication token, and booking ID) between different step definitions within a single test scenario.
 *
 * <p>
 * Although some methods like the constructor and getToken() might show "no usages" in the IDE,
 * they are still essential:
 * </p>
 * <ul>
 *   <li>
 *     The constructor is used by the dependency injection framework (PicoContainer) to create and inject
 *     the TestContext into step definition classes.
 *   </li>
 *   <li>
 *     The getToken() method, even if not directly used in our current tests (since we switched to Basic Auth),
 *     is part of the context and can be useful for future token-based tests or enhancements.
 *   </li>
 * </ul>
 *
 * <p>
 * In simple terms, TestContext acts like a shared notepad where one step can store data that other steps need.
 * </p>
 */
public class TestContext {

    private static final Logger logger = LogManager.getLogger(TestContext.class);

    // Stores the most recent API response.
    private Response response;

    // Stores the authentication token from the API.
    // Even if not used directly now, it is available for future token-based tests.
    private String token;

    // Stores the booking ID created during a test.
    private int bookingId;

    /**
     * Constructor: Creates a new TestContext.
     * Although you might not see direct calls to this constructor, it is used by the DI framework (PicoContainer)
     * to instantiate TestContext automatically.
     */
    public TestContext() {
        logger.debug("TestContext created.");
    }

    /**
     * Returns the current API response.
     *
     * @return the Response object.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Sets the API response.
     *
     * @param response the Response object to store.
     */
    public void setResponse(Response response) {
        this.response = response;
        logger.debug("Response set in TestContext. Status code: {}",
                response != null ? response.getStatusCode() : "null");
    }

    /**
     * Returns the stored authentication token.
     * Although this method might not be directly used in our current tests (we use Basic Auth),
     * it is available for future scenarios that require token-based authentication.
     *
     * @return the token as a String.
     */
    public String getToken() {
        return token;
    }

    /**
     * Stores the authentication token.
     *
     * @param token the token string to store.
     */
    public void setToken(String token) {
        this.token = token;
        logger.debug("Token set in TestContext: {}", token);
    }

    /**
     * Returns the stored booking ID.
     *
     * @return the booking ID.
     */
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Stores the booking ID.
     *
     * @param bookingId the booking ID to store.
     */
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
        logger.debug("BookingId set in TestContext: {}", bookingId);
    }
}
