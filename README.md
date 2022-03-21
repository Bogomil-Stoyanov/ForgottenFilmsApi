# Forgotten Films REST API Documentation

#### Contents
* [Overview](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi#overview)
* [Features](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi#features)
* [Libraries](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi#libraries)
* [Architecture](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi#architecture)
* [Tests](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi#tests)
* [Security](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi#security)
* [Routes](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi#routes)
* [Deployment](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi#deployment)

## Overview
Forgotten Films REST API is a Ktor Rest API server for watching selected films from the Public Domain. It provied everything that you will need to immerse the user in the world of classic films.<br />
**Currently the server is running on: https://157.230.22.117:8081**<br />
**Get the Android client application here:** [Forgotten Films](http://bbsapps.eu/forgottenfilms/forgottenfilms.html)<br />

#### Tech stack:
- Language: Kotlin
- Framework: Ktor (https://ktor.io/)
- Database: MongoDb

#### Detailed documentation: [Documentation](http://bbsapps.eu/forgottenfilms/APIDoc/documentation/index.html)

## Features

- Register a user with email, password, nickname and favourite film genres
- Login with email and password
- Forgotten password and change password
- Get recommended films to watch depending on your favourite genres
- Search, watch, like, dislike and share films
- Watch time statistics - collects user activities in order to provide a great experience and recommend films.

## Libraries

Forgotten Films uses the most modern libraries for backend development with Ktor

- [Ktor] - Framework to build asynchronous server applications (https://github.com/ktorio/ktor)
- [KMongo] - Kotlin toolkit for Mongo (https://github.com/Litote/kmongo)
- [dokka] - Documentation Engine for Kotlin (https://github.com/Kotlin/dokka)
- [Coroutines] - Lightweight threads (https://github.com/Kotlin/kotlinx.coroutines)

## Architecture

The application has several layers.

![Architecture](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi/blob/master/architecture/schema.png)

- Routes - all route files define the endpoints of the API
- Modules - receive structured data from the routes and performs operations on it, decides what it will return to the client
- Controller - structures the return data and communicates to the Data Access Object (DAO)
- Data Access Object - defines an interface functions to access the database
- Database - MongoDb
<br />
Database schema: 

![Database schema](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi/blob/master/architecture/database_schema.png)

## Tests
All the business logic is covered by unit tests. For the purpose of testing is created a seperate custom mock database in oreder not to damage the production database.
Unit tests:

![Module test](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi/blob/master/architecture/ModuleTestSuite.png)

Also there are automated tests for the server with Postman (https://www.postman.com/)

![Server test](https://github.com/Bogomil-Stoyanov/ForgottenFilmsApi/blob/master/architecture/newManTestResults.png)

## Security
Top most priority for any project is the security. The server is protected agains various attacks. The database is protected from NoSQL Injection attacks, because KMongo is used in the project, which is an extension over the MongoDB Java Driver. This tool uses BSON (Binary JSON) for queries and documents. When a given request is sent it is in String format and is passed directly to as a query, without parsing. This way all kind of NoSQL Innjection attacks are avoided. There is DDoS protection. Each request to the server has `apiKey` attached, which is a secret and unique for every request. Each request has to be authenicated with Basic Auth. If any of these parameters is unauthentic or broken the request is rejected. All traffic is HTTPS encrypted. This way attacks such as Man-In-The-Middle (MITM) are prevented. All sensitive user data are hashed with SHA256 and salted.

## Routes
The API supports versioning. The current version is `v1`. 
All endpoints are proceeded with `/currentVersion`, e.g. `/v1/register`
All endpoints require a secret API key
Below is provided a list of all API endpoints:

##### Registration
- `/register` - registers a new user
 `POST`
Url Params: `apiKey=[String]`
Request Body: JSON 
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
Body Request: JSON 
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

##### Film Admin

- `/isAdmin` - checks if a user is an admin
`GET`
Url Params: `apiKey=[String]`
Responses: `true` if the authenticated is an admin, else `false`

- `/users` - gets a list of all users
`GET`
Url Params: `apiKey=[String]`
Response: 
`[
    {
        "email": "email",
        "name": "Name"
    },
    {
        "email": "email",
        "name": "name"
    }
]`
- `/films` - adds films to the database
`POST`
Url Params: `apiKey=[String]`
Request Body: JSON 
```
{
     "films": [
        {
            "name": "Movie",
            "imageUrls": [
                "url1",
                "url2"
            ],
            "description": "Desciption",
            "genres": [
                "Test"
            ],
            "likedBy": [],
            "dislikedBy": [],
            "url": "url"
        }
    ]
}
```
- `/filmsCount` - gets the count of all movies
`GET`
Url Params: `apiKey=[String]`
Response: Int

- `/usersCount` - gets the count of all movies
`GET`
Url Params: `apiKey=[String]`
Response: Int

- `/stats` - gets all watchtime statistics for admin
`GET`
Url Params: `apiKey=[String]`
Response: 
```
[
    {
        "genre": "Общо",
        "totalWatchTimeInSeconds": 1
    },
    {
        "genre": "Genre",
        "totalWatchTimeInSeconds": 1
    }
]
```

- `/user` - deletes a user with email
`DELETE`
Url Params: `apiKey=[String]` ; `email=[String]` - the user's email to be deleted

- `/films` - deletes a movie with a title 
`Delete`
Url Params: `apiKey=[String]` ; `filmName=[String]` - the user's name to be deleted

##### Account Managment
- `/nickname` - updates user nickname
`PATCH`
Url Params: `apiKey=[String]` ; `newNickname=[String]`
Responses:
`{
    "successful": true,
    "message": "Успешно смени името си"
}`

- `/genres` - update user's genres
`PATCH`
Url Params: `apiKey=[String]`
Request Body: 
```
{
    "genres": [
        "Genre 1",
        "Genre 2"
    ]
}
```

- `/userGenres` - gets a list of user's genres
`GET`
Url Params: `apiKey=[String]`
Response: 
```
[
    "Genre 1",
    "Genre 2"
]
```

- `/filmList` - adds a film to user's list
`POST`
Url Params: `apiKey=[String]`; `id=[String]` - id of the film
Responses:
`{ "successful": true, "message": "Филмът е добавен към списъка ти" }`

- `/filmList` - removes a film from user's list
`DELETE`
Url Params: `apiKey=[String]`, `id=[String]` - id of the film
Responses:
If the film was in user's list and it was removed: `{
    "successful": true,
    "message": "Филмът е премахнат от списъка ти"
}`
If the film was not in user's list: `{
    "successful": false,
    "message": "Филмът не е в списъка ти"
}`

- `/watchTime` - gets the watchtime of every genere the user has watched
`GET`
Url Params: `apiKey=[String]`
Responses:
```
[{
    "genre": "Total",
    "totalWatchTimeInSeconds": 1
}, {
    "genre": "Genere",
    "totalWatchTimeInSeconds": 1
}]
```

- `/watchTime` - add watch time for genre
`POST`
Url Params: `apiKey=[String]`
Request Body: 
```
{
    "genre": "Genre",
    "additionalWatchTimeInSeconds": 1
}
```
Responses: `{
    "successful": true,
    "message": "Успешно обновено време за гледане"
}`

- `/nickname` - gets user's nickaname
`GET`
Url Params: `apiKey=[String]`
Responses: `{
    "successful": true,
    "message": "Example Nickname"
}`

- `/changePassword` - updates user password, the password needs to be valid
`POST` 
Url Params: `apiKey=[String]`; `password=[String]`
Responses:
If the password is valid: `{
    "successful": true,
    "message": "Паролата е сменена, моля излезте и влезте в систамата"
}`
If the password was not valid: `{
    "successful": true,
    "message": "Паролата трябва да бъде най-малко 8 знака и трябва да съдържа малки и главни букви, цифри и специални символи (@#\$%^&amp;+=_)"
}` 

- `/forgottenPassword` - if a user has forgotten their password an email with a new random temporary password is sent
`POST` 
Url Params: `apiKey=[String]`; `email=[String]`
Responses:
If the email was successfully sent: `{
    "successful": true,
    "message": "Изпратен е имейл на email, съдържащ инструкции са възстановяване на акаунта"}`
If a user with this mail does not exist: `{
    "successful": false,
    "message": "Потребител с този имейл не съществува"}`


##### Films
- `/filmLike` - liked a film, if the film is already liked - the like is removed, if the film is already disliked - the dislike is removed and a like is added
`POST`
Url Params: `apiKey=[String]`; `id=[String]` - the id of the liked film
Responses:
If the film hasn't been already liked: `1`
If the film has been disliked an a like is added: `1`
If a like is removed: `0`

- `/filmDislike - dislike a film, if the film is already disliked - the dislike is removed, if the film is already liked - the like is removed and a dislike is added
`POST`
Url Params: `apiKey=[String]`; `id=[String]` - the id of the liked film
Responses:
If the film hasn't been already dislliked: `-1`
If the film has been liked an a dislike is added: `-1`
If a dislike is removed: `0`

- `/likes` - gets a like count for film
`GET`
Url Params: `apiKey=[String]`; `id=[String]` - the id of the liked film
Response: Int

- `/dislikes` - gets a like count for film
`GET`
Url Params: `apiKey=[String]`; `id=[String]` - the id of the liked film
Response: Int

- `/film` - gets a film for a user
`GET`
Url Params: `apiKey=[String]`; `id=[String]` - the id of the liked film
Responses: 
```
{
    "name": "Title",
    "imageUrls": [
        "url1",
        "url2"
    ],
    "description": "Desciption",
    "categories": [
        "Genre"
    ],
    "likes": 0,
    "dislikes": 0,
    "isLiked": 0,
    "url": "testurl",
    "id": "id"
}
```

- `/feed` - gets a user's feed
`GET`
Url Params: `apiKey=[String]`
Responses:
```
[{
    "title": "Genre",
    "films": [{
        "name": "Title",
        "image": "url",
        "id": "id"
    }]
}, {
    "title": "Може да ти хареса",
    "films": [{
        "name": "Title",
        "image": "url",
        "id": "id"
    }]
}, {
    "title": "Най-харесвани филми",
    "films": [{
        "name": "Title",
        "image": "url",
        "id": "id"
    }]
}]
```
-`/search` - search a film by title
`GET`
Url Params: `apiKey=[String]`, `query=[String]` - the title of the film
Responses: `[{
    "name": "title",
    "image": "url1",
    "id": "id"
}]`

- `/genres` - gets a list of all genres alphabetically ordered
`GET`
Url Params: `apiKey=[String]`
Responses: ["Genre1","Genre2"]

- `/films` - gets a list of all films sorted by main genre
`GET`
Url Params: `apiKey=[String]`
Response:
```
[{
    "title": "Genre1",
    "films": [{
        "name": "Title",
        "image": "url",
        "id": "id"
    }]
}, {
    "title": Genre 2",
    "films": [{
        "name": "Title",
        "image": "url",
        "id": "id"
    }]
}]
```

## Deployment
To run this Ktor application you need a VPS with root access to deploy it on. 
1. Clone the repository and  generate a fat jar (`build.gradle`)
2. Connect to your server with SFTP
3. Upload the jar file and the keystore
4. Connect to your server with SSH
5. Install Java 
6. Install MongoDB (https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/)
7. Verify that MongoDB was successfully installed: `sudo systemctl status mongod`
8. Create a service configuration file for the Ktor Server:
```
[Unit]
Description= Forgotten Films Service
After=mongod.service
StartLimitIntervalSec=0

[Service]
Type=simple
RestartSec=1
Restart=always
User=root
ExecStart=/usr/bin/java -jar /home/app-0.0.1.jar
Restart=always

[Install]
WantedBy=multi-user.target
```
9. Start the service: `sudo systemctl start films` (your service file name may differ)
10. Check the status of the server: `sudo systemctl status films`
