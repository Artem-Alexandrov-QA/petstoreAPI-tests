package com.alexandrov.steps.pets;

import com.alexandrov.pojo.pets.Pet;
import io.qameta.allure.Step;

import static com.alexandrov.specs.PetsSpecs.requestSpec;
import static com.alexandrov.specs.PetsSpecs.petsSuccessResponseSpec;
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
                .spec(petsSuccessResponseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/pets/post_pet_petstore_schema.json"))
                .statusCode(200)
                .extract().as(Pet.class);
    }
}