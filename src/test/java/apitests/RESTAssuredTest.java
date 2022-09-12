package apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import models.CreateUserModel;
import models.LoginModel;
import models.RegisterModel;
import models.UpdateUserModel;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.io.File;

import static org.hamcrest.Matchers.*;

public class RESTAssuredTest {
    @Test
    public void checkListOfUsersTest() {
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users")
                .then()
                .statusCode(200)
                .body("total", equalTo(12));
    }

    @Test
    public void checkSingleUserTest() {
        JsonPath expectedJson = new JsonPath(new File("src/test/resources/user.json"));
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("", equalTo(expectedJson.getMap("")));
    }

    @Test
    public void checkUserNotFoundTest() {
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    public void checkBodyValuesTest() {
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .body("page", instanceOf(Integer.class))
                .body("per_page", equalTo(6));
    }

    @Test
    public void checkStaticResponseTest() {
        JsonPath expectedJson = new JsonPath(new File("src/test/resources/user.json"));
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/unknown/2")
                .then()
                .statusCode(200)
                .body("", equalTo(expectedJson.getMap("")));
    }

    @Test
    public void checkDynamicResponseBody() {
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/unknown/2")
                .then()
                .statusCode(200)
                .body("data.name", equalTo("fuchsia rose"));
    }

    @Test
    public void getWithQueryParamsTest() {
        RestAssured
                .given()
                .queryParam("page", "2")
                .when()
                .get("https://reqres.in/api/users")
                .then()
                .statusCode(200)
                .body("page", equalTo(2));
    }

    @Test
    public void singleResourceNotFoundTest() {
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/unknown/23")
                .then()
                .statusCode(404);
    }

    @Test
    public void createUserTest() {
        CreateUserModel createUserBody = CreateUserModel
                .builder()
                .job("CEO")
                .name("Ben")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(createUserBody)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201);
    }

    @Test
    public void updateUserTest() {
        UpdateUserModel updateBody = UpdateUserModel
                .builder()
                .name("Maria")
                .job("QA")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(updateBody)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200);
    }

    @Test
    public void partialUserUpdateTest() {
        UpdateUserModel updateBody = UpdateUserModel
                .builder()
                .job("AQA")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(updateBody)
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("job", equalTo("AQA"));
    }

    @Test
    public void deleteUserTest() {
        RestAssured
                .given()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    public void registerSuccessfullyTest() {
        RegisterModel registerModel = RegisterModel
                .builder()
                .email("miaumiau123@gmail.com")
                .password("dogs")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(registerModel)
                .when()
                .post("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test
    public void registerUnsuccessfullyTest() {
        RegisterModel registerModel = RegisterModel
                .builder()
                .email("miaumiau@gmail.com")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(registerModel)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void loginSuccessfullyTest() {
        LoginModel loginRequestBody = LoginModel
                .builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(loginRequestBody)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    public void loginUnsuccessfullyTest() {
        LoginModel loginRequestBody = LoginModel
                .builder()
                .email("peter@klaven")
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .and()
                .body(loginRequestBody)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void delayedResponseTest() {
        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users?delay=3")
                .then()
                .time(Matchers.lessThan(4000L))
                .statusCode(200)
                .body("total", equalTo(12));
    }
}
