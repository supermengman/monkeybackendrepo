package com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PredictionRunner {
    public int runPythonScript(int attribute1, int attribute2, int attribute3) {
        try {
            String pythonScript = "import pandas as pd\n" +
                    "from sklearn.tree import DecisionTreeClassifier\n" +
                    "from sklearn.model_selection import train_test_split\n" +
                    "from sklearn.metrics import accuracy_score\n" +
                    "from sklearn import tree\n" +
                    "data = pd.read_csv('apscores.csv')\n" +
                    "X = data.iloc[:, 1:4]\n" +
                    "y = data.iloc[:, 4]\n" +
                    "X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)\n" +
                    "clf = DecisionTreeClassifier()\n" +
                    "clf.fit(X_train, y_train)\n" +
                    "y_pred = clf.predict(X_test)\n" +
                    "def predict_score(attribute1, attribute2, attribute3):\n" +
                    "    input_data = pd.DataFrame([[attribute1, attribute2, attribute3]])\n" +
                    "    prediction = clf.predict(input_data)\n" +
                    "    return prediction[0]\n" +
                    "predicted_score = predict_score(" + attribute1 + ", " + attribute2 + ", " + attribute3 + ")\n" +
                    "print(predicted_score)";

            ProcessBuilder processBuilder = new ProcessBuilder("python", "-c", pythonScript);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();

            int exitCode = process.waitFor();
            System.out.println("Python script execution finished with exit code: " + exitCode);

            return Integer.parseInt(line);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static void main(String[] args) {
        PredictionRunner scriptRunner = new PredictionRunner();
        int predictedScore = scriptRunner.runPythonScript(3, 4, 5);
        System.out.println("Predcted Score: " + predictedScore);
    }
}