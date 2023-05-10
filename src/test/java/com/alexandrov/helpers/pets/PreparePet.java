package com.alexandrov.helpers.pets;

import com.alexandrov.domain.pets.Pet;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;

public class PreparePet {
    static Faker faker = new Faker();

    @Step("Подготовка нового питомца.")
    public static Pet preparePet() {
        return Pet
                .builder()
                .name(faker.superhero().name())
                .photoUrls(new ArrayList<>(List.of(faker.internet().url())))
                .build();
    }
}