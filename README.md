To run this project:
1. Start 3 instances of say-hello service by using commands:
    a. mvn clean spring-boot:run
    b. export SERVER_PORT=9092
       mvn clean spring-boot:run
    c. export SERVER_PORT=9999
       mvn clean spring-boot:run
2. Start 1 instance of user service by using command:
    a. mvn clean spring-boot:run
3. open a new terminal and use any of the following commands:
    a. curl http://localhost:8888/hi
    b. curl http://localhost:8888/hi?name=<NAME>
    c. curl http://localhost:8888/hello
    d. curl http://localhost:8888/hello?name=<NAME>