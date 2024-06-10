package com.rprass.payment.integrationstests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.rprass.payment.configs.TestConfigs;
import com.rprass.payment.integrationstests.testcontainers.AbstractIntegrationTest;
import com.rprass.payment.integrationstests.vo.AccountCredentialsVO;
import com.rprass.payment.integrationstests.vo.TokenVO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerJsonTests extends AbstractIntegrationTest {

    private static TokenVO tokenVO;

    @Test
    @DisplayName("Deve autenticar um usuário com sucesso")
    @Order(1)
    public void testSignin() throws JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro","admin123");
        tokenVO =
                given()
                        .basePath("/auth/signin")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(user)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenVO.class);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());

    }

    @Test
    @DisplayName("Deve atualizar o token de um usuário com sucesso")
    @Order(2)
    public void testRefreshToken() throws JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro","admin123");
        var newTokenVO =
                given()
                        .basePath("/auth/refresh")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .pathParam("username", tokenVO.getUsername())
                        .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " +  tokenVO.getRefreshToken())
                        .when()
                        .put("{username}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenVO.class);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());

    }

}