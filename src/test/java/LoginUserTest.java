import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserSteps;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {

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
    @DisplayName("Success login in, use /api/auth/login")
    public void signInUser() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        UserSteps.signInUser(user)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        fillIdForDel(user);
    }

    @Test
    @DisplayName("Sign in without login")
    public void signInWithoutLogin() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        User incorrectUser = new User(null, user.getPassword(), null);
        UserSteps.signInUser(incorrectUser)
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
        fillIdForDel(user);
    }

    @Test
    @DisplayName("Sign in without password")
    public void signInWithoutPassword() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        User incorrectUser = new User(user.getEmail(), null, null);
        UserSteps.signInUser(incorrectUser)
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
        fillIdForDel(user);
    }

    @Test
    @DisplayName("Sign in nonexistent user")
    public void signInNonExistent() {
        user = new User(email, password, name);
        UserSteps.signInUser(user)
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Sign in with wrong password")
    public void signInWithWrongPassword() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        User incorrectUser = new User(user.getEmail(), RandomStringUtils.randomAlphabetic(10), null);
        UserSteps.signInUser(incorrectUser)
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
        fillIdForDel(user);
    }

    @Test
    @DisplayName("Sign in with wrong login")
    public void signInWithWrongLogin
            () {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        User incorrectUser = new User(RandomStringUtils.randomAlphabetic(10), user.getPassword(), null);
        UserSteps.signInUser(incorrectUser)
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
        fillIdForDel(user);
    }


    private void fillIdForDel(User user) {
        Response response = UserSteps.signInUser(user);
        accessToken = response.then().extract().path("accessToken").toString().substring(7);
    }

}
