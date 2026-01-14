package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.After;
import org.junit.Test;
import steps.UserClientSteps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static steps.UserClientSteps.checkRequestAuthLogin;
import static steps.UserClientSteps.createUniqueNewUser;

public class UserCreateTest extends BaseAPITest {


    @Test
    @DisplayName("Проверка создания уникального пользователя")
    @Description("Регистрация уникального пользователя c корректными данными")
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
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    @Description("Регистрация уже зарегистрированного пользователя")
    public void checkRegisteredUserTest() {

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

        createUniqueNewUser(user)
                .then()
                .log().all()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка появления ошибки при создании пользователя без имени")
    public void checkCreateUserWithoutNameTest() {

        User user = new User(email, password, null);
        createUniqueNewUser(user)
                .then()
                .log().all()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка появления ошибки при создании пользователя без почты")
    public void checkCreateUserWithoutEmailTest() {

        User user = new User(null, password, name);
        createUniqueNewUser(user)
                .then()
                .log().all()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка появления ошибки при создании пользователя без пароля")
    public void checkCreateUserWithoutPasswordTest() {

        User user = new User(email, null, name);
        createUniqueNewUser(user)
                .then()
                .log().all()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без полей")
    @Description("Проверка появления ошибки при создании пользователя без заполненных полей")
    public void checkCreateUserWithoutAllFieldsTest() {

        User user = new User(null, null, null);
        createUniqueNewUser(user)
                .then()
                .log().all()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));

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

