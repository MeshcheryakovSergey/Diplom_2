package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import util.ApiForTest;
import util.ApiSpecBuilder;


public class OrderSteps {

    @Step("Create order")
    public static Response createOrder(String accessToken, Ingredients ingredients) {
        Response response = ApiSpecBuilder.requestSpec()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .and()
                .body(ingredients)
                .when()
                .post(ApiForTest.ORDERS_PATH);
        return response;
    }

    @Step("Get user orders")
    public static Response getUserOrders(String accessToken) {
        Response response = ApiSpecBuilder.requestSpec()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .get(ApiForTest.ORDERS_PATH);
        return response;
    }



}
