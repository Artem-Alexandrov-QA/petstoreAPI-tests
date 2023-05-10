package com.alexandrov.helpers.pets;

import com.alexandrov.domain.pets.Pet;
import io.qameta.allure.Step;

import static com.alexandrov.specs.pets.PetsSpecs.requestSpec;
import static com.alexandrov.specs.pets.PetsSpecs.responseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AddPetToPetstore {
    @Step("Запрос на добавление питомца. Проверка, что ответ соответстует схеме.")
    public static Pet addPet(Pet pet) {

        return given()
                .spec(requestSpec)
                .body(pet)
                .when()
                .post("pet")
                .then()
                .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/pets/post_pet_petstore_schema.json"))
                .statusCode(200)
                .extract().as(Pet.class);
    }
}