package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import util.Api;
import util.ApiSpecBuilder;

public class IngredientsSteps {

    @Step("Take ingredients information")
    public static Response takeIngredientsInfo (String accessToken) {
        Response response = ApiSpecBuilder.requestSpec().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .get(Api.INGREDIENTS_PATH);
        return response;
    }
}
