package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/user")
public class PersonApiController {
    /*
     * #### RESTful API ####
     * Resource: https://spring.io/guides/gs/rest-service/
     */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private PersonJpaRepository repository;

    @Autowired
    private LevelJpaRepository levelRepository;

    /*
     * DELETE individual Person using ID
     */
    @DeleteMapping("/deletePerson")
    public ResponseEntity<Object> deletePerson(@RequestBody final Map<String, Object> map)
            throws NoSuchAlgorithmException {
        String name = (String) map.get("name");
        String password = (String) map.get("password");
        Optional<Person> optional = repository.findByName(name);
        if (optional.isPresent()) { // Good ID
            Person person = optional.get(); // value from findByID

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);

            if (!computedPasswordHash.equals(person.passwordHash)) {
                Map<String, Object> resp = new HashMap<>();
                resp.put("err", "Incorrect Password");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            }

            repository.deleteById(person.getId());

            Map<String, Object> resp = new HashMap<>();
            resp.put("err", false);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        // Bad ID
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "No account with this email");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    /*
     * POST Aa record by Requesting Parameters from URI
     */
    @PostMapping("/createPerson")
    public ResponseEntity<Object> postPerson(@RequestBody final Map<String, Object> map)
            throws NoSuchAlgorithmException {
        // check for existing person
        if (repository.findByName((String) map.get("name")).isPresent()) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Email already in use");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
        
        // check for password complexity requirements
        String password = (String) map.get("password");
        if (password.length() < 8 || !password.matches(".*[0-9]+.*") || !password.matches(".*[^A-Za-z0-9\\s].*")) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Password does not meet complexity requirements (length >= 8, contains number, contains special character)");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        // A person object WITHOUT ID will create a new record with default roles as
        // student
        Person person = new Person();
        person.setName((String) map.get("name"));

        // password hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        String computedPasswordHash = new String(encodedHash);
        person.setPasswordHash(computedPasswordHash);

        // level
        person.setLevel(levelRepository.findByNumber(0));

        repository.save(person);
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", false);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }
    
    

    // handles exceptions
    @ExceptionHandler({ ClassCastException.class, NullPointerException.class })
    public ResponseEntity<Object> handleBadUserInput() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Bad User Input");
        return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ MissingRequestCookieException.class })
    public ResponseEntity<Object> handleNoCookie() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Account doesn't exist");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
}