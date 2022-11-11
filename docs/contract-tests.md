**[Back](../README.md)**

# Running PACT tests

Running PACT tests locally involves 2 steps:
1. Run consumer PACT tests in *springboot-auth* and copy the json file from *resources/pact* folder to same folder in *springboot-db*
2. Run provider PACT tests with Redis server and DBMainApp running (from *springboot-db* folder).

Running PACT tests with PactBroker involves 4 steps:
1. Uncomment PACT plugin in *pom.xml* file from *springboot-auth* folder.
2. Run consumer PACT tests in *springboot-auth* and run **mvn pact:publish** from *springboot-auth*
3. Uncomment commented PACT references in *PactProviderTest* from *springboot-db* and comment annotation **@PactFolder** from there
4. Run provider PACT tests with Redis server and DBMainApp running (from *springboot-db* folder).

# Running Spring Cloud Contract tests

Running Sprin Cloud Contract tests involves 2 steps:
1. Run **mvn clean install -DskipTests**
2. Run Spring Cloud Contract tests from *springboot-auth*