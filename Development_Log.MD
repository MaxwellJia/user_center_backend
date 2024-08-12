# Development Log

## Project Goals
1. User register and login
2. User Management, search and modify users information
3. User Verification

## Technology Selection

Back-end:

java
spring (dependency injection framework, helps you manage Java objects and integrate some other content)
springmvc (web framework, provides interface access, restful interface and other capabilities)
mybatis (Java database operation framework, persistence layer framework, jdbc encapsulation)
mybatis-plus (enhancement of mybatis, can achieve addition, deletion, modification and query without writing sql)
springboot (quick start/quick integration project. No need to manage spring configuration by yourself, no need to integrate various frameworks by yourself)
junit unit test library
mysql database

## Development Tools
WebStorm (front end), IntelliJ IDEA Ult(backend), MySQL and so on

## Common Plugins
MyBatisX, GenerateAllSetter, Auto Filling Java Call Arguments, 


## Development Process

### Initialise Project
Ant Design Pro, Ant Design framework to initialize front end
IDEA Spring Initializr to initialize backend (Generate basic frameworks and dependencies), configure the database, initialise myBaits-plus to modify database later (dependencies and framework)

### Database Design (Mysql Database user)
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

### Automatic Generator - MyBatisX ([User.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fmodel%2Fdomain%2FUser.java), [UserMapper.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fmapper%2FUserMapper.java))
MyBatisX plug-in, automatically generates according to the database:
domain: entity object
mapper: object for operating the database
mapper.xml: defines the association between the mapper object and the database, where you can write SQL yourself
service: contains commonly used additions, deletions, modifications and queries
serviceImpl: specific implementation of the service
thereby improving development efficiency

### Registration Logic Design ([UserService.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fservice%2FUserService.java), [UserServiceImpl.java](src%2Fmain%2Fjava%2Fcom%2Fwangtao%2Fusercenter%2Fservice%2Fimpl%2FUserServiceImpl.java))
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

### Login Function

#### Interface Design
Accepted parameters: user account, password

Request type: POST

Request body: data in JSON format

Return value: user information (masked)

#### Login Logic
1. Check whether the user account and password are legal
2. Not empty
3. The account length is not less than 4 characters
4. The password is not less than 8 characters
5. The account does not contain special characters
6. Check whether the password is entered correctly and compare it with the ciphertext password in the database
7. Desensitize user information, hide sensitive information, and prevent field leakage in the database
8. Record the user's login status (session) and save it on the server (record it using the server tomcat encapsulated by the backend SpringBoot framework)



