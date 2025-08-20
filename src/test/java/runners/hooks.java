package runners;

import Tests.BaseTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;


public class hooks extends BaseTest {

    @Before
    public void setUp() {
        initializeDriver();
    }

    @After
    public void tearDown() {
        quitDriver();
    }

}
