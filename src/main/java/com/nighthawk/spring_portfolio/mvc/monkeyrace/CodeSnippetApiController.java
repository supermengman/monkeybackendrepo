package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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
public class CodeSnippetApiController {
    @Autowired
    CodeSnippetJpaRepository codeSnippetJpaRepository;
    
    @Autowired
    PersonJpaRepository personJpaRepository;

    @Autowired
    LoginHandler handler;

    @Autowired
    LevelJpaRepository levelJpaRepository;
    
    @PostMapping("/createSnippet")
    public ResponseEntity<Object> createSnippet(@RequestBody final Map<String, Object> map, @CookieValue("jwt") String jwt) {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account Does Not Exist");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        Level level = levelJpaRepository.findById((long) map.get("level"));
        Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, level);
        if (optional.isPresent()) {
            CodeSnippet snippet = optional.get();
            snippet.setSnippet((String) map.get("code"));
            codeSnippetJpaRepository.save(snippet);

            Map<String, Object> resp = new HashMap<>();
            resp.put("err", false);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        String code = (String) map.get("code");
        CodeSnippet snippet = new CodeSnippet();
        snippet.setSnippet(code);
        snippet.setPerson(p);

        
        snippet.setLevel(level);
        snippet.setStage(level.getStage());

        codeSnippetJpaRepository.save(snippet);

        Map<String, Object> resp = new HashMap<>();
        resp.put("err", false);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/getSnippet")
    public ResponseEntity<Object> getSnippet(@RequestBody final Map<String, Object> map, @CookieValue("jwt") String jwt) {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account Does Not Exist");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        Level level = levelJpaRepository.findById((long) map.get("level"));
        Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, level);
        if (optional.isPresent()) {
            CodeSnippet snippet = optional.get();
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", false);
            resp.put("code", snippet.getSnippet());
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "No Code Snippet Found");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    // mainly for testing
    @PostMapping("/frq_a_2018")
    public ResponseEntity<String> frq_a_2018(@RequestBody final Map<String, Object> map) {
        CodeSnippetRunner r = new CodeSnippetRunner(CodeSnippetRunner.Problem.FRQ_A_2018);
        return new ResponseEntity<String>(r.isCorrect((String)map.get("code")) ? "TRUE" : "FALSE", HttpStatus.OK);
    }
}