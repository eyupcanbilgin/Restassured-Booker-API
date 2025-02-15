package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * This class is the main test runner for our API tests.
 *
 * When you run this class, it does the following:
 *
 * - It tells JUnit to use Cucumber to run the tests.
 * - It specifies that all our feature files are located in "src/test/resources/features".
 * - It indicates that step definitions (the code that ties Gherkin steps to Java methods)
 *   are located in the "steps" and "utils" packages.
 * - It sets the output to "pretty", meaning the test results will be printed in a readable format.
 * - It uses PicoContainer for dependency injection so that shared objects (like our TestContext)
 *   are automatically provided to our step classes.
 *
 * In plain language, this class acts like a launchpad for our tests.
 * It doesn't contain any test logic itself; it just tells the framework where to find the tests
 * and how to run them.
 *
 * You can explain in an interview: "This class integrates Cucumber with JUnit.
 * It is configured via annotations to locate our feature files and step definitions, and it uses
 * PicoContainer for dependency injection to cleanly share data across steps."
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps", "utils"},
        plugin = {"pretty"},
        monochrome = true,
        objectFactory = io.cucumber.picocontainer.PicoFactory.class
)
public class RunCucumberTest {
    // No additional code is needed here since the annotations handle all configurations.
}
