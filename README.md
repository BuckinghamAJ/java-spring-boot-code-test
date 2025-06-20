# Problem Statement
We have a bug in our app.

The `api/tasks?userId=1` is returning all tasks for all users rather than just the user with id 1 - this is a security issue.

Here is an example of what should be returned for `userId=1`:
```
[
  {
  	"id": 101,
  	"userId": 1,
  	"title": "Buy groceries",
  	"status": "TODO"
  }
]
```

# Important to know
The following was discovered as part of building this project:

* The original package name 'com.example.spring-java-method-challenge' is invalid and this project uses 'com.example.spring_java_method_challenge' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.0/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.0/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.0/reference/web/servlet.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

### Running the Server

To start the application server, use the following Maven command:

```
mvn spring-boot:run
```

This will compile the project (if necessary) and launch the Spring Boot application. By default, the server will be available at [http://localhost:8080](http://localhost:8080).

### Running Unit Tests

To execute the unit tests for the project, run:

```
mvn test
```

This will compile and run all tests in the project, displaying the results in the console.
