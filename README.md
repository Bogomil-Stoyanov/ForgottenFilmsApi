# Forgotten Films REST API Documentation

A Ktor Rest API server for watching selected films from the Public Domain
Tech stack:
- Language: Kotlin
- Framework: Ktor (https://ktor.io/)
- Database: MongoDb

## Features

- Register a user with email, password, nickname and favourite film genres
- Login with email and password
- Get recommended films to watch depending on your favourite genres
- Search, watch, like, dislike and share films
- Watch time statistics

## Libraries

Forgotten Films uses the most modern libraries for backend development with Ktor

- [Ktor] - Framework to build asynchronous server applications (https://github.com/ktorio/ktor)
- [KMongo] - Kotlin toolkit for Mongo (https://github.com/Litote/kmongo)
- [dokka] - Documentation Engine for Kotlin (https://github.com/Kotlin/dokka)

## Architecture

The application has several layers.

![Architecture](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi/blob/master/architecture/schema.png)

- Routes - all route files define the endpoints of the API
- Modules - receive structured data from the routes and performs operations on it, decides what it will return to the client
- Controller - structures the return data and communicates to the Data Access Object (DAO)
- Data Access Object - defines an interface functions to access the database
- Database - MongoDb

## Tests
All the business logic is covered by unit tests. For the purpose of testing is created a seperate custom mock database in oreder not to damage the production database.
Also there are automated tests for the server with Postman (https://www.postman.com/)

## Routes

The API supports versioning. The current version is `v1`. 
All endpoints are proceeded with `/currentVersion`, e.g. `/v1/register`
All endpoints require a secret API key
Below is provided a list of all API endpoints:

##### Registration
- `/register` - registers a new user
 `POST`
Url Params: `apiKey=[String]`
Body: JSON 
```
{
    "email": "",
    "password": "",
    "nickname": "",
    "genres": []
}
```
Responses: 
* Success: code 200, content: `{"successful": true,"message": "Успешно създаден профил"}`
* Error: code 422, content: `{"successful": false,"message": "Паролата трябва да бъде най-малко 8 знака и трябва да съдържа малки и главни букви, цифри и специални символи"}`;`{
    "successful": false,
    "message": "Невалиден имейл"
}`;`{
    "successful": false,
    "message": "Името не може да бъде празно"
}`;`{
    "successful": false,
    "message": "Името не може да бъде празно"
}`
* Error: code 409, content: `{
    "successful": false,
    "message": "Потребител със същия имейл вече съществува"
}`

Notes: The password that it will check whether it is valid. Password has to contain and least one uppercase letter (A-Z), at least one lowercase letter (a-z), at least one special character (@#$%^&+=_) and has to be longer than 8 characters

##### Login
- `/login` - logins an existing user
 `POST`
Url Params: `apiKey=[String]`
Body: JSON 
```
{
    "email": "",
    "password": "",
}
```
Responses: 
* Success: code 200, content: `{
    "successful": true,
    "message": "Влязохте в системата"
}`
* Error: code 200, content: `{
    "successful": false,
    "message": "Грешен имейл или парола"
}`



