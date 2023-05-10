package com.alexandrov.helpers.pets;

import com.alexandrov.domain.pets.Pet;
import io.qameta.allure.Step;

import static com.alexandrov.specs.pets.PetsSpecs.requestSpec;
import static com.alexandrov.specs.pets.PetsSpecs.responseSpec;
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
                .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/pets/get_pet_petstore_schema.json"))
                .statusCode(200)
                .extract().as(Pet.class);
    }
}