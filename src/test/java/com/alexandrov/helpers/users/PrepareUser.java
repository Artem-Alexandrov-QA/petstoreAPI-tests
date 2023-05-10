package com.alexandrov.helpers.users;

import com.alexandrov.domain.users.User;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class PrepareUser {
    static Faker faker = new Faker();

    @Step("Подготовка нового юзера.")
    public static User prepareUser() {
        return User
                .builder()
                .username(faker.name().username())
                .firstName(faker.harryPotter().character())
                .lastName(faker.harryPotter().house())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
    }
}