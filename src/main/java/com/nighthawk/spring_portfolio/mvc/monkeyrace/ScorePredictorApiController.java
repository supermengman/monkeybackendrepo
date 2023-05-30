package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
@RequestMapping("/api/predictor")
public class ScorePredictorApiController extends PredictionRunner {
    @Autowired
    CodeSnippetJpaRepository codeSnippetJpaRepository;
    
    @Autowired
    PersonJpaRepository personJpaRepository;

    @Autowired
    LoginHandler handler;

    @Autowired
    LevelJpaRepository levelJpaRepository;
    
    @PostMapping("/apscore")
    public ResponseEntity<Object> getLevelList(@CookieValue("flashjwt") String jwt) {
        Person p = handler.decodeJwt(jwt);
        if (p == null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("err", "Account does not exist. Make sure you are logged in");
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        List<Level> levels = levelJpaRepository.findAllByOrderByNumberAsc();
        ArrayList<Integer> levelStatus = new ArrayList<Integer>();

        for (Level l : levels) {

            Optional<CodeSnippet> optional = codeSnippetJpaRepository.findByPersonAndLevel(p, l);
            if (optional.isPresent()) {
                CodeSnippet snippet = optional.get();
                levelStatus.set(l.getNumber(), snippet.getTestcasesPassed() == null ? 0 : snippet.getTestcasesPassed());
            }
            else {
                levelStatus.set(l.getNumber(), -1);
            }
        }

        int attribute1 = levelStatus.get(1);
        int attribute2 = levelStatus.get(2);
        int attribute3 = levelStatus.get(3);      
        
        int score = runPythonScript(attribute1, attribute2, attribute3);

        return new ResponseEntity<Object>(score, HttpStatus.OK);
    }
}