The code **springboot-auth-db** is a set of 2 connected services, automated end to end tests and contract tests.

To have the application working you need to have a Redis server instance running on **localhost:6379**. For more details about how to start the apps, please check the page about running end to end tests below.

To check how the apps work, start services as mentioned and use the 2 API POST requests:
- **get-message** - if you send a json body with *{"id":"OK", "db":"Redis"}*, you will get a reply: *The chosen database is: Redis*;
- **set-message** 
  - if you send a json body with *{"id":"OK", "message":"Hello, "}*, you will get a reply: *Message updated successfully!*;
  - if you send after this a json body with *{"id":"OK", "db":"Redis"}*, you will get a reply: *Hello, Redis*;

To run automated tests please check the documentation:
1. [Run end to end tests](docs/end-to-end-tests.md)
2. [Run contract tests](docs/contract-tests.md)