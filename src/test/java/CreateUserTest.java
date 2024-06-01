import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserSteps;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {

    private static String email;
    private static String password;
    private static String name;

    User user;
    String token;
    String accessToken;

    @Before
    public void setUp() {
        email = RandomStringUtils.randomAlphabetic(6)+"@gmail.com";
        password = RandomStringUtils.randomAlphabetic(5);
        name = RandomStringUtils.randomAlphabetic(5);
    }

    @After
    public void tearDown() {
        UserSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Success create user, use /api/auth/register")
    public void createUser() {
        user = new User(email, password, name);
        Response response = UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create duplicate user, use /api/auth/register")
    public void twoSimilarUser() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        UserSteps.createUser(user)
                .then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create user without email, use /api/auth/register")
    public void userWithoutLogin() {
        user = new User(null, password, name);
        UserSteps.createUser(user)
                .then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user without password, use /api/auth/register")
    public void userWithoutPassword() {
        user = new User(email, null, name);
        UserSteps.createUser(user)
                .then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user without name, use /api/auth/register")
    public void userWithoutName() {
        user = new User(email, password, null);
        UserSteps.createUser(user)
                .then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}

