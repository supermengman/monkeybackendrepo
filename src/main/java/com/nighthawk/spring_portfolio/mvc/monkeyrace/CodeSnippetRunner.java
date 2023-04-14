package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

public class CodeSnippetRunner {
    public enum Problem {
        FRQ_A_2018, FRQ_B_2018
    }

    Problem problem;

    public CodeSnippetRunner(Problem p) {
        this.problem = p;
    }

    private String getProblemFileName() {
        switch (this.problem) {
            case FRQ_A_2018:
                return "2018FRQA.javat";
            case FRQ_B_2018:
                return "2018FRQB.javat";
        }
        return null;
    }

    private String getTemplate() {
        String fileName = getProblemFileName();
        if (fileName == null) return null;

        InputStream stream;
        try {
            stream = new ClassPathResource("classpath:javatemplates/" + fileName).getInputStream();
        } catch (Exception e) {
            return null;
        }
        
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));

        return r.lines().collect(Collectors.joining("\n"));
    }
}
