package com.alexandrov.tests;

import com.alexandrov.pojo.pets.Pet;
import com.alexandrov.steps.pets.GetPetFromPetstore;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.alexandrov.data.PetBuilder.preparePet;
import static com.alexandrov.steps.pets.AddPetToPetstore.addPet;
import static com.alexandrov.specs.PetsSpecs.requestSpec;
import static com.alexandrov.specs.PetsSpecs.petsSuccessResponseSpec;
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

        step("Проверка, что в ответе корректные данные. Имя: " + newPetInStore.getName());
        assertThat(newPetInStore.getName()).isEqualTo(pet.getName());
        assertThat(newPetInStore.getPhotoUrls()).isEqualTo(pet.getPhotoUrls());
    }

    @Test
    @Story("Pets tests")
    @Owner("Artem Alexandrov")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Получение данных питомца по id")
    @Feature("API test Petstore.swagger.io")
    public void getPetByIdTest() {
        Pet pet = preparePet();
        Pet newPetInStore = addPet(pet);

        Pet petFromPetstore = GetPetFromPetstore.getPetById(String.valueOf(newPetInStore.getId()));

        step("Проверка, что в ответе корректные данные. Имя: " + petFromPetstore.getName());
        assertThat(petFromPetstore.getName()).isEqualTo(pet.getName());
        assertThat(petFromPetstore.getPhotoUrls()).isEqualTo(pet.getPhotoUrls());
    }

    @Test
    @Story("Pets tests")
    @Owner("Artem Alexandrov")
    @Severity(SeverityLevel.NORMAL)
    @Feature("API test Petstore.swagger.io")
    @DisplayName("Обновление данных о питомце")
    public void updatePetTest() {
        Pet pet = preparePet();
        Pet newPetInStore = addPet(pet);

        step("Обновление данных питомца.");
        newPetInStore.setName("NewPetName");
        newPetInStore.setPhotoUrls(new ArrayList<>(List.of("none")));

        Response response =
                given()
                        .spec(requestSpec)
                        .body(newPetInStore)
                        .when()
                        .put("pet")
                        .then()
                        .spec(petsSuccessResponseSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/pets/post_pet_petstore_schema.json"))
                        .statusCode(200)
                        .extract().response();

        step("Проверка, что данные питомца изменились", () -> {
            assertThat(response.as(Pet.class).getName()).isEqualTo("NewPetName");
            assertThat(response.as(Pet.class).getPhotoUrls()).contains("none");
        });
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
                        .spec(petsSuccessResponseSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/pets/delete_pet_petstore_schema.json"))
                        .extract().response();

        step("Проверка, что в ответе корректные данные(code,type,message).");
        assert response.body().path("code").equals(200);
        assert response.body().path("type").equals("unknown");
        assert response.body().path("message").equals(String.valueOf(newPetInStore.getId()));
    }
}