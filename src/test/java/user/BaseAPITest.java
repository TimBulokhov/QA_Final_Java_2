package user;

import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import steps.UserClientSteps;

import static constants.TestData.BASE_URI;

public class BaseAPITest {

    protected String email;
    protected String password;
    protected String name;
    

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;

        email = RandomStringUtils.randomAlphanumeric(5, 10) + "@test.com";
        password = RandomStringUtils.randomAlphanumeric(7, 15);
        name = RandomStringUtils.randomAlphabetic(2, 18);

        UserClientSteps userClientSteps = new UserClientSteps();
    }
}
