package com.nighthawk.spring_portfolio.mvc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.nighthawk.spring_portfolio.mvc.monkeyrace.Problem;
import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.Level;
import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.LevelJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Component // Scans Application for ModelInit Bean, this detects CommandLineRunner
public class ModelInit {  
    @Autowired LevelJpaRepository repository;

    @Bean
    CommandLineRunner run() {  // The run() method will be executed after the application starts
        return args -> {

            Level[] levels = {
                new Level(null, "Level 0", 0, Problem.FRQ_A_2018.getName()),
                new Level(null, "Level 1", 1, Problem.FRQ_B_2018.getName()),
                new Level(null, "Completion", Level.DUMMY_LEVEL, null),
            };

            for (Level level : levels) {
                Level oldLevel = repository.findByNumber(level.getNumber());
                if (oldLevel == null) {
                    repository.save(level);
                } else {
                    level.setId(oldLevel.getId());
                    repository.save(level);
                }
            }

        };
    }
}

