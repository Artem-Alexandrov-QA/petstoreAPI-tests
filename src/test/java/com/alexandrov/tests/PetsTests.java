package com.alexandrov.tests;

import com.alexandrov.domain.pets.Pet;
import com.alexandrov.helpers.pets.GetPetFromPetstore;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.alexandrov.helpers.pets.AddPetToPetstore.addPet;
import static com.alexandrov.helpers.pets.PreparePet.preparePet;
import static com.alexandrov.specs.pets.PetsSpecs.requestSpec;
import static com.alexandrov.specs.pets.PetsSpecs.responseSpec;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

public class PetsTests {

    @Test
    @Story("Pets tests")
    @Owner("Artem Alexandrov")
    @Severity(SeverityLevel.NORMAL)
    @Feature("API test Petstore.swagger.io")
    @DisplayName("Добавление нового питомца")
    public void addPetTest() {
        Pet pet = preparePet();
        Pet newPetInStore = addPet(pet);

        step("Проверка, что в ответе корректные данные. Имя: " + newPetInStore.getName() + " == SomePetName");
        assertThat(newPetInStore.getName()).isEqualTo(pet.getName());
        step("Проверка, что в ответе корректные данные. URL фото.");
        assertThat(newPetInStore.getPhotoUrls()).isEqualTo(pet.getPhotoUrls());
    }

    @Test
    @Story("Pets tests")
    @Owner("Artem Alexandrov")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Получение питомца по id")
    @Feature("API test Petstore.swagger.io")
    public void getPetByIdTest() {
        Pet pet = preparePet();
        Pet newPetInStore = addPet(pet);

        Pet petFromPetstore = GetPetFromPetstore.getPetById(String.valueOf(newPetInStore.getId()));

        step("Проверка, что в ответе корректные данные. Имя: " + petFromPetstore.getName() + " == SomePetName");
        assertThat(petFromPetstore.getName()).isEqualTo(pet.getName());
        step("Проверка, что в ответе корректные данные. URL фото." + petFromPetstore.getName() + " == SomePetName");
        assertThat(petFromPetstore.getPhotoUrls()).isEqualTo(pet.getPhotoUrls());
    }

    @Test
    @Story("Pets tests")
    @Owner("Artem Alexandrov")
    @Severity(SeverityLevel.NORMAL)
    @Feature("API test Petstore.swagger.io")
    @DisplayName("Обновление информации о питомце")
    public void updatePetTest() {
        Pet pet = preparePet();
        Pet newPetInStore = addPet(pet);

        step("Обновление данных питомца.");
        newPetInStore.setName("NewPetName");
        newPetInStore.setPhotoUrls(new ArrayList<>(List.of("none")));

        step("Отправляю запрос на обновление данных питомца.");
        Response response =
                given()
                        .spec(requestSpec)
                        .body(newPetInStore)
                        .when()
                        .put("pet")
                        .then()
                        .spec(responseSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/pets/post_pet_petstore_schema.json"))
                        .extract().response();

        step("Проверка ответа: " + response.statusCode());
        step("Проверка, что ответ соответствует JSON схеме.");

        step("Проверка, что в ответе корректные данные. Имя: " + response.as(Pet.class).getName() + " == SomePetName");
        assertThat(response.as(Pet.class).getName()).isEqualTo("NewPetName");
        step("Проверка, что в ответе корректные данные. URL фото.");
        assertThat(response.as(Pet.class).getPhotoUrls()).contains("none");
    }

    @Test
    @Story("Pets tests")
    @Owner("Artem Alexandrov")
    @Severity(SeverityLevel.NORMAL)
    @Feature("API test Petstore.swagger.io")
    @DisplayName("Удаление питомца из магазина")
    public void deletePetTest() {
        Pet pet = preparePet();
        Pet newPetInStore = addPet(pet);

        step("Отправить запрос на удаление.");
        Response response =
                given()
                        .spec(requestSpec)
                        .body(newPetInStore)
                        .when()
                        .delete("pet/" + newPetInStore.getId())
                        .then()
                        .spec(responseSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/pets/delete_pet_petstore_schema.json"))
                        .extract().response();

        step("Проверка ответа: " + response.statusCode());
        step("Проверка, что ответ соответствует JSON схеме.");

        step("Проверка, что в ответе корректные данные.");
        assert response.body().path("code").equals(200);
        assert response.body().path("type").equals("unknown");
        assert response.body().path("message").equals(String.valueOf(newPetInStore.getId()));
    }
}