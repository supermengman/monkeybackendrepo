package com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PredictionRunner {
    public int runPythonScript(double attribute1, double attribute2, double attribute3) {
        
        return (int) attribute2;
//        try {
//            String pythonScript = "import pandas as pd\n" +
//                    "from sklearn.tree import DecisionTreeClassifier\n" +
//                    "from sklearn.model_selection import train_test_split\n" +
//                   "from sklearn.metrics import accuracy_score\n" +
//                    "from sklearn import tree\n" +
//                    "X = [[9,9,9], [8,8,8], [7,7,7], [8,8,8], [7,7,7], [7,7,7], [7,7,7], [8,8,8], [8,8,8], [8,8,8], [9,9,9], [8,8,8], [9,9,9], [6,6,6], [6,6,6], [5,5,5], [4,4,4], [3,3,3], [5,5,5], [4,4,4], [7,7,7], [5,5,5], [8,8,8], [8,8,8], [8,8,8], [8,8,8], [8,8,8], [7,7,7], [5,5,5], [5,5,5], [6,6,6], [5,5,5], [4,4,4], [5,5,5], [4,4,4], [4,4,4], [7,7,7], [2,2,2], [2,2,2], [1,1,1], [3,3,3], [2,2,2], [2,2,2], [3,3,3], [1,1,1], [3,3,3], [4,4,4], [5,5,5], [6,6,6], [7,7,7], [5,5,5], [4,4,4], [4,4,4], [4,4,4], [3,3,3], [3,3,3], [2,2,2], [3,3,3], [4,4,4], [1,1,1], [3,3,3], [3,3,3], [4,4,4], [6,6,6], [7,7,7], [7,7,7], [5,5,5], [4,4,4], [3,3,3], [2,2,2], [2,2,2], [4,4,4], [5,5,5], [7,7,7], [4,4,4], [3,3,3], [5,5,5], [5,5,5], [3,3,3], [4,4,4], [6,6,6], [4,4,4], [4,4,4], [6,6,6], [4,4,4], [3,3,3], [5,5,5], [5,5,5], [3,3,3], [6,6,6], [6,6,6], [4,4,4], [5,5,5], [6,6,6], [4,4,4], [6,6,6], [5,5,5], [6,6,6], [4,4,4], [6,6,6]]\n" +
//                    "y = [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 4, 4, 4, 5, 3, 2, 5, 3, 5, 5, 5, 5, 5, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 2, 2, 1, 2, 1, 1, 1, 1, 2, 2, 2, 3, 4, 4, 4, 4, 3, 3, 3, 2, 3, 3, 1, 2, 2, 3, 4, 5, 5, 4, 3, 2, 1, 1, 1, 3, 4, 2, 1, 3, 3, 2, 3, 4, 3, 3, 4, 3, 2, 3, 3, 1, 4, 4, 2, 3, 4, 2, 4, 3, 4, 2, 4]\n" +
//                    "X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)\n" +
//                    "clf = DecisionTreeClassifier()\n" +
//                    "clf.fit(X_train, y_train)\n" +
//                    "y_pred = clf.predict(X_test)\n" +
//                    "def predict_score(attribute1, attribute2, attribute3):\n" +
//                    "    input_data = pd.DataFrame([[attribute1, attribute2, attribute3]])\n" +
//                   "    prediction = clf.predict(input_data)\n" +
//                    "    return prediction[0]\n" +
//                    "predicted_score = predict_score(" + attribute1 + ", " + attribute2 + ", " + attribute3 + ")\n" +
//                    "print(predicted_score)";

//            ProcessBuilder processBuilder = new ProcessBuilder("python", "-c", pythonScript);
//            Process process = processBuilder.start();

//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line = reader.readLine();

//            int exitCode = process.waitFor();
//            System.out.println("Python script execution finished with exit code: " + exitCode);
//            return Integer.parseInt(line);

//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }

//        return -5;
    }

    public static void main(String[] args) {
        PredictionRunner scriptRunner = new PredictionRunner();
        int predictedScore = scriptRunner.runPythonScript(1.14, 4.9, 8.91);
        System.out.println("Score: " + predictedScore);
    }
}