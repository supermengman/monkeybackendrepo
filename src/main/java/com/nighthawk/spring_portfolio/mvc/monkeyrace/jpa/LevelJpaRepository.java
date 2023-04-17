package com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelJpaRepository extends JpaRepository<Level, Long> {
    List<Level> findByNumber(int number);
    Level findById(long id);
}