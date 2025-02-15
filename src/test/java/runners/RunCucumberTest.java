package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Cucumber runner with PicoContainer DI.
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
}
