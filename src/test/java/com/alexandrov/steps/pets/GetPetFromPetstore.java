package com.alexandrov.steps.pets;

import com.alexandrov.pojo.pets.Pet;
import io.qameta.allure.Step;

import static com.alexandrov.specs.PetsSpecs.requestSpec;
import static com.alexandrov.specs.PetsSpecs.petsSuccessResponseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetPetFromPetstore {

    @Step("Запрос на получение питомца. Проверка, что ответ соответствует JSON схеме.")
    public static Pet getPetById(String petId) {

        return given()
                .spec(requestSpec)
                .when()
                .get("pet/" + petId)
                .then()
                .spec(petsSuccessResponseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/pets/get_pet_petstore_schema.json"))
                .statusCode(200)
                .extract().as(Pet.class);
    }
}