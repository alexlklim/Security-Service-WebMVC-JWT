# Security Service with JWT token authentication
Main Technologoies: Java, Spring Boot, Spring Security, Hibernate, JPA, MySQL, JMailSender, Swagger OpenAPI, Mapstruct, lombok

# Security service

Expiration time for access token is 5 minutes, for refresh token is 1 hour. If your access token is expired, send a refresh token to get a new one. If the refresh token is expired too, you need to login again. After logout your refresh-token will be deleted from DB, and It is not possible to get access tokens using them, but your access token will be active for several minutes until its expiration time is over. If you try to get a new access token, the service returns a new access token and refresh token. Both of them will be new. Don’t try to save them in cookies (because of their size, it is about 4KB), access token is very heavy for this, also sometimes Chrome doesn’t allow saving JWT in cookies, it is bad practice because of CSRF attack. With access token you can reach any service, it will be parsed by Spring Security. To get a refresh token ask the Authentication Service.
The application uses BCryptPasswordEncoder for encrypting passwords stored in the database.For encrypting access tokens, the application uses the BASE64 algorithm.
Refresh tokens, unique to each user, are generated using UUIDs (Universally Unique Identifiers).
The security system employs a hybrid approach combining JWT-based authorization with cookie-based authentication. This strategy enables stateless authorization while providing more control over the tokens.

## Login Request

**Endpoint**:  
`POST http://{{server}}:9095/api/v1/security/auth/login`

**Description**:  
This endpoint allows users to log in. It generates access and refresh tokens. The access token is used to authorize requests, while the refresh token is used to obtain new tokens without re-entering credentials.

- **Access Token**: Valid for 5 minutes.
- **Refresh Token**: Valid for 1 hour. After using it to get new tokens, the previous refresh token will be invalidated.

### Request Body

```json
{
    "email": "alex@gmail.com",
    "password": "1122"
}
```

### Response 
200 OK - everything is okay

```json
{
    "first_name": "Alex",
    "last_name": "Klim",
    "expires_at": "2024-06-20 10:35",
    "role": ["ADMIN" ],
    "access_token": "eyJhNiJ9.eyJY29tNTR9.lwLegVQPa1C8",
    "refresh_token": "d838226a-9295-4340-a9da-ce9a7fe8e506"
}
```

400 UNAUTHORIZED - something wrong

```json
{
    "code": 401,
    "message": "Authentication failed"
}
```



## Logout Request

**Endpoint**:  
`GET http://{{server}}:9095/api/v1/security/auth/logout`

**Description**:  
This action deletes your refresh token from the database, making it impossible to obtain new tokens using the refresh endpoint. The access token will remain valid until its expiration date (approximately 5 minutes).

### Parameters

- **Authorization**: Bearer token

### Responses

**Success (200 OK)**:  
If everything is okay, you will receive a response with no additional content.


## Refresh Request

**Endpoint**:  
`POST http://{{server}}:9095/api/v1/security/auth/refresh`

**Description**:  
This endpoint returns a new AuthDTO. Use this endpoint to obtain new access and refresh tokens if your access token has expired and you cannot access resources.

### Parameters

- **Authorization**: Not required for this endpoint.

### Request Body

```json
{
    "refresh_token": "c5f207cf-ae7b-482e-bfc8-26c783dea10a",
    "email": "alex@gmail.com"
}
```
### Response 
200 OK - everything is okay

```json
{
    "first_name": "Alex",
    "last_name": "Klim",
    "expires_at": "2024-06-20 10:35",
    "role": ["ADMIN" ],
    "access_token": "eyJhNiJ9.eyJY29tNTR9.lwLegVQPa1C8",
    "refresh_token": "d838226a-9295-4340-a9da-ce9a7fe8e506"
}
```

400 UNAUTHORIZED - something wrong

```json
{
    "code": 401,
    "message": "Authentication failed"
}
```

## Register User

**Endpoint**:  
`POST http://{server}:9095/api/v1/security/auth/register`

**Description**:  
After a successful request, an email with a confirmation link will be sent to the specified email address in the request body. The server does not check if the token is expired.

### Request Body

