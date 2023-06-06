# Login API Documentation

The Login API provides endpoints for user authentication, logout, and retrieving user information. It is implemented in Java using the Spring framework.

## Endpoints

### Authenticate User

**URL:** `/api/login/authenticate`

**Method:** POST

**Description:** Authenticates a user by checking the provided username and password against the stored user data. If the authentication is successful, a JSON Web Token (JWT) is created and returned in the response. The JWT is also stored in a cookie named `flashjwt` for subsequent API requests.

**Request Body:**
```json
{
  "name": "username",
  "password": "password"
}
```

**Response:**
- Status: 200 OK
- Body: The generated JSON Web Token (JWT) representing the authenticated user.

If authentication fails due to incorrect password or a user with the given username not found, an error message is returned in the response body.

### Logout

**URL:** `/api/login/logout`

**Method:** POST

**Description:** Logs out the currently logged-in user by clearing the `flashjwt` cookie.

**Response:**
- Status: 200 OK
- Body: A success message indicating that the user has been logged out.

### Get User Information

**URL:** `/api/login/getYourUser`

**Method:** POST

**Description:** Retrieves the user information for the currently authenticated user based on the provided JSON Web Token (JWT) stored in the `flashjwt` cookie.

**Request Header:**
- Cookie: `flashjwt=[JWT]`

**Response:**
- Status: 200 OK
- Body: The user information as a JSON object.

If the JWT is invalid or expired, or if the user account doesn't exist, an error message is returned in the response body.

## Error Handling

The API includes error handling for the following scenarios:

- Missing `flashjwt` cookie: If the `flashjwt` cookie is missing in the request, a `MissingRequestCookieException` is thrown. The exception is caught and handled by returning an error message in the response.

## Dependencies

The API relies on the following dependencies:

- Spring Framework
- Spring Web
- Spring Data JPA

Make sure to have these dependencies properly configured in your Java project.

---

This documentation provides an overview of the Login API implemented in Java using the Spring framework. For detailed implementation and usage instructions, please refer to the corresponding Java source code.
