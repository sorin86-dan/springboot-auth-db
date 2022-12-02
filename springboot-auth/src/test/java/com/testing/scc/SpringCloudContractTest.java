package com.testing.scc;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.testing:springboot-db:+:stubs:8090")
public class SpringCloudContractTest {
    @Test
    public void testSetDbMessageIdEmpty() throws Exception {
        URI uri = new URI("http://localhost:8090/set-db-message");
        HttpEntity<String> request = generateRequest("", "{\"message\": \"Baza de date aleasa: \"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Authorization failed", result.getBody());
    }

    @Test
    public void testSetDbMessageIdInvalid() throws Exception {
        URI uri = new URI("http://localhost:8090/set-db-message");
        HttpEntity<String> request = generateRequest("DUMMY", "{\"message\": \"Baza de date aleasa: \"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Authorization failed", result.getBody());
    }

    @Test
    public void testSetDbMessage() throws Exception {
        URI uri = new URI("http://localhost:8090/set-db-message");
        HttpEntity<String> request = generateRequest("OK", "{\"message\": \"Baza de date aleasa: \"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Message updated successfully!", result.getBody());
    }

    @Test
    public void testSetDbMessageInvalidBody() throws Exception {
        URI uri = new URI("http://localhost:8090/set-db-message");
        HttpEntity<String> request = generateRequest("OK", "{\"message\": null}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Missing 'message' field!", result.getBody());
    }

    @Test
    public void testGetDbMessageIdEmpty() throws Exception {
        URI uri = new URI("http://localhost:8090/get-db-message");
        HttpEntity<String> request = generateRequest("", "{\"db\": \"Redis\"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Authorization failed", result.getBody());
    }

    @Test
    public void testGetDbMessageIdInvalid() throws Exception {
        URI uri = new URI("http://localhost:8090/get-db-message");
        HttpEntity<String> request = generateRequest("DUMMY", "{\"db\": \"Redis\"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Authorization failed", result.getBody());
    }

    @Test
    public void testGetDbMessage() throws Exception {
        URI uri = new URI("http://localhost:8090/get-db-message");
        HttpEntity<String> request = generateRequest("OK", "{\"db\": \"Redis\"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Message updated successfully!", result.getBody());
    }

    @Test
    public void testGetDbMessageInvalidBody() throws Exception {
        URI uri = new URI("http://localhost:8090/get-db-message");
        HttpEntity<String> request = generateRequest("OK", "{\"db\": null}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Missing 'db' field!", result.getBody());
    }


    @NotNull
    private HttpEntity<String> generateRequest(String ok, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Type", "application/json");
        headers.set("id", ok);
        return new HttpEntity<>(jsonBody, headers);
    }
}
