package com.testing;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DefaultTest.class)
public class DefaultTest {

    @Value("${auth.ip.address:localhost}")
    private String authIpAddress;
    @Value("${db.ip.address:localhost}")
    private String dbIpAddress;

    @BeforeEach
    public void setUp() {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"OK\", \"message\":\"The chosen database is: \"}")
                .post("http://"+ authIpAddress + ":8081/set-message");
    }

    @Test
    public void checkMessageEndpoint() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"OK\", \"db\":\"Redis\"}")
                .post("http://"+ authIpAddress + ":8081/get-message");

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("The chosen database is: Redis"));
    }

    @Test
    public void checkMessageEndpointInvalidBody() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"OK\"}")
                .post("http://"+ authIpAddress + ":8081/get-message");

        assertEquals(400, response.getStatusCode());
        assertEquals("Missing 'db' field!", response.getBody().asString());
    }

    @Test
    public void checkMessageEndpointMissingAuth() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"db\":\"Redis\"}")
                .post("http://"+ authIpAddress + ":8081/get-message");

        assertEquals(401, response.getStatusCode());
        assertEquals("Authorization failed", response.getBody().asString());
    }

    @Test
    public void checkMessageEndpointWrongAuth() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"NOK\", \"db\":\"Redis\"}")
                .post("http://"+ authIpAddress + ":8081/get-message");

        assertEquals(401, response.getStatusCode());
        assertEquals("Authorization failed", response.getBody().asString());
    }

    @Test
    public void checkMessageEndpointMissingHeader() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"db\":\"Redis\"}")
                .post("http://"+ dbIpAddress + ":8082/get-db-message");

        assertEquals(400, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().jsonPath().get("error"));
    }

    @Test
    public void checkMessageEndpointWrongHeader() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("id", "NOK")
                .body("{\"db\":\"Redis\"}")
                .post("http://"+ dbIpAddress + ":8082/get-db-message");

        assertEquals(401, response.getStatusCode());
        assertEquals("Authorization failed", response.getBody().asString());
    }

    @Test
    public void setMessageEndpoint() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"OK\", \"message\":\"Baza de date aleasa este: \"}")
                .post("http://"+ authIpAddress + ":8081/set-message");

        assertEquals(200, response.getStatusCode());
        assertEquals("Message updated successfully!", response.getBody().asString());

        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"OK\", \"db\":\"Redis\"}")
                .post("http://"+ authIpAddress + ":8081/get-message");

        assertEquals(200, response.getStatusCode());
        assertEquals("Baza de date aleasa este: Redis", response.getBody().asString());
    }

    @Test
    public void setMessageEndpointInvalidBody() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"OK\"}")
                .post("http://"+ authIpAddress + ":8081/set-message");

        assertEquals(400, response.getStatusCode());
        assertEquals("Missing 'message' field!", response.getBody().asString());
    }

    @Test
    public void setMessageEndpointMissingAuth() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"message\":\"Baza de date aleasa este: \"}")
                .post("http://"+ authIpAddress + ":8081/set-message");

        assertEquals(401, response.getStatusCode());
        assertEquals("Authorization failed", response.getBody().asString());
    }

    @Test
    public void setMessageEndpointWrongAuth() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"NOK\", \"message\":\"Baza de date aleasa este: \"}")
                .post("http://"+ authIpAddress + ":8081/set-message");

        assertEquals(401, response.getStatusCode());
        assertEquals("Authorization failed", response.getBody().asString());
    }

    @Test
    public void setMessageEndpointMissingHeader() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"message\":\"Baza de date aleasa este: \"}")
                .post("http://"+ dbIpAddress + ":8082/set-db-message");

        assertEquals(400, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().jsonPath().get("error"));
    }

    @Test
    public void setMessageEndpointWrongHeader() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("id", "NOK")
                .body("{\"message\":\"Baza de date aleasa este: \"}")
                .post("http://"+ dbIpAddress + ":8082/set-db-message");

        assertEquals(401, response.getStatusCode());
        assertEquals("Authorization failed", response.getBody().asString());
    }
}