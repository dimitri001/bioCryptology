# Spring REST
# This project was made following an example from mkyong
Article link : https://www.mkyong.com/spring-boot/spring-rest-hello-world-example/

# Explanation of the problem that this code solves

This code creates a CRUD app to manage USERS.

- So you can know if the user is activated.
- The user that isn't whitin five min will be removed from the DB.
- This solution must apply DDD and all design patterns that fit
- Users can't be repeated

## 1. How to start
```
$ cd spring-rest-test

$ mvn clean install
$ mvn spring-boot:run

$ curl -v localhost:8080/users
```

## 2. Explanation of the code
The app uses the db h2.

The unique entity that it has is User with the followinf fields:

 Long id;
 String name;
 String email;
 Integer age;
 String sex;
 Boolean activated;
 
UserControllerRestTemplateTest is a class that test the code

## 3. Improvement of the code
This code can be improved by :
- adding a service layer
- adding oauth2 to secure it


