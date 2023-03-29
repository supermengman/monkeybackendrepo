package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StageJpaRepository extends JpaRepository<Stage, Long> {
    Stage findByNumber(int number);
    Stage findById(long id);
}