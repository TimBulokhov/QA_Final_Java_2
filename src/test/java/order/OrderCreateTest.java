package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Ingredients;

import model.Order;
import model.User;


import org.junit.After;
import org.junit.Test;
import steps.OrderClientSteps;
import steps.UserClientSteps;

import java.util.ArrayList;
import java.util.Locale;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static steps.UserClientSteps.checkRequestAuthLogin;

public class OrderCreateTest extends BaseOrderAPITest {


    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Успешное создание заказа с авторизацией")
    public void createOrderWithAuthorizationTest() {

        Ingredients ingredientsResponse = OrderClientSteps.getIngredient();
        ingredients.add(ingredientsResponse.getData().get(1).getId());
        ingredients.add(ingredientsResponse.getData().get(2).getId());
        ingredients.add(ingredientsResponse.getData().get(3).getId());
        ingredients.add(ingredientsResponse.getData().get(4).getId());
        ingredients.add(ingredientsResponse.getData().get(5).getId());
        ingredients.add(ingredientsResponse.getData().get(7).getId());
        ingredients.add(ingredientsResponse.getData().get(8).getId());
        Response response = OrderClientSteps.createOrderWithAuthorization(order, accessToken);
        response.then().
                log().all()
                .assertThat().statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", any(Integer.class))
                .body("order.ingredients", notNullValue())
                .body("order.id", notNullValue())
                .body("order.owner.name", equalTo(name))
                .body("order.owner.email", equalTo(email.toLowerCase(Locale.ROOT)))
                .body("order.status", equalTo("done"))
                .body("order.name", notNullValue())
                .body("order.price", notNullValue());
    }


    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Успешное создание заказа без авторизации")
    public void createOrderWithoutAuthorizationTest() {
        Ingredients ingredientsResponse = OrderClientSteps.getIngredient();
        ingredients.add(ingredientsResponse.getData().get(1).getId());
        ingredients.add(ingredientsResponse.getData().get(2).getId());
        ingredients.add(ingredientsResponse.getData().get(3).getId());
        Response response = OrderClientSteps.createOrderWithoutAuthorization(order);
        response.then()
                .log().all()
                .assertThat().statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", any(Integer.class));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка создания заказа без ингредиентов")
    public void createEmptyOrderWithoutAuthorization() {
        // Создаем пустой заказ
        Order emptyOrder = new Order(new ArrayList<>());

        OrderClientSteps.checkFailedResponseApiOrders(emptyOrder,accessToken)
                .then()
                .log().all()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));

    }
    @Test
    @DisplayName("Создание заказа с авторизацией с неверным хешем ингредиентов")
    @Description("Проверка создания заказа с авторизацией с неверным хешем ингредиентов")
    public void createOrderWithAuthorizationWithWrongHashTest() {
        Ingredients ingredientsResponse = OrderClientSteps.getIngredient();
        ingredients.add(ingredientsResponse.getData().get(1).getId() + "khilunjlknjkbyg444333");
        ingredients.add(ingredientsResponse.getData().get(2).getId() + "7889iuojuigtyfjkuhh");
        Response response = OrderClientSteps.createOrderWithAuthorization(order, accessToken);
        response.then().log().all()
                .statusCode(500);
    }
    @Test
    @DisplayName("Создание заказа без авторизации с неверным хешем ингредиентов")
    @Description("Проверка создания заказа без авторизации с неверным хешем ингредиентов")
    public void createOrderWithoutAuthorizationWithWrongHashTest() {
        Ingredients ingredientsResponse = OrderClientSteps.getIngredient();
        ingredients.add(ingredientsResponse.getData().get(1).getId() + "khilunjlknjkbyg444333");
        ingredients.add(ingredientsResponse.getData().get(2).getId() + "7889iuojuigtyfjkuhh");
        Response response = OrderClientSteps.createOrderWithoutAuthorization(order);
        response.then().log().all()
                .statusCode(500);
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

