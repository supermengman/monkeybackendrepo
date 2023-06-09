## Team Macro Monkeys 🐒

## Table of Contents
1. [Overview](#overview)
2. [Build Instructions (Backend)](#build-instructions-backend)
3. [Features](#features)
4. [Technical Requirements](#technical-requirements)
5. [Usage](#usage)
6. [Contributing](#contributing)
7. [License](#license)

## Overview

Application that asks users CS based questions that are solved by inserting code which is checked by a series of algorithmns.
This project aims to enhance our existing CS problem-solving platform by expanding the range of topics beyond 2D Arrays/Arrays. It also includes the development of a recommendation engine that suggests problems based on a user's skill level and a prediction feature to estimate a user's AP score based on their problem-solving proficiency. Additionally, the project focuses on improving the scoring system by incorporating more test cases for partial scoring, handling complex inputs, and providing helpful feedback to users.

[Frontend Repo](https://github.com/supermengman/macro-monkeys)

Visit the deployed site: https://monkeygame.rohanj.dev/

## Build Instructions (Backend)

1. Clone the repository to a machine
git clone https://github.com/supermengman/monkeybackendrepo.git

2. Navigate to the project directory:
cd monkeybackendrepo

4. Configure the backend environment variables:
- Create a .env file, and set the admin key (should be some random string that only you know), example below
- The Admin Key is used to access private data such as the CSV with user information
```
ADMIN_KEY=asdfjkashdfaos
```

5. Build the project:
docker-compose build

6. Start the backend server on port 8298:
docker-compose up -d

7. Access the application:
- Open a web browser.
- Enter the URL http://localhost:8298

## Features

1. **Expanded Topic Coverage:** The platform will include a wider range of topics besides 2D Arrays/Arrays. Users will be able to explore and solve problems related to various CS concepts.

2. **Recommendation Engine:** A recommendation engine will be implemented to suggest problems suited to a user's skill level. This recommendation will be based on an assessment of the user's proficiency, potentially utilizing a diagnostic evaluation.

3. **AP Score Prediction:** The system will predict a user's AP (Advanced Placement) score based on their ability to solve specific problems. The prediction will provide an estimate of the user's performance in the AP exam.

4. **Enhanced Scoring System:** The scoring mechanism will be improved by incorporating additional test cases for partial scoring. This will involve the inclusion of more complex inputs and edge cases to accurately evaluate the user's solution. Furthermore, the feedback provided to users will be enhanced to offer more helpful insights.

5. **Data Structures and Operations:** The project will leverage various data structures, such as an online database, to store user-submitted code and their corresponding scores. CRUD operations (Create, Read, Update, Delete) will be implemented to manage user data. Sorting, data filtering, and query operations will be used to organize problems based on difficulty and categories.

## Technical Requirements

The following technical components are necessary to implement the project effectively:

- **Online Database:** An online database will be utilized to store user code submissions and scores. CRUD operations (Create, Read, Update, Delete) will be implemented to manage user data effectively.

- **Sorting and Filtering:** Problems will be sorted and filtered based on their difficulty level. This functionality will be used for recommending problems to users and allowing users to browse problems by categories.

- **Data Structures:** Proper implementation of data structures will be essential to support efficient querying, filtering, and sorting of problems and user data.

## Usage

1. **Solving Problems:** Users can browse through available problem categories and select a problem of interest. The platform will guide users through solving the problem by presenting the necessary code template and any additional instructions.

2. **Recommendation Engine:** Users will receive personalized problem recommendations based on their skill level. The recommendation engine will analyze user performance and suggest suitable problems for further practice.

3. **AP Score Prediction:** Users can track their progress and receive an estimated AP score based on their problem-solving ability. This feature aims to provide users with valuable insights into their preparedness for the AP exam.

4. **Scoring and Feedback:** The scoring system will evaluate user-submitted code against a range of test cases, including complex inputs and edge cases. Users will receive detailed feedback highlighting areas for improvement and suggestions for optimizing their solutions.

5. **Data Management:** The online database will store user code submissions and their associated scores. Users can access their historical data and track their progress over time. CRUD operations will be available for managing user data effectively.

## Contributing
We welcome contributions to this project. To contribute, follow these steps:

Fork the repository

Create a new branch for your feature or bug fix.

Implement your changes and ensure that the codebase remains in good quality.

Write appropriate tests to validate the functionality of your changes.

Submit a pull request with a descriptive title and a summary of your changes.

Participate in the code review process by addressing any feedback or suggestions.

You can follow a basic guide for training and testing a machine learning model similar to that in this project with this guide: https://aaravarora01.github.io/AaravArora-APCSA-Fastpages/jupyter/2023/05/15/apscoresml.html 

## License

This project is licensed under the MIT LICENSE
