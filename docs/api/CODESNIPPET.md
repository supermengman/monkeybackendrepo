# CodeSnippetApiController API Documentation

The CodeSnippetApiController API provides endpoints for managing code snippets and attempting levels in a monkey race game. It is implemented in Java using the Spring framework.

## Endpoints

### Attempt Level

**URL:** `/api/code/attemptLevel`

**Method:** POST

**Description:** Attempts a level with the given code. The code is tested against the level's problem using a CodeSnippetRunner. The result of the test, including any errors and the number of test cases passed, is saved for the user.

**Request Body:**
```json
{
  "levelNumber": 1,
  "code": "function add(a, b) {\n  return a + b;\n}"
}
```

**Response:**
- Status: 200 OK
- Body: Successful attempt.
  ```json
  {
    "msg": "2/2 Testcases were passed."
  }
  ```

- Status: 400 Bad Request
- Body: Error occurred during attempt.
  ```json
  {
    "err": "Error message"
  }
  ```

### Get Snippet

**URL:** `/api/code/getSnippet`

**Method:** POST

**Description:** Retrieves the code snippet for a specific level. The snippet is associated with the user and level and contains the code they have previously submitted.

**Request Body:**
```json
{
  "level": 1
}
```

**Response:**
- Status: 200 OK
- Body: The code snippet object containing the user's code for the specified level.

- Status: 400 Bad Request
- Body: No code snippet found for the user and level.
  ```json
  {
    "err": "No Code Snippet Found"
  }
  ```

### Get Level List

**URL:** `/api/code/getLevelList`

**Method:** POST

**Description:** Retrieves the list of levels and their statuses for the user. The status indicates whether the user has attempted the level and the number of test cases passed.

**Request Body:**
```json
{}
```

**Response:**
- Status: 200 OK
- Body: The list of levels with their statuses for the user.
  ```json
  {
    "levels": [
      {
        "number": 1,
        "name": "Level 1"
      },
      {
        "number": 2,
        "name": "Level 2"
      }
    ],
    "status": {
      "1": 2,
      "2": -1
    },
    "categories": [
      {
        "id": 1,
        "name": "Category 1"
      },
      {
        "id": 2,
        "name": "Category 2"
      }
    ]
  }
  ```

### Get Levels by Category

**URL:** `/api/code/getLevelsByCategory`

**Method:** POST

**Description:** Retrieves the levels and their statuses for the user based on a specific category. The status indicates whether the user has attempted the level and the completion status.

**Request Body:**
```json
{
  "category": "Category 1"
}
```

**Response:**
- Status: 200 OK
- Body: The levels and their statuses for the user in the specified category.
  ```json
  {
    "levels": [
      {
        "number": 1,
        "name": "Level 1"
      },
      {
        "number": 2,
        "name": "Level 2"
      }
    ],
    "status": {
      "1": "Complete",
      "2": "Not Attempted"
    }
  }
  ```

### Get Data

 for Level

**URL:** `/api/code/getDataForLevel`

**Method:** POST

**Description:** Retrieves the data for a specific level. The data includes the level object and its associated category.

**Request Body:**
```json
{
  "levelNumber": 1
}
```

**Response:**
- Status: 200 OK
- Body: The level object and its associated category.
  ```json
  {
    "level": {
      "number": 1,
      "name": "Level 1",
      "problem": "Write a function to add two numbers.",
      "description": "This is the first level of the monkey race game."
    },
    "category": {
      "id": 1,
      "name": "Category 1"
    }
  }
  ```

---

This documentation provides an overview of the CodeSnippetApiController API implemented in Java using the Spring framework. For detailed implementation and usage instructions, please refer to the corresponding Java source code.
[Source Code](https://github.com/supermengman/monkeybackendrepo/blob/main/src/main/java/com/nighthawk/spring_portfolio/mvc/monkeyrace/CodeSnippetApiController.java)
