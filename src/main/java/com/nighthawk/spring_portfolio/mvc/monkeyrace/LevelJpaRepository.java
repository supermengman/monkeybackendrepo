package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelJpaRepository extends JpaRepository<Level, Long> {
    List<Level> findByNumberAndStage(int number, Stage stage);
    Level findById(long id);
    List<Level> findByStage(Stage stage);
}