package com.testing.controllers;

import com.testing.services.DBService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DBController {

    @Autowired
    DBService dbService;

    @PostMapping(value = "/get-db-message")
    public ResponseEntity getMessage(@RequestHeader("id")String id, @RequestBody String body) throws IOException {
        if(StringUtils.isNotBlank(id) && "OK".equals(id)) {
            return dbService.retrieveDataFromDb(body);
        }
        return new ResponseEntity("Authorization failed", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/set-db-message")
    public ResponseEntity setMessage(@RequestHeader("id")String id, @RequestBody String body) throws IOException {
        if(StringUtils.isNotBlank(id) && "OK".equals(id)) {
            return dbService.setDataToDb(body);
        }
        return new ResponseEntity("Authorization failed", HttpStatus.UNAUTHORIZED);
    }

}
