# Level API Documentation

The Level API provides endpoints for retrieving information about game levels. It is implemented in Java using the Spring framework.

## Endpoints

### Get Level

**URL:** `/api/level/getLevel`

**Method:** POST

**Description:** Retrieves the details of a specific game level based on the provided level number. The level information includes the level object and its corresponding description from a file.

**Request Body:**
```json
{
  "number": "1"
}
```

**Response:**
- Status: 200 OK
- Body: The level object and its description as a JSON object.

If the level with the given number is not found, an error message is returned in the response body.

## File Description

The API reads the description of each level from a file. The file path is constructed using the `descriptionFile` field of the level object. The file should be located in the `descriptions` directory of the classpath.

## Error Handling

The API includes error handling for the following scenarios:

- Missing `description` file: If the description file for a level is not found, an exception is thrown. The exception is caught and logged, and an error message is returned in the response.

- Missing level: If the level with the given number is not found, an error message is returned in the response.


---

This documentation provides an overview of the Level API implemented in Java using the Spring framework. For detailed implementation and usage instructions, please refer to the corresponding Java source code.
[Source Code](https://github.com/supermengman/monkeybackendrepo/blob/main/src/main/java/com/nighthawk/spring_portfolio/mvc/monkeyrace/LevelApiController.java)
