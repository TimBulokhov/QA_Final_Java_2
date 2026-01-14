package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserClientSteps;

import static constants.TestData.WRONGLOGIN;
import static constants.TestData.WRONGPASSWORD;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static steps.UserClientSteps.checkRequestAuthLogin;
import static steps.UserClientSteps.createUniqueNewUser;

public class UserLoginTest extends BaseAPITest {

    @Before
    public void checkCreateUserTest() {
        User user = new User(email, password, name);
        createUniqueNewUser(user)
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", notNullValue())
                .body("user.name", notNullValue());
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Проверяем, что пользователь может авторизоваться с набором валидных данных")

    public void loginUserSuccess() {

        User user = new User(email, password);
        checkRequestAuthLogin(user)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", notNullValue())
                .body("user.name", notNullValue());

    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Проверяем, что появляется ошибка при использовании неверного login")

    public void LoginUserWithInvalidLogin() {

        User user = new User(WRONGLOGIN, password);
        checkRequestAuthLogin(user)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Проверяем, что появляется ошибка при использовании неверного password")

    public void LoginUserWithInvalidPassword() {

        User user = new User(email, WRONGPASSWORD);
        checkRequestAuthLogin(user)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином и паролем")
    @Description("Проверяем, что появляется ошибка при использовании неверного email, password")

    public void LoginUserWithInvalidLoginPassword() {

        User user = new User(WRONGLOGIN, WRONGPASSWORD);
        checkRequestAuthLogin(user)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

    }

    @After
    public void tearDown() {
        // Удаление созданного пользователя
        User user = new User(email, password, name);
        String accessToken = checkRequestAuthLogin(user).then().extract().path("accessToken");

        if (accessToken != null) {
            UserClientSteps userClientSteps = new UserClientSteps();
            userClientSteps.deleteUser(accessToken)
                    .then()
                    .log().all()
                    .statusCode(202);

        }
    }
}

