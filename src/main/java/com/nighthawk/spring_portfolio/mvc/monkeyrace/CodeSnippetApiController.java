package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.*;

import java.nio.charset.StandardCharsets;
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
public class CodeSnippetApiController {
    @Autowired
    CodeSnippetJpaRepository codeSnippetJpaRepository;
    
    @Autowired
    PersonJpaRepository personJpaRepository;

    @Autowired
    LoginHandler handler;

    @Autowired
    LevelJpaRepository levelJpaRepository;
    
    @PostMapping("/attemptLevel")
    public ResponseEntity<Object> attemptLevel(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt) {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account Does Not Exist");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        int levelNumber = (int) map.get("levelNumber");
        Level level = levelJpaRepository.findByNumber(levelNumber);
        if (level == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Level Does Not Exist");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
        // Level level = p.getLevel();

        // TODO: don't need this cuz now level is set
        if (level.getNumber() == Level.DUMMY_LEVEL) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "You completed the game!");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST); 
        }

        // test the code
        CodeSnippetRunner runner = new CodeSnippetRunner(level.getProblemEnum());
        Optional<String> result = runner.isCorrect((String) map.get("code"));

        Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, level);
        if (optional.isPresent()) {
            CodeSnippet snippet = optional.get();
            snippet.setSnippet((String) map.get("code"));
            snippet.setError(result.isPresent() ? result.get() : null);
            codeSnippetJpaRepository.save(snippet);

            // succeeded
            if (!result.isPresent()) {
                // TODO: could delete?
                Level newLevel = levelJpaRepository.findByNumber(level.getNumber() + 1);
                if (newLevel != null) p.setLevel(newLevel);
                else p.setLevel(levelJpaRepository.findByNumber(Level.DUMMY_LEVEL));
                personJpaRepository.save(p);
            }
            else {
                Map<String, Object> resp = new HashMap<>();
                resp.put("err", result.get());
                return new ResponseEntity<>(resp, HttpStatus.PAYMENT_REQUIRED);
            }
        }
        else {
            String code = (String) map.get("code");
            CodeSnippet snippet = new CodeSnippet();
            snippet.setSnippet(code);
            snippet.setPerson(p);
            snippet.setLevel(level);
            snippet.setError(result.isPresent() ? result.get() : null);
            codeSnippetJpaRepository.save(snippet);

            // succeeded
            if (!result.isPresent()) {
                Level newLevel = levelJpaRepository.findByNumber(level.getNumber() + 1);
                if (newLevel != null) p.setLevel(newLevel);
                else p.setLevel(levelJpaRepository.findByNumber(Level.DUMMY_LEVEL));
                personJpaRepository.save(p);
            }
            else {
                Map<String, Object> resp = new HashMap<>();
                resp.put("err", result.get());
                return new ResponseEntity<>(resp, HttpStatus.PAYMENT_REQUIRED);
            }
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("err", false);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/setLevel")
    public ResponseEntity<Object> setLevel(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt) {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account Does Not Exist");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        Level level = levelJpaRepository.findByNumber((int) map.get("level"));
        if (level == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Level Does Not Exist");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        p.setLevel(level);
        personJpaRepository.save(p);

        Map<String, Object> resp = new HashMap<>();
        resp.put("err", false);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/getSnippet")
    public ResponseEntity<Object> getSnippet(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt) {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account Does Not Exist");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        Level level = levelJpaRepository.findByNumber((int) map.get("level"));
        Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, level);
        if (optional.isPresent()) {
            CodeSnippet snippet = optional.get();
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", false);
            resp.put("status", snippet.getError());
            resp.put("code", snippet.getSnippet());
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "No Code Snippet Found");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    // @PostMapping("/data.csv")
    // public ResponseEntity<Object> getData(@RequestBody final Map<String, Object> map) {
    //     String key = (String) map.get("key");
    //     if (!key.equals(System.getenv("ADMIN_KEY"))) {
    //         return new ResponseEntity<>("You are not authorized", HttpStatus.UNAUTHORIZED);
    //     }

    //     String csv = "Name,Level\n";
    //     List<Person> persons = personJpaRepository.findAll();
    //     for (Person p : persons) {
    //         csv += p.getName() + "," + p.getLevel().getNumber() + "\n";
    //     }

    //     return new ResponseEntity<>(csv, HttpStatus.OK);
    // }

    @PostMapping("/getLevelList")
    public ResponseEntity<Object> getLevelList(@CookieValue("flashjwt") String jwt) {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account Does Not Exist");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        List<Level> levels = levelJpaRepository.findAllByOrderByNumberAsc();
        HashMap<Integer, String> levelStatus = new HashMap<Integer, String>();

        for (Level l : levels) {
            if (l.getNumber() == Level.DUMMY_LEVEL) continue;

            Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, l);
            if (optional.isPresent()) {
                CodeSnippet snippet = optional.get();
                if (snippet.getError() == null) {
                    levelStatus.put(l.getNumber(), "Complete");
                }
                else levelStatus.put(l.getNumber(), "Attempted");
            }
            else {
                levelStatus.put(l.getNumber(), "Not Attempted");;
            }
        }

        return new ResponseEntity<Object>(levelStatus, HttpStatus.OK);
    }

    @PostMapping("/data.csv")
    public ResponseEntity<Object> getData(@RequestBody final Map<String, Object> map) {
        String key = (String) map.get("key");
        if (!key.equals(System.getenv("ADMIN_KEY"))) {
            return new ResponseEntity<>("You are not authorized", HttpStatus.UNAUTHORIZED);
        }

        List<Person> persons = personJpaRepository.findAll();
        List<Level> levels = levelJpaRepository.findAllByOrderByNumberAsc();

        String csv = "Name,";
        for (Level l : levels) {
            csv += l.getName() + ",";
        }
        csv += "total";

        csv += "\n";

        for (Person p : persons) {
            int total = 0;
            csv += p.getName() + ",";
            for (Level l : levels) {
                Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, l);
                if (optional.isPresent()) {
                    CodeSnippet snippet = optional.get();
                    if (snippet.getError() == null) {
                        csv += "1,";
                        total++;
                    }
                    else csv += "0,";
                }
                else {
                    csv += "0,";
                }
            }
            csv += total;
            csv += "\n";
        }

        return new ResponseEntity<>(csv, HttpStatus.OK);
    }

    @PostMapping("/datascaled.csv")
    public ResponseEntity<Object> getDataScaled(@RequestBody final Map<String, Object> map) {
        String key = (String) map.get("key");
        if (!key.equals(System.getenv("ADMIN_KEY"))) {
            return new ResponseEntity<>("You are not authorized", HttpStatus.UNAUTHORIZED);
        }

        List<Person> persons = personJpaRepository.findAll();
        List<Level> levels = levelJpaRepository.findAllByOrderByNumberAsc();

        String csv = "Name,";
        for (Level l : levels) {
            csv += l.getName() + ",";
        }
        csv += "scaled score";

        csv += "\n";

        for (Person p : persons) {
            int total = 0;
            int extraCredit = 0;
            csv += p.getName() + ",";
            for (Level l : levels) {
                Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, l);
                if (optional.isPresent()) {
                    CodeSnippet snippet = optional.get();
                    if (snippet.getError() == null) {
                        csv += "1,";
                        if (l.getNumber() != 6) {
                            total++;
                        } else {
                            extraCredit++;
                        }
                    }
                    else csv += "0,";
                }
                else {
                    csv += "0,";
                }
            }
            csv += total * 2/6 + extraCredit * 0.3;
            csv += "\n";
        }

        return new ResponseEntity<>(csv, HttpStatus.OK);
    }

    // mainly for testing
    @PostMapping("/frq_a_2018")
    public ResponseEntity<String> frq_a_2018(@RequestBody final Map<String, Object> map) {
        CodeSnippetRunner r = new CodeSnippetRunner(Problem.FRQ_A_2018);
        Optional<String> result = r.isCorrect((String)map.get("code"));
        return new ResponseEntity<String>(result.isPresent() ? result.get() : "PASSED", HttpStatus.OK);
    }

    // handles exceptions
    @ExceptionHandler({ ClassCastException.class, NullPointerException.class })
    public ResponseEntity<Object> handleBadUserInput() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "Bad User Input");
        return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }
}