package com.testing;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DefaultTest {

    private String authIpAddress = "172.0.0.2";
    private String dbIpAddress = "172.0.0.3";
//    private String authIpAddress = "localhost";
//    private String dbIpAddress = "localhost";

    @BeforeTest
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

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().asString().contains("The chosen database is: Redis"));
    }

    @Test
    public void checkMessageEndpointMissingAuth() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"db\":\"Redis\"}")
                .post("http://"+ authIpAddress + ":8081/get-message");

        assertEquals(response.getStatusCode(), 401);
        assertTrue(response.getBody().asString().contains("Authorization failed"));
    }

    @Test
    public void checkMessageEndpointWrongAuth() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"NOK\", \"db\":\"Redis\"}")
                .post("http://"+ authIpAddress + ":8081/get-message");

        assertEquals(response.getStatusCode(), 401);
        assertEquals(response.getBody().asString(), "Authorization failed");
    }

    @Test
    public void checkMessageEndpointMissingHeader() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"db\":\"Redis\"}")
                .post("http://"+ dbIpAddress + ":8082/get-db-message");

        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.getBody().jsonPath().get("error"), "Bad Request");
    }

    @Test
    public void checkMessageEndpointWrongHeader() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("id", "NOK")
                .body("{\"db\":\"Redis\"}")
                .post("http://"+ dbIpAddress + ":8082/get-db-message");

        assertEquals(response.getStatusCode(), 401);
        assertEquals(response.getBody().asString(), "Authorization failed");
    }

    @Test
    public void setMessageEndpoint() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"OK\", \"message\":\"Baza de date aleasa este: \"}")
                .post("http://"+ authIpAddress + ":8081/set-message");

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().asString().contains("Message updated successfully!"));

        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"OK\", \"db\":\"Redis\"}")
                .post("http://"+ authIpAddress + ":8081/get-message");

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().asString().contains("Baza de date aleasa este: Redis"));
    }

}