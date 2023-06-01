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
    CategoryJpaRepository categoryJpaRepository;

    @Autowired
    LoginHandler handler;

    @Autowired
    LevelJpaRepository levelJpaRepository;
    
    @PostMapping("/attemptLevel")
    public ResponseEntity<Object> attemptLevel(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt) {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account does not exist. Make sure you are logged in");
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

        // test the code
        CodeSnippetRunner runner = new CodeSnippetRunner(level.getProblem());
        var result = runner.testCode((String) map.get("code"));

        Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, level);
        if (optional.isPresent()) {
            CodeSnippet snippet = optional.get();
            snippet.setSnippet((String) map.get("code"));
            snippet.setError(result.getFirst().isPresent() ? result.getFirst().get() : null);
            snippet.setTestcasesPassed(result.getSecond());
            codeSnippetJpaRepository.save(snippet);
        }
        else {
            String code = (String) map.get("code");
            CodeSnippet snippet = new CodeSnippet();
            snippet.setSnippet(code);
            snippet.setPerson(p);
            snippet.setLevel(level);
            snippet.setError(result.getFirst().isPresent() ? result.getFirst().get() : null);
            snippet.setTestcasesPassed(result.getSecond());
            codeSnippetJpaRepository.save(snippet);
        }
        
        if (result.getFirst().isPresent()) {
            Map<String, Object> resp = new HashMap<>();

            resp.put("err", result.getFirst());

            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("msg", result.getSecond() + "/" + level.getTestcases() + " Testcases were passed.");
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/getSnippet")
    public ResponseEntity<Object> getSnippet(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt) {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account does not exist. Make sure you are logged in");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        Level level = levelJpaRepository.findByNumber((int) map.get("level"));
        Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, level);
        if (optional.isPresent()) {
            CodeSnippet snippet = optional.get();
            // Map<String, Object> resp = new HashMap<>();
            // resp.put("err", false);
            // resp.put("status", snippet.getError());
            // resp.put("code", snippet.getSnippet());
            return new ResponseEntity<>(snippet, HttpStatus.OK);
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("err", "No Code Snippet Found");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }



    @PostMapping("/getLevelList")
    public ResponseEntity<Object> getLevelList(@CookieValue(value="flashjwt", required=false) String jwt) {
        

        List<Level> levels = levelJpaRepository.findAllByOrderByNumberAsc();
        HashMap<Integer, Integer> levelStatus = new HashMap<Integer, Integer>();

        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            levelStatus = null;
        } else {
            for (Level l : levels) {

                Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, l);
                if (optional.isPresent()) {
                    CodeSnippet snippet = optional.get();
                    levelStatus.put(l.getNumber(), snippet.getTestcasesPassed() == null ? 0 : snippet.getTestcasesPassed());
                }
                else {
                    levelStatus.put(l.getNumber(), -1);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("levels", levels);
        result.put("status", levelStatus);
        
        List<Category> categories = categoryJpaRepository.findAll();
        result.put("categories", categories);

        result.forEach((key, value) -> System.out.println(key + ":" + value));

        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    @PostMapping("/getLevelsByCategory")
    public ResponseEntity<Object> getLevelsByCategory(@RequestBody final Map<String, Object> map, @CookieValue("flashjwt") String jwt) {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account does not exist. Make sure you are logged in");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        String category = (String) map.get("category");
        List<Level> levels = levelJpaRepository.findAllByCategoriesName(category);
        HashMap<Integer, String> levelStatus = new HashMap<Integer, String>();

        for (Level l : levels) {

            Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, l);
            if (optional.isPresent()) {
                CodeSnippet snippet = optional.get();
                if (snippet.getError() == null) {
                    levelStatus.put(l.getNumber(), "Complete");
                }
                else levelStatus.put(l.getNumber(), "Attempted");
            }
            else {
                levelStatus.put(l.getNumber(), "Not Attempted");
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("levels", levels);
        result.put("status", levelStatus);

        result.forEach((key, value) -> System.out.println(key + ":" + value));

        return new ResponseEntity<Object>(result, HttpStatus.OK);
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
            double a = 2;
            double b = 6;
            csv += (double) total * (a/b) + (double) extraCredit * 0.3;
            csv += "\n";
        }

        return new ResponseEntity<>(csv, HttpStatus.OK);
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
