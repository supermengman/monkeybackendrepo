package com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Level {
    // highest level
    public static int DUMMY_LEVEL = 100;

    // id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private int number;

    @OneToMany
    private List<Category> categories = new ArrayList<>();

    private String problem;

    public static void main(String[] args) {
        Level l = new Level();
        l.setId(10l);
        l.setName("What is sorting?");
        l.setNumber(1);
        System.out.println(l);
    }
}
