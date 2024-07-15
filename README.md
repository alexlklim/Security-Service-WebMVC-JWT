# Security Service with JWT token authentication
Main Technologoies: Java, Spring Boot, Spring Security, Hibernate, JPA, MySQL, JMailSender, Swagger OpenAPI, Mapstruct, lombok

# Security service

## Login Request

**Endpoint**:  
`POST http://{{server}}:9095/api/v1/id_planner/auth/login`

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
`GET http://{{server}}:9095/api/v1/id_planner/auth/logout`

**Description**:  
This action deletes your refresh token from the database, making it impossible to obtain new tokens using the refresh endpoint. The access token will remain valid until its expiration date (approximately 5 minutes).

### Parameters

- **Authorization**: Bearer token

### Responses

**Success (200 OK)**:  
If everything is okay, you will receive a response with no additional content.


## Refresh Request

**Endpoint**:  
`POST http://{{server}}:9095/api/v1/id_planner/auth/refresh`

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
`POST http://{server}:9095/api/v1/id_planner/auth/register`

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
`GET http://{server}:9095/api/v1/id_planner/auth/activate/{UUID}`

**Description**:  
Users can use this link after registration to activate their accounts. The link contains a unique token. The server will find the user associated with this token and update their activation status.

### Responses

**Success (200 OK)**:  
If everything is okay, the user's account will be successfully activated.

