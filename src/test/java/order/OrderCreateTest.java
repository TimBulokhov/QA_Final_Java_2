package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Ingredient;
import model.Ingredients;

import model.Order;
import model.User;


import org.junit.After;
import org.junit.Test;
import steps.OrderClientSteps;
import steps.UserClientSteps;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.apache.http.HttpStatus.*;
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
        // Проверяем, что получили ингредиенты
        if (ingredientsResponse == null || ingredientsResponse.getData() == null || ingredientsResponse.getData().isEmpty()) {
            throw new AssertionError("Failed to get ingredients from API");
        }
        
        // Очищаем список перед добавлением новых ингредиентов
        ingredients.clear();
        
        // Получаем валидные ID ингредиентов (фильтруем null значения и используем правильные индексы)
        ingredientsResponse.getData().stream()
                .filter(ingredient -> ingredient != null && ingredient.getId() != null && !ingredient.getId().isEmpty())
                .skip(1) // Пропускаем первый элемент
                .limit(7) // Берем максимум 7 ингредиентов
                .forEach(ingredient -> ingredients.add(ingredient.getId()));
        
        // Проверяем, что список не пустой
        if (ingredients.isEmpty()) {
            throw new AssertionError("No valid ingredients found. Total ingredients: " + ingredientsResponse.getData().size());
        }
        
        // Обновляем order с актуальным списком ингредиентов
        order.setIngredients(ingredients);
        
        Response response = OrderClientSteps.createOrderWithAuthorization(order, accessToken);
        response.then().
                log().all()
                .assertThat().statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", any(Integer.class))
                .body("order.ingredients", notNullValue())
                .body("order._id", notNullValue())
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
        // Проверяем, что получили ингредиенты
        if (ingredientsResponse == null || ingredientsResponse.getData() == null || ingredientsResponse.getData().isEmpty()) {
            throw new AssertionError("Failed to get ingredients from API");
        }
        
        // Очищаем список перед добавлением новых ингредиентов
        ingredients.clear();
        
        // Получаем валидные ID ингредиентов (фильтруем null значения и используем правильные индексы)
        ingredientsResponse.getData().stream()
                .filter(ingredient -> ingredient != null && ingredient.getId() != null && !ingredient.getId().isEmpty())
                .skip(1) // Пропускаем первый элемент
                .limit(3) // Берем максимум 3 ингредиента
                .forEach(ingredient -> ingredients.add(ingredient.getId()));
        
        // Проверяем, что список не пустой
        if (ingredients.isEmpty()) {
            throw new AssertionError("No valid ingredients found. Total ingredients: " + ingredientsResponse.getData().size());
        }
        
        // Обновляем order с актуальным списком ингредиентов
        order.setIngredients(ingredients);
        
        Response response = OrderClientSteps.createOrderWithoutAuthorization(order);
        response.then()
                .log().all()
                .assertThat().statusCode(SC_OK)
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
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));

    }
    @Test
    @DisplayName("Создание заказа с авторизацией с неверным хешем ингредиентов")
    @Description("Проверка создания заказа с авторизацией с неверным хешем ингредиентов")
    public void createOrderWithAuthorizationWithWrongHashTest() {
        Ingredients ingredientsResponse = OrderClientSteps.getIngredient();
        // Очищаем список перед добавлением новых ингредиентов
        ingredients.clear();
        
        // Получаем валидные ID и добавляем к ним невалидные строки
        List<String> validIds = ingredientsResponse.getData().stream()
                .filter(ingredient -> ingredient != null && ingredient.getId() != null && !ingredient.getId().isEmpty())
                .skip(1)
                .limit(2)
                .map(Ingredient::getId)
                .collect(java.util.stream.Collectors.toList());
        
        if (validIds.size() < 2) {
            throw new AssertionError("Not enough valid ingredients found. Found: " + validIds.size());
        }
        
        ingredients.add(validIds.get(0) + "khilunjlknjkbyg444333");
        ingredients.add(validIds.get(1) + "7889iuojuigtyfjkuhh");
        
        order.setIngredients(ingredients);
        Response response = OrderClientSteps.createOrderWithAuthorization(order, accessToken);
        response.then().log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
    @Test
    @DisplayName("Создание заказа без авторизации с неверным хешем ингредиентов")
    @Description("Проверка создания заказа без авторизации с неверным хешем ингредиентов")
    public void createOrderWithoutAuthorizationWithWrongHashTest() {
        Ingredients ingredientsResponse = OrderClientSteps.getIngredient();
        // Очищаем список перед добавлением новых ингредиентов
        ingredients.clear();
        
        // Получаем валидные ID и добавляем к ним невалидные строки
        List<String> validIds = ingredientsResponse.getData().stream()
                .filter(ingredient -> ingredient != null && ingredient.getId() != null && !ingredient.getId().isEmpty())
                .skip(1)
                .limit(2)
                .map(Ingredient::getId)
                .collect(java.util.stream.Collectors.toList());
        
        if (validIds.size() < 2) {
            throw new AssertionError("Not enough valid ingredients found. Found: " + validIds.size());
        }
        
        ingredients.add(validIds.get(0) + "khilunjlknjkbyg444333");
        ingredients.add(validIds.get(1) + "7889iuojuigtyfjkuhh");
        
        order.setIngredients(ingredients);
        Response response = OrderClientSteps.createOrderWithoutAuthorization(order);
        response.then().log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
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
                    .statusCode(SC_ACCEPTED);

        }
    }
}

