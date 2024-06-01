package util;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiSpecBuilder {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";

    public static RequestSpecification requestSpec() {
        return given()
                .baseUri(BASE_URI);
    }
}
