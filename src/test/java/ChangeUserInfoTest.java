import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserSteps;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserInfoTest {

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
    @DisplayName("Success update user name, use /api/auth/user")
    public void updateName() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        UserSteps.takeUserInfo(accessToken);
        User updateInfo = new User(null, null, RandomStringUtils.randomAlphabetic(10));
        UserSteps.updateUserInfo(accessToken, updateInfo)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("user.name", equalTo(updateInfo.getName()));
    }

    @Test
    @DisplayName("Success update user email, use /api/auth/user")
    public void updateEmail() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        UserSteps.takeUserInfo(accessToken);
        User updateInfo = new User(RandomStringUtils.randomAlphabetic(10)+"@mail.ru", null, null);
        UserSteps.updateUserInfo(accessToken, updateInfo)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("user.email", equalTo(updateInfo.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("Success update user password with authentication, use /api/auth/user")
    public void updatePasswordWithAuth() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        UserSteps.takeUserInfo(accessToken);
        User updateInfo = new User(null, RandomStringUtils.randomAlphabetic(10), null);
        UserSteps.updateUserInfo(accessToken, updateInfo)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Fail update user password with authentication, use /api/auth/user")
    public void updatePasswordWithoutAuth() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        User updateInfo = new User(null, RandomStringUtils.randomAlphabetic(10), null);
        UserSteps.updateUserInfoWithoutAuth(updateInfo)
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Fail update user name with authentication, use /api/auth/user")
    public void updateNameWithoutAuth() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        User updateInfo = new User(null, null, RandomStringUtils.randomAlphabetic(10));
        UserSteps.updateUserInfoWithoutAuth(updateInfo)
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Fail update user password with authentication, use /api/auth/user")
    public void updateEmailWithoutAuth() {
        user = new User(email, password, name);
        UserSteps.createUser(user);
        accessToken = UserSteps.signInUser(user).then().extract().path("accessToken").toString().substring(7);
        User updateInfo = new User(RandomStringUtils.randomAlphabetic(10)+"@mail.ru", null, null);
        UserSteps.updateUserInfoWithoutAuth(updateInfo)
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false));
    }
}
