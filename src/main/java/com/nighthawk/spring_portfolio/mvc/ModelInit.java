package com.nighthawk.spring_portfolio.mvc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.Category;
import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.CategoryJpaRepository;
import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.Level;
import com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa.LevelJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

@Component // Scans Application for ModelInit Bean, this detects CommandLineRunner
public class ModelInit {  
    @Autowired LevelJpaRepository repository;
    @Autowired CategoryJpaRepository categoryRepository;

    @Bean
    CommandLineRunner run() {  // The run() method will be executed after the application starts
        return args -> {

            String[] categories = {"2D Arrays", "Arrays"};
            Map<String, Category> categoryMap = new HashMap<>();

            for (String c : categories) {
                Optional<Category> copt = categoryRepository.findByName(c);
                if (copt.isPresent()) {
                    categoryMap.put(c, copt.get());
                }
                else {
                    Category cat = new Category(null, c);
                    categoryRepository.save(cat);
                    categoryMap.put(c, cat);
                }
            }

            Yaml yaml = new Yaml();
            InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("../resources/Problems.yaml");
            
            
            List<Level> levels = new ArrayList<>();
            for (Object data : yaml.loadAll(inputStream)) {
                Map<String, Object> map = (Map<String, Object>) data;
                String name = (String) map.get("name");
                int number = (int) map.get("number");
                String description = (String) map.get("description");
                String problem = (String) map.get("problem");
                List<String> categoriesList = (List<String>) map.get("categories");
                List<Category> categoryList = new ArrayList<>();
                for (String c : categoriesList) {
                    categoryList.add(categoryMap.get(c));
                }
                int testcases = (int) map.get("testcases");
                Level l = new Level(null, name, number, categoryList, description, problem, testcases);
                levels.add(l);
            }
            


            // Level[] levels = {
            //     new Level(null, "Level 0", 0, List.of(categoryMap.get("2D Arrays")), "This is the 2018 FRQ Part A", "2018FRQA.javat"),
            //     new Level(null, "Level 1", 1, List.of(categoryMap.get("2D Arrays")), "This is the 2018 FRQ Part B", "2018FRQB.javat"),
            //     new Level(null, "Level 2", 2, List.of(categoryMap.get("2D Arrays"), categoryMap.get("Arrays")), "This is the 2017 FRQ #4 Part A", "2017FRQ4A.javat"),
            //     new Level(null, "Level 3", 3 ,List.of(categoryMap.get("2D Arrays"), categoryMap.get("Arrays")), "This is the 2017 FRQ #4 Part B", "2017FRQ4B.javat"),
            //     new Level(null, "Level 4", 4, List.of(categoryMap.get("2D Arrays")), "This is the 2016 FRQ Part A", "2016FRQA.javat"),
            //     new Level(null, "Level 5", 5, List.of(categoryMap.get("2D Arrays")), "This is the 2016 FRQ Part B", "2016FRQB.javat"),
            //     new Level(null, "Level 6", 6, List.of(categoryMap.get("2D Arrays")), "This is the extra credit challenge", "ExtraCreditArrays.javat"),
            //     new Level(null, "Completion", Level.DUMMY_LEVEL, new ArrayList<>(), "This is a dummy level", null),
            // };

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

