package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;

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

    @Autowired
    LoginHandler handler;
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

        repository.save(person);
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", false);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @PostMapping("/updateUsername")
    public ResponseEntity<Object> updatePerson(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt, HttpServletResponse response) throws NoSuchAlgorithmException {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account does not exist. Make sure you are logged in");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        String name = (String) map.get("name");

        if (name != null) {
            if (repository.findByName(name).isPresent()) {
                Map<String, Object> resp = new HashMap<>();
                resp.put("err", "Name already in use");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            }

            p.setName(name);

            

            repository.save(p);

            // update jwt and send to client
            String newJwt = handler.createJwt(p);
            Cookie cookie = new Cookie("flashjwt", newJwt);
            cookie.setPath("/");
            cookie.setMaxAge(1000 * 60 * 60);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            Map<String, Object> resp = new HashMap<>();
            resp.put("err", false);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        

        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Bad User Input");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/updatePassword")
    public ResponseEntity<Object> updatePassword(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt) throws NoSuchAlgorithmException {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account does not exist. Make sure you are logged in");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        String currentPassword = (String) map.get("currentPassword");
        String password = (String) map.get("password");

        if (password != null && currentPassword != null) {
            MessageDigest digestCurrent = MessageDigest.getInstance("SHA-256");
            byte[] encodedHashCurrent = digestCurrent.digest(
                    currentPassword.getBytes(StandardCharsets.UTF_8));
            String computedCurrentPasswordHash = new String(encodedHashCurrent);
            if (!computedCurrentPasswordHash.equals(p.passwordHash)) {
                Map<String, Object> resp = new HashMap<>();
                resp.put("err", "Incorrect Password");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            }

            if (password.length() < 8 || !password.matches(".*[0-9]+.*") || !password.matches(".*[^A-Za-z0-9\\s].*")) {
                Map<String, Object> resp = new HashMap<>();
                resp.put("err", "Password does not meet complexity requirements (length >= 8, contains number, contains special character)");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);
            p.setPasswordHash(computedPasswordHash);

            repository.save(p);
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", false);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Bad User Input");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);

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