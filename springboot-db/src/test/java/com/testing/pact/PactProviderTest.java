package com.testing.pact;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.testing.utils.RedisCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("SpringBootDB")
@Consumer("SpringBootAuth")
@PactFolder("src/test/resources/pact")
//@PactBroker(url = "http://localhost:9292/")
public class PactProviderTest {

    @BeforeEach
    void setUp(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", 8082));
//        System.setProperty("pact.verifier.publishResults", "true");
    }

    @AfterEach
    void clean() {
        RedisCache redis = new RedisCache("127.0.0.1", 6379);
        redis.delete("db");
        redis.delete("message");
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("a request to set data from DB with invalid id")
    public void setDbDataInvalidIdState() {}

    @State("a request to set data from DB with empty id")
    public void setDBDataEmptyIdState() {}

    @State("a valid request to set data from DB")
    public void setDBDataState() {}

    @State("a request to set data from DB with invalid body")
    public void setDbDataInvalidBodyState() {}

    @State("a request to get data from DB with invalid id")
    public void getDbDataInvalidIdState() {}

    @State("a request to get data from DB with empty id")
    public void getDBDataEmptyIdState() {}

    @State("a valid request to get data from DB")
    public void getDBDataState() {}

    @State("a request to get data from DB with invalid body")
    public void getDbDataInvalidBodyState() {}
}