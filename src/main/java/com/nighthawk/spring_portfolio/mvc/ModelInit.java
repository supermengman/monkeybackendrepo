package com.nighthawk.spring_portfolio.mvc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.Level;
import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.LevelJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;

@Component // Scans Application for ModelInit Bean, this detects CommandLineRunner
public class ModelInit {  
    @Autowired LevelJpaRepository repository;

    @Bean
    CommandLineRunner run() {  // The run() method will be executed after the application starts
        return args -> {

            Level l = new Level()
            Level[] levels = {
                new Level(null, "Level 0", 0, new ArrayList<>(), "This is the 2018 FRQ Part A", "2018FRQA.javat"),
                new Level(null, "Level 1", 1, new ArrayList<>(), "This is the 2018 FRQ Part B", "2018FRQB.javat"),
                new Level(null, "Level 2", 2, new ArrayList<>(), "This is the 2017 FRQ #4 Part A", "2017FRQ4A.javat"),
                new Level(null, "Level 3", 3 ,new ArrayList<>(), "This is the 2017 FRQ #4 Part B", "2017FRQ4B.javat"),
                new Level(null, "Level 4", 4, new ArrayList<>(), "This is the 2016 FRQ Part A", "2016FRQA.javat"),
                new Level(null, "Level 5", 5, new ArrayList<>(), "This is the 2016 FRQ Part B", "2016FRQB.javat"),
                new Level(null, "Level 6", 6, new ArrayList<>(), "This is the extra credit challenge", "ExtraCreditArrays.javat"),
                new Level(null, "Completion", Level.DUMMY_LEVEL, new ArrayList<>(), "This is a dummy level", null),
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

