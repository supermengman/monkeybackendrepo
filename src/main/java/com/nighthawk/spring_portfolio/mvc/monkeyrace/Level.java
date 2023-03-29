package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Level {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int number;

    private String question;
    
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    
    private int correctAnswer;

    @ManyToOne
    private Stage stage;

    public static void main(String[] args) {
        Level l = new Level();
        l.setId(10l);
        l.setName("What is sorting?");
        l.setNumber(1);
        l.setQuestion("Sort this array: 5 6 4 2 1");
        l.setAnswer1("1 2 4 5 6");
        l.setAnswer2("2 3 4 5 6");
        l.setAnswer3("6 5 4 2 1");
        l.setAnswer4("1 2 3 5 5");
        l.setCorrectAnswer(1);

        System.out.println(l);
    }
}
