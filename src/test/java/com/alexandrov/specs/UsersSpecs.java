package com.alexandrov.specs;

import com.alexandrov.config.CredentialsConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.aeonbits.owner.ConfigFactory;

public class UsersSpecs {

    static CredentialsConfig config = ConfigFactory.create(CredentialsConfig.class);

    public static RequestSpecification userRequestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(config.host())
            .build();

    public static ResponseSpecification userSuccessResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(LogDetail.ALL)
            .build();
}