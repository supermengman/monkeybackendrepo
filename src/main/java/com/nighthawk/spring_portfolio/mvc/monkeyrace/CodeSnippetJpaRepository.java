package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.sqlite.core.Codes;

import java.util.List;
import java.util.Optional;

/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methods
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
public interface CodeSnippetJpaRepository extends JpaRepository<CodeSnippet, Long> {

    // Finds by person
    List<CodeSnippet> findByPerson(Person person);

    // Finds by person and level
    Optional<CodeSnippet> findByPersonAndLevel(Person person, Level level);
}