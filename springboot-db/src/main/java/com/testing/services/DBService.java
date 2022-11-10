package com.testing.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.utils.DBObject;
import com.testing.utils.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DBService {

    // Using service with Docker containers
//    private RedisCache redis = new RedisCache("172.0.0.4", 6379);

    // Using service locally
    private RedisCache redis = new RedisCache("127.0.0.1", 6379);

    public ResponseEntity retrieveDataFromDb (String body) throws IOException {
        var jsonBody = new ObjectMapper();
        var dbObject = jsonBody.readValue(body, DBObject.class);
        var message = redis.get("message");
        if (StringUtils.isBlank(message)) {
            message = "The chosen database is: ";
        }
        var responseBody = jsonBody.writeValueAsString("Missing 'db' field!").replace("\"", "");

        if(dbObject.getDb() != null) {
            responseBody = jsonBody.writeValueAsString(message + dbObject.getDb()).replace("\"", "");
        }

        return new ResponseEntity(responseBody, HttpStatus.OK);
    }

    public ResponseEntity setDataToDb (String body) throws IOException {
        var jsonBody = new ObjectMapper();
        var dbObject = jsonBody.readValue(body, DBObject.class);
        var responseBody = "Missing 'message' field!";

        if(dbObject.getMessage() != null) {
            redis.put("message", dbObject.getMessage());
            responseBody = "Message updated successfully!";
        }

        return new ResponseEntity(responseBody, HttpStatus.OK);
    }

}
