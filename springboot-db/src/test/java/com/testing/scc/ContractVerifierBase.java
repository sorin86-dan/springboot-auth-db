package com.testing.scc;

import com.testing.controllers.DBController;
import com.testing.services.DBService;
import com.testing.utils.RedisCache;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(classes = {ContractVerifierBase.class, DBController.class, DBService.class})
public class ContractVerifierBase {
    @Autowired
    private DBController dbController;

    @BeforeEach
    public void setup() {
        RedisCache redis = new RedisCache("127.0.0.1", 6379);
        redis.delete("db");
        redis.delete("message");

        RestAssuredMockMvc.standaloneSetup(MockMvcBuilders.standaloneSetup(dbController));
    }
}
