package com.alexandrov.tests;

import com.alexandrov.pojo.users.User;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.alexandrov.data.UserBuilder.prepareUser;
import static com.alexandrov.steps.users.CreateUserInPetstore.createUser;
import static com.alexandrov.specs.UsersSpecs.userRequestSpec;
import static com.alexandrov.specs.UsersSpecs.userSuccessResponseSpec;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

public class UsersTests {

    @Test
    @Story("Users tests")
    @Owner("Artem Alexandrov")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("API test Petstore.swagger.io")
    @DisplayName("Создание нового пользователя в системе")
    public void createUserTest() {
        User user = prepareUser();
        user.setId(createUser(user).getId());

        step("Проверка, что id юзера заполнен.");
        assertThat(user.getId()).isNotNull();
    }

    @Test
    @Story("Users tests")
    @Owner("Artem Alexandrov")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("API test Petstore.swagger.io")
    @DisplayName("Получение данных пользователя по username")
    public void getUserByUsernameTest() {
        User user = prepareUser();
        user.setId(createUser(user).getId());

        step("Запрос на получение данных пользователя по username.");
        User userFromApi =
                given()
                        .spec(userRequestSpec)
                        .when()
                        .get("user/" + user.getUsername())
                        .then()
                        .spec(userSuccessResponseSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/users/get_user_by_username_petstore_schema.json"))
                        .extract().response().as(User.class);

        step("Проверка ответа.");
        assertThat(userFromApi).isEqualTo(user);
    }
}