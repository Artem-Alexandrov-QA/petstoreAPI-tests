package com.alexandrov.helpers.users;

import com.alexandrov.domain.users.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.alexandrov.specs.users.UsersSpecs.userRequestSpec;
import static com.alexandrov.specs.users.UsersSpecs.userResponseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CreateUserInPetstore {

    @Step("Запрос на создание юзера. Проверка, что ответ соответствует JSON схеме.")
    public static User createUser(User user) {

        Response response =
                given()
                        .spec(userRequestSpec)
                        .body(user)
                        .when()
                        .post("user")
                        .then()
                        .spec(userResponseSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/users/post_user_petstore_schema.json"))
                        .statusCode(200)
                        .extract().response();
        user.setId(Long.parseLong(response.body().path("message")));

        return user;
    }
}