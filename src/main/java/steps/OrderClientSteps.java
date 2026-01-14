package steps;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Ingredients;
import model.Order;
import static constants.TestData.ORDERS;
import static io.restassured.RestAssured.given;


public class OrderClientSteps {

    @Step("Получение данных об ингредиентах.")
    public static Ingredients getIngredient() {
        return given()
                .header("Content-Type", "application/json")
                .log().all()
                .get("/api/ingredients")
                .body()
                .as(Ingredients.class);
    }

    @Step("Создание заказа с авторизацией")
    public static Response createOrderWithAuthorization(Order order, String token) {
        return given()
                .log().all()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body(order)
                .when()
                .post(ORDERS);
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrderWithoutAuthorization(Order order) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDERS);
    }

    @Step("Создание заказа без ингредиентов")
    public static Response checkFailedResponseApiOrders (Order order, String token) {
        return given()
                .log().all()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body(order)
                .when()
                .post(ORDERS);
    }

}
