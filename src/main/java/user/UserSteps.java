package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import util.ApiForTest;
import util.ApiSpecBuilder;

import java.util.Objects;

public class UserSteps {

    @Step("User create")
    public static Response createUser (User user) {
        Response response = ApiSpecBuilder.requestSpec()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(ApiForTest.REG_PATH);
        return response;
    }

    @Step("Sing in user")
    public static Response signInUser (User user) {
        Response response = ApiSpecBuilder.requestSpec().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(ApiForTest.LOGIN_PATH);
        return response;
    }

    @Step("Log out user")
    public static Response signOutUser (String token) {
        Response response = ApiSpecBuilder.requestSpec().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(token)
                .when()
                .post(ApiForTest.LOGOUT_PATH);
        return response;
    }

    @Step("Take user information")
    public static Response takeUserInfo (String token) {
        Response response = ApiSpecBuilder.requestSpec().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(token)
                .when()
                .get(ApiForTest.USER_PATH);
        return response;
    }

    @Step("Update user information")
    public static Response updateUserInfo (String accessToken, User user) {
            Response response = ApiSpecBuilder.requestSpec()
                    .auth().oauth2(accessToken)
                    .header("Content-type", "application/json")
                    .and()
                    .body(user)
                    .when()
                    .patch(ApiForTest.USER_PATH);
            return response;
    }

    @Step("Update user information without authentication")
    public static Response updateUserInfoWithoutAuth (User user) {
        Response response = ApiSpecBuilder.requestSpec()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(ApiForTest.USER_PATH);
        return response;
    }

    @Step("Delete user")
    public static void deleteUser (String accessToken) {
        if (Objects.nonNull(accessToken)) {
            ApiSpecBuilder.requestSpec()
                    .auth().oauth2(accessToken)
                    .delete(ApiForTest.USER_PATH);
        }
    }

}
