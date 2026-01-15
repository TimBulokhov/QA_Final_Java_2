package order;

import io.restassured.RestAssured;
import model.Order;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import steps.UserClientSteps;

import java.util.ArrayList;
import java.util.List;

import static constants.TestData.BASE_URI;

public class BaseOrderAPITest {
    protected String email;
    protected String password;
    protected String name;
    protected User user;
    protected String accessToken;
    protected List<String> ingredients;
    protected Order order;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;

        email = RandomStringUtils.randomAlphanumeric(5, 10) + "@test.com";
        password = RandomStringUtils.randomAlphanumeric(7, 15);
        name = RandomStringUtils.randomAlphabetic(2, 18);

        // Создаем объект пользователя
        user = new User(email, password, name);

        // Создаем пользователя
        UserClientSteps.createUniqueNewUser(user);

        // Логинимся и получаем токен
        accessToken = UserClientSteps.checkRequestAuthLogin(user)
                .then()
                .extract()
                .path("accessToken");

        ingredients = new ArrayList<>();
        order = new Order(ingredients);
    }
}
