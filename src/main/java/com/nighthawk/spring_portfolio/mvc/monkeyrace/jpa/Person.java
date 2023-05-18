package com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // For roles
    // @ManyToMany(fetch = FetchType.EAGER)
    // private Collection<PersonRole> roles = new ArrayList<>();

    @NotEmpty
    @Size(min = 5)
    @Column(unique = true)
    String name;

    // to ensure protection other classes need to redact this
    public String passwordHash;

    public static void main(String[] args) {
        Person p = new Person();
        p.setName("Yipee");
        p.setPasswordHash("password");

        System.out.println(p.toString());
    }
}
