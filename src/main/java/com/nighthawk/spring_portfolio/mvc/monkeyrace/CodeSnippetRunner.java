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

    private String getJavaCode(String className, String answer, int specialCode) {
        String template = getTemplate();
        if (template == null) {
            System.out.println("Getting template has failed, user's code will FAIL. Problem File: " + getProblemFileName());
            template = "public class {{ classname }} {public static void main(String[] args) {System.exit(255);}}";
        }
        
        // special security code that prevents RCE
        String security = "Permission noExecPermission = new RuntimePermission(\"exec\");\n\n"
        + "Policy.setPolicy(new Policy() {\n"
        + "    @Override\n"
        + "    public boolean implies(ProtectionDomain domain, Permission permission) {\n"
        + "        if (permission.equals(noExecPermission)) {\n"
        + "            return false;\n"
        + "        }\n"
        + "        return super.implies(domain, permission);\n"
        + "    }\n"
        + "});\n"
        + "System.setSecurityManager(new SecurityManager());\n\n";

        return template.replace("{{ classname }}", className).replace("{{ specialcode }}", Integer.toString(specialCode)).replace("{{ security }}", security).replace("{{ answer }}", answer);
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
        int specialCode = (int)(100000*Math.random());
        String code = getJavaCode(className, answer, specialCode);

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
        
        // compile
        String[] compileCommand = {"javac", className+".java"};
        ProcessBuilder compileBuilder = new ProcessBuilder(compileCommand);
        compileBuilder = compileBuilder.directory(new File("volumes/javacode"));
        
        try {
            Process p = compileBuilder.start();
            p.waitFor();
            System.out.println(new String(p.getErrorStream().readAllBytes(), StandardCharsets.UTF_8));
            
            if (p.exitValue() != 0) {
                return Pair.of(Optional.of("Compilation Error"), 0);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e + ", " + e.getStackTrace());
            return Pair.of(Optional.of("Backend error"), 0);
        }
        
        // run
        
        String[] command = {"java", className};
        ProcessBuilder builder = new ProcessBuilder(command);
        builder = builder.directory(new File("volumes/javacode"));
        
        try {
            Process p = builder.start();
            p.waitFor();
            System.out.println(p.exitValue());
            System.out.println(new String(p.getErrorStream().readAllBytes(), StandardCharsets.UTF_8));

            if (p.exitValue() == 1) {
                return Pair.of(Optional.of("Runtime Error"), 0);
            }

            String output = new String(p.getInputStream().readAllBytes());
            
            try {
                int outputtedSpecialCode = Integer.parseInt(output.split("-")[0]);
                if (outputtedSpecialCode != specialCode) throw new Exception();
                int testcases = Integer.parseInt(output.split("-")[1].replace("\n", "").replace("\r", "").replace(" ", ""));
                return Pair.of(Optional.empty(), testcases);
            } catch (Exception e) {
                return Pair.of(Optional.of("Error processing solution. Ensure you DO NOT have any prints in your code"), 0);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e + ", " + e.getStackTrace());
            return Pair.of(Optional.of("Backend error"), 0);
        }
    }
}
