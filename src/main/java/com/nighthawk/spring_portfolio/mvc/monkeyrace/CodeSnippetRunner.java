package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.util.Pair;

public class CodeSnippetRunner {
    String problem;

    public CodeSnippetRunner(String p) {
        this.problem = p;
    }

    private String getProblemFileName() {
        return problem;
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

    private String getJavaCode(String className, String answer, int exitCode) {
        String template = getTemplate();
        if (template == null) {
            System.out.println("Getting template has failed, user's code will FAIL. Problem File: " + getProblemFileName());
            template = "public class {{ classname }} {public static void main(String[] args) {System.exit(255);}}";
        }

        return template.replace("{{ classname }}", className).replace("{{ exitcode }}", Integer.toString(exitCode)).replace("{{ answer }}", answer);
    }

    private String generateRandomClassName(int characters) {
        String name = "";
        for (int i = 0; i<characters; i++) {
            char c = (char)((int)(Math.random() * 26) + 'A');
            name += c;
        }
        return name;
    }

    /**
     * 
     * @param answer The code to be tested
     * @return A string if there is an error (code failed), if passed Optional is empty
     */
    public Pair<Optional<String>, Integer> testCode(String answer) {
        String className = generateRandomClassName(40);
        int exitCode = 2 + (int)(Math.random() * 253); // exit codes 2-254
        String code = getJavaCode(className, answer, exitCode);

        File outputFile = new File("volumes/javacode/" + className + ".java");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(code);
            writer.close();
        } catch (Exception e) {
            System.out.println("FAILED to write code to file\nFile Name: " + outputFile.getAbsolutePath() + "\nCode:" + code);
            System.out.println("Exception: " + e + ", " + e.getStackTrace());
            return Pair.of(Optional.of("Backend error"), 0);
        }

        String[] command = {"java", className + ".java"};
        ProcessBuilder builder = new ProcessBuilder(command);
        builder = builder.directory(new File("volumes/javacode"));
        
        try {
            Process p = builder.start();
            p.waitFor();
            System.out.println(p.exitValue());
            System.out.println(new String(p.getErrorStream().readAllBytes(), StandardCharsets.UTF_8));
            if (p.exitValue() == exitCode) {
                return Pair.of(Optional.empty(), 1);
            } else if (p.exitValue() == 1) {
                return Pair.of(Optional.of("Compilation failed or Runtime Error"), 1);
            } else {
                return Pair.of(Optional.of("Code failed"), 1);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e + ", " + e.getStackTrace());
            return Pair.of(Optional.of("Backend error"), 1);
        }
    }
}
