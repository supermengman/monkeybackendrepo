package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
            System.out.println("Exception: " + e + ", " + e.getStackTrace());
            return null;
        }
        
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));

        return r.lines().collect(Collectors.joining("\n"));
    }

    private String getJavaCode(String className, String answer) {
        String template = getTemplate();
        if (template == null) {
            System.out.println("Getting template has failed, user's code will FAIL. Problem File: " + getProblemFileName());
            template = "public class {{ classname }} {public static void main(String[] args) {System.exit(1);}}";
        }
        if (answer.contains("System.exit")) {
            System.out.println("Stop trying to cheat user");
            template = "public class {{ classname }} {public static void main(String[] args) {System.exit(1);}}";
        }

        return template.replace("{{ classname }}", className).replace("{{ answer }}", answer);
    }

    private String generateRandomClassName(int characters) {
        String name = "";
        for (int i = 0; i<characters; i++) {
            char c = (char)((int)(Math.random() * 26) + 'A');
            name += c;
        }
        return name;
    }

    public boolean isCorrect(String answer) {
        String className = generateRandomClassName(40);
        String code = getJavaCode(className, answer);

        File outputFile = new File("volumes/javacode/" + className + ".java");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(code);
            writer.close();
        } catch (Exception e) {
            System.out.println("FAILED to write code to file\nFile Name: " + outputFile.getAbsolutePath() + "\nCode:" + code);
            System.out.println("Exception: " + e + ", " + e.getStackTrace());
            return false;
        }

        String[] command = {"java", className + ".java"};
        ProcessBuilder builder = new ProcessBuilder(command);
        builder = builder.directory(new File("volumes/javacode"));
        
        try {
            Process p = builder.start();
            p.waitFor();
            if (p.exitValue() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e + ", " + e.getStackTrace());
            return false;
        }
    }
}
