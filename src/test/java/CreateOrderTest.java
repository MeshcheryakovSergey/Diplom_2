import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Ingredients;
import order.IngredientsSteps;
import order.OrderSteps;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserSteps;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {

    private static String email;
    private static String password;
    private static String name;

    User user;
    String accessToken;

    @Before
    public void setUp() {
        email = RandomStringUtils.randomAlphabetic(6) + "@gmail.com";
        password = RandomStringUtils.randomAlphabetic(5);
        name = RandomStringUtils.randomAlphabetic(5);
    }

    @After
    public void tearDown() {
        UserSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Success create order with authentication and ingredients, use /api/orders")
    public void createOrderAuthPlusIngredients() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        Response responseGetIngredients = IngredientsSteps.takeIngredientsInfo(accessToken);
        ArrayList<String> ingredients = responseGetIngredients
                .then()
                .extract()
                .path("data._id");
        Ingredients ingredientsForOrder = new Ingredients(ingredients);
        Response createOrder = OrderSteps.createOrder(accessToken, ingredientsForOrder);
        createOrder.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Fail create order with authentication and without ingredients, use /api/orders")
    public void createOrderAuthPlusNoIngredients() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        ArrayList<String> ingredients = new ArrayList<>();
        Ingredients ingredientsForOrder = new Ingredients(ingredients);
        Response createOrder = OrderSteps.createOrder(accessToken, ingredientsForOrder);
        createOrder.then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Success create order without authentication and with ingredients, use /api/orders")
    public void createOrderNoAuthPlusIngredients() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        Response responseGetIngredients = IngredientsSteps.takeIngredientsInfo(accessToken);
        ArrayList<String> ingredients = responseGetIngredients
                .then()
                .extract()
                .path("data._id");
        Ingredients ingredientsForOrder = new Ingredients(ingredients);
        Response createOrder = OrderSteps.createOrder("", ingredientsForOrder);
        createOrder.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Fail create order without authentication and without ingredients, use /api/orders")
    public void createOrderNoAuthPlusNoIngredients() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        ArrayList<String> ingredients = new ArrayList<>();
        Ingredients ingredientsForOrder = new Ingredients(ingredients);
        Response createOrder = OrderSteps.createOrder("", ingredientsForOrder);
        createOrder.then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Fail create order with authentication and wrong hash ingredients, use /api/orders")
    public void createOrderAuthPlusBrokeIngredients() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        Response responseGetIngredients = IngredientsSteps.takeIngredientsInfo(accessToken);
        ArrayList<String> ingredients = responseGetIngredients
                .then()
                .extract()
                .path("data._id");
        String hashWrong = ingredients.set(0,"wrong");
        Ingredients ingredientsForOrder = new Ingredients(ingredients);
        Response createOrder = OrderSteps.createOrder("", ingredientsForOrder);
        createOrder.then()
                .assertThat()
                .statusCode(500);
    }
}
