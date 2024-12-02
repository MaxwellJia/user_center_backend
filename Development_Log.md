# Development Log

## Project Goals
Achieve a web user-center including functions:

1. User register and login
2. User Management, search and modify users information
3. User Verification

## Project Logic
Backend:

Basic database server(Mysql) -> Database controller(Mybatis, Mybatis-plus) -> service and service implement -> controller -> Continuous optimization and updates

## Technology Selection

Backend:

1. spring (dependency injection framework, helps you manage Java objects and integrate some other content)
2. springmvc (web framework, provides interface access, restful interface and other capabilities)
3. mybatis (Java database operation framework, persistence layer framework, jdbc encapsulation)
4. mybatis-plus (enhancement of mybatis, can achieve addition, deletion, modification and query without writing sql)
5. springboot (quick start/quick integration project. No need to manage spring configuration by yourself, no need to integrate various frameworks by yourself)
6. junit unit test library
7. mysql database
8. java (language)

## Development Tools
WebStorm (front end), IntelliJ IDEA Ult (backend), MySQL and so on

## Common Plugins
MyBatisX, GenerateAllSetter, Auto Filling Java Call Arguments, 

## Initialise Project
1. Ant Design Pro, Ant Design framework to initialize front end
2. IDEA Spring Initializr to initialize backend (Generate basic frameworks and dependencies), configure the database, initialise myBaits-plus to modify database later (dependencies and framework)

## Database Design (Mysql Database user)
id (primary key) bigint

username nickname varchar

userAccount login account

avatarUrl avatar varchar

gender gender tinyint

userPassword password varchar

phone varchar

email mailbox varchar

userStatus user status int 0 - normal

createTime creation time (data insertion time) datetime

updateTime update time (data update time) datetime

isDelete whether to delete 0 1 (logical deletion) tinyint

userRole user role 0 - ordinary user 1 - administrator

## Automatic Generator - MyBatisX ([User.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fmodel%2Fdomain%2FUser.java), [UserMapper.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fmapper%2FUserMapper.java))
MyBatisX plug-in, automatically generates according to the database:

domain: entity object

mapper: object for operating the database

mapper.xml: defines the association between the mapper object and the database, where you can write SQL yourself

service: contains commonly used additions, deletions, modifications and queries

serviceImpl: specific implementation of the service

thereby improving development efficiency

## Registration Logic Design ([UserService.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fservice%2FUserService.java), [UserServiceImpl.java](https://github.com/MaxwellJia/user-center/blob/78f35aeffe78d4d1998c7102d0637be9360b0ce5/src/main/java/com/wangtao/usercenter/service/impl/UserServiceImpl.java#L92))
1. The user enters the account and password, as well as the verification code (todo) on the front end
2. Verify the user's account, password, and verification password to see if they meet the requirements
   i. Not empty

   ii. The account length is not less than 4 characters

   iii. The password should be not less than 8 characters

   iv. The account cannot be repeated

   v. The account does not contain special characters

   vi. The password and verification password are the same

3. Encrypt the password (the password must not be stored directly in the database in plain text)
4. Insert user data into the database

## Login Function ([UserService.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fservice%2FUserService.java), [UserServiceImpl.java](https://github.com/MaxwellJia/user-center/blob/78f35aeffe78d4d1998c7102d0637be9360b0ce5/src/main/java/com/wangtao/usercenter/service/impl/UserServiceImpl.java#L92))

### Interface Design
Accepted parameters: user account, password

Request type: POST @PostMapping

Request body: data in JSON format

Return value: user information (masked)

### Login Logic ([UserService.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fservice%2FUserService.java), [UserServiceImpl.java](https://github.com/MaxwellJia/user-center/blob/78f35aeffe78d4d1998c7102d0637be9360b0ce5/src/main/java/com/wangtao/usercenter/service/impl/UserServiceImpl.java#L92))
1. Check whether the user account and password are legal
2. Not empty
3. The account length is not less than 4 characters
4. The password is not less than 8 characters
5. The account does not contain special characters
6. Check whether the password is entered correctly and compare it with the ciphertext password in the database
7. Desensitize user information, hide sensitive information, and prevent field leakage in the database
8. Record the user's login status (session) and save it on the server (record it using the server tomcat encapsulated by the backend SpringBoot framework)

### Implement ####
Controller encapsulates requests

Some request entities are generated used to be the input request for methods in controller (such as: [UserLoginRequest.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fmodel%2Fdomain%2Frequest%2FUserLoginRequest.java), [UserRegisterRequest.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fmodel%2Fdomain%2Frequest%2FUserRegisterRequest.java))

Some mappings are created to help users visit certain methods, such as: @RestController, @RequestMapping

application.yml specifies the global path prefix of the interface:

servlet:

context-path: /api
Controller annotation:

@RestController is suitable for writing restful-style APIs, and the return value defaults to json type
Where should the validation be written?

The controller layer tends to validate the request parameters themselves, and does not involve the business logic itself (the less the better)

The service layer validates the business logic (it may be called by classes other than the controller)

## User Management [UserController.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fcontroller%2FUserController.java)

### Main Functions (Utilise Mybatis-plus inner methods and achieve them in controller level directly)
Identify user role or authority (Not anyone is able to search users or delete users)

1. Search users 
2. Delete users

## Front-end and back-end interaction
The front end interacts with the back end through ajax, axios encapsulates ajax and request is a repackage of the ant design project.

## Proxy
Forward proxy: Sends requests to the server on behalf of the client, which can solve cross-domain problems

Reverse proxy: receives requests on behalf of the server.

How to implement proxy?

Nginx server and Node.js server

