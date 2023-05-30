package com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

import org.springframework.http.ResponseEntity;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ScorePredictor {
    ResponseEntity<Object> response = CodeSnippet.getLevelList("your_jwt_value");

    if (response.getStatusCode() == HttpStatus.OK) {
        Object responseBody = response.getBody();
        if (responseBody instanceof Map) {
            Map<String, Object> result = (Map<String, Object>) responseBody;
            List<Level> levels = (List<Level>) result.get("levels");
            HashMap<Integer, Integer> status = (HashMap<Integer, Integer>) result.get("status");

            int score1 = status.get(1);  // Get score for level 1
            int score2 = status.get(2);  // Get score for level 2
            int score3 = status.get(3);  // Get score for level 3
        }
    
        int predictedScore = runPythonScript(int score1, int score2, int score3);
    }
}