```json
{
    "first_name": "Alex",
    "last_name": "Klim",
    "phone": "48 788 202 134",
    "email": "t.klimenko@csmm.pl",
    "password": "1122",
    "role": "USER"
}
```

### Response 
201 CREATED - everything is okay

```json
{
    "first_name": "Alex",
    "last_name": "Klim",
    "expires_at": "2024-06-20 10:35",
    "role": ["ADMIN" ],
    "access_token": "eyJhNiJ9.eyJY29tNTR9.lwLegVQPa1C8",
    "refresh_token": "d838226a-9295-4340-a9da-ce9a7fe8e506"
}
```

409 CONFLICT - email is taken

```json
{
    "code": 409,
    "message": "User with email {} is already exists"
}
```

## Activate User Account

**Endpoint**:  
`GET http://{server}:9095/api/v1/security/auth/activate/{UUID}`

**Description**:  
Users can use this link after registration to activate their accounts. The link contains a unique token. The server will find the user associated with this token and update their activation status.

### Responses

200 OK - everything is okay


## Change Password

**Endpoint**:  
`GET http://{{server}}:9095/api/v1/security/auth/pw/change`

**Description**:  
This endpoint allows authenticated users to change their passwords.

### Parameters

- **Authorization**: Bearer token (required)
- **Roles**: ADMIN, USER

### Request Body

```json
{
    "current_password": "1122",
    "new_password": "1111"
}
```


## Password Recovery

**Endpoint**:  
`POST http://{{server}}:9095/api/v1/security/auth/pw/recovery/{{$randomUUID}}`

**Description**:  
Users should click the link from the email (handled by the front-end) and send their new password in the body of the request to this endpoint. If the token does not exist, the server will return an Unauthorized response.

### Request Body

```json
{
    "password": "1122"
}
```

## Get Info About Authenticated User

**Endpoint**:  
`GET http://{{server}}:9095/api/v1/security/auth`

**Description**:  
This endpoint retrieves information about the authenticated user.

### Parameters

- **Authorization**: Bearer token (required)

### Responses

**Success (200 OK)**:  
If everything is okay, you will receive a response with user information:

```json
{
    "id": 1,
    "email": "alex@gmail.com",
    "first_name": "Alex",
    "last_name": "Klim",
    "phone": null,
    "is_active": true,
    "last_activity": "2024-07-08 14:25",
    "role": "ADMIN",
    "active": true
}
```


## Get User by ID

**Endpoint**:  
`GET http://{{server}}:9095/api/v1/security/users/{user_id}`

**Description**:  
This endpoint retrieves information about a specific user. Access is restricted to users with the ADMIN role.

### Parameters

- **Authorization**: Bearer token (required)
- **Roles**: ADMIN (required)

### Responses

**Success (200 OK)**:  
If everything is okay, you will receive a response with user information:

```json
{
    "id": 1,
    "email": "alex@gmail.com",
    "first_name": "Alex",
    "last_name": "Klim",
    "phone": null,
    "is_active": true,
    "last_activity": "2024-07-08 14:25",
    "role": "ADMIN",
    "active": true
}
```


## Get Users

**Endpoint**:  
`GET http://{{server}}:9095/api/v1/security/users`

**Description**:  
This endpoint retrieves a list of users. Access is restricted to users with the ADMIN role.

### Parameters

- **Authorization**: Bearer token (required)
- **Roles**: ADMIN (required)

### Responses

**Success (200 OK)**:  
If everything is okay, you will receive a response with a list of users:

```json
[
    {
        "id": 1,
        "email": "alex@gmail.com",
        "first_name": "Alex",
        "last_name": "Klim",
        "is_active": true,
        "last_activity": "2024-07-08 14:28",
        "role": "ADMIN",
        "active": true
    }
]
```

## Update User (by Admin)

**Endpoint**:  
`PUT http://{{server}}:9095/api/v1/security/users/{user_id}`

**Description**:  
This endpoint allows an ADMIN to update user information.

### Parameters

- **Authorization**: Bearer token (required)
- **Roles**: ADMIN (required)

### Request Body

```json
{
    "email": "aldex@gmail.com",
    "first_name": "Alddex",
    "last_name": "Klddim",
    "role": "ADMIN",
    "active": true
}
```
