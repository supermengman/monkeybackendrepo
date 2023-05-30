package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code")
public class LevelApiController {
    @Autowired
    CodeSnippetJpaRepository codeSnippetJpaRepository;
    
    @Autowired
    PersonJpaRepository personJpaRepository;

    @Autowired
    LoginHandler handler;

    @Autowired
    LevelJpaRepository levelJpaRepository;

    @PostMapping("/getLevel")
    public ResponseEntity<Level> getLevel(@RequestBody Map<String, String> body) {
        int number = Integer.parseInt(body.get("number"));
        Level level = levelJpaRepository.findByNumber(number);
        Map<String, Object> resp = new HashMap<>();
        resp.put("level", level);

        // return description from file
        String descriptionFile = level.getDescription();
        Path filePath = Path.of("descriptions/" + descriptionFile);
        
        String description = "";
        try {
            description = Files.readString(filePath);
        } catch (Exception e) {
            System.out.println("Exception: " + e + ", " + e.getStackTrace());
        }

        resp.put("description", description);

        return new ResponseEntity(resp, HttpStatus.OK);
    }

}