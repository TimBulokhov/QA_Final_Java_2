package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.User;

import static constants.TestData.*;
import static io.restassured.RestAssured.given;

public class UserClientSteps {

    @Step("Создание уникальнго пользователя")
    public static Response createUniqueNewUser(User user) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(REGISTER)
                .then()
                .extract().response();
    }

    @Step("Логин под существующим пользователем")
    public static Response checkRequestAuthLogin(User user) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(LOGIN)
                .then()
                .extract().response();
    }
    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken){
        return given()
                .header("Authorization",accessToken)
                .when()
                .delete(DELETE);
    }
}
