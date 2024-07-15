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
} '''



