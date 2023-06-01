package com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findAll();
}
