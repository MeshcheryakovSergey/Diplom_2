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

public class GetOrderTest {

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
    @DisplayName("Get user orders with authentication, use /api/orders")
    public void getOrdersWithAuth() {
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
        OrderSteps.getUserOrders(accessToken).then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("total", notNullValue());
    }

    @Test
    @DisplayName("Get user orders without authentication, use /api/orders")
    public void getOrdersWithoutAuth() {
        OrderSteps.getUserOrders("").then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
