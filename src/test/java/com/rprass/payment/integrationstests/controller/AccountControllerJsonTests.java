package com.rprass.payment.integrationstests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rprass.payment.configs.TestConfigs;
import com.rprass.payment.integrationstests.testcontainers.AbstractIntegrationTest;
import com.rprass.payment.integrationstests.vo.AccountCredentialsVO;
import com.rprass.payment.integrationstests.vo.TokenVO;
import com.rprass.payment.application.dto.AccountDTO;
import com.rprass.payment.integrationstests.wrapper.WrapperAccountDTO;
import com.rprass.payment.unit.tests.mapper.mocks.MockAccount;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerJsonTests extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static AccountDTO dto;
    static MockAccount input;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        input = new MockAccount();
    }

    @Test
    @DisplayName("Should authenticate a user successfully")
    @Order(0)
    public void authorization() throws JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
        var accessToken = given()
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
                .as(TokenVO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/account/v1/")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @DisplayName("Should create an account successfully")
    @Order(1)
    public void testCreate() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(input.mockDTO(1))
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        AccountDTO persistedDTO = objectMapper.readValue(content, AccountDTO.class);
        dto = persistedDTO;
        Date expectedDueDate = persistedDTO.getDueDate();
        Date expectedPaymentDate = persistedDTO.getPaymentDate();

        assertNotNull(persistedDTO);
        assertNotNull(persistedDTO.getDueDate());
        assertNotNull(persistedDTO.getPaymentDate());
        assertNotNull(persistedDTO.getDescription());
        assertNotNull(persistedDTO.getStatus());
        assertTrue(persistedDTO.getKey() > 0);
        assertEquals("description1", persistedDTO.getDescription());
        assertEquals(new BigDecimal(10), persistedDTO.getValue());
        assertEquals(expectedDueDate.toInstant().truncatedTo(ChronoUnit.SECONDS), persistedDTO.getDueDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(expectedPaymentDate.toInstant().truncatedTo(ChronoUnit.SECONDS), persistedDTO.getPaymentDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("Should verify CORS error when origin is not authorized")
    @Order(2)
    public void testCreateWithWrongOringin() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERROR)
                .body(input.mockDTO(1))
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @DisplayName("Should return an account successfully with a valid ID")
    @Order(3)
    public void testFindById() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_APPLICATION)
                .pathParam("id", dto.getKey())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        AccountDTO foundDTO = objectMapper.readValue(content, AccountDTO.class);

        assertNotNull(foundDTO);
        Date expectedDueDate = foundDTO.getDueDate();
        Date expectedPaymentDate = foundDTO.getPaymentDate();

        assertNotNull(foundDTO);
        assertNotNull(foundDTO.getDueDate());
        assertNotNull(foundDTO.getPaymentDate());
        assertNotNull(foundDTO.getDescription());
        assertNotNull(foundDTO.getStatus());
        assertTrue(foundDTO.getKey() > 0);
        assertEquals("description1", foundDTO.getDescription());
        assertEquals(new BigDecimal("10.00"), foundDTO.getValue());
        assertEquals(expectedDueDate.toInstant().truncatedTo(ChronoUnit.SECONDS), foundDTO.getDueDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(expectedPaymentDate.toInstant().truncatedTo(ChronoUnit.SECONDS), foundDTO.getPaymentDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("Should return an error when trying to find an account with a valid ID but without origin authorization")
    @Order(4)
    public void testFindByIdWrongOrigin() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERROR)
                .pathParam("id", dto.getKey())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @DisplayName("Should update an account successfully")
    @Order(5)
    public void testUpdate() throws JsonProcessingException {
        dto.setDescription("Updated Description");
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(dto)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        AccountDTO updatedDTO = objectMapper.readValue(content, AccountDTO.class);

        assertNotNull(updatedDTO);
        assertEquals("Updated Description", updatedDTO.getDescription());
        assertEquals(dto.getDueDate().toInstant().truncatedTo(ChronoUnit.SECONDS), updatedDTO.getDueDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(dto.getPaymentDate().toInstant().truncatedTo(ChronoUnit.SECONDS), updatedDTO.getPaymentDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("Should delete an account successfully")
    @Order(6)
    public void testDelete() throws JsonProcessingException {
        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", dto.getKey())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Should return all accounts")
    @Order(7)
    public void testFindAll() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_APPLICATION)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        WrapperAccountDTO wrapperAccountDTO = objectMapper.readValue(content, WrapperAccountDTO.class);
        var accounts = wrapperAccountDTO.getEmbedded().getAccounts();

        AccountDTO foudAccountOne = accounts.get(0);
        assertNotNull(accounts);
        assertTrue(accounts.size() > 0);
    }

    @Test
    @DisplayName("Should return an error when trying to find all accounts without origin authorization")
    @Order(8)
    public void testFindAllWrongOrigin() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERROR)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }
}
