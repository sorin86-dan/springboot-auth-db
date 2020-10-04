package com.testing;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@SpringBootTest
public class DefaultTest {

    @Test
    public void checkMessageEndpoint() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"OK\", \"db\":\"Redis\"}")
                .post("http://localhost:8081/auth-message");

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().asString().contains("The chosen database is: Redis"));
    }

    @Test
    public void checkMessageEndpointMissingHeader() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"db\":\"Redis\"}")
                .post("http://localhost:8081/auth-message");

        assertEquals(response.getStatusCode(), 401);
        assertTrue(response.getBody().asString().contains("Authorization failed"));
    }

    @Test
    public void checkMessageEndpointWrongHeader() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"NOK\", \"db\":\"Redis\"}")
                .post("http://localhost:8081/auth-message");

        assertEquals(response.getStatusCode(), 401);
        assertEquals(response.getBody().asString(), "Authorization failed");
    }

}