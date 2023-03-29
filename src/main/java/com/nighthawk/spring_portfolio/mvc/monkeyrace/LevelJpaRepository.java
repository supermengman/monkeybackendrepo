package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelJpaRepository extends JpaRepository<Stage, Long> {
    List<Stage> findByNumberAndStage(int number, Stage stage);
    Stage findById(long id);
    List<Stage> findByStage(Stage stage);
}