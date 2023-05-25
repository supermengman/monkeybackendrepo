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
public class CodeSnippet {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // For roles
    // @ManyToMany(fetch = FetchType.EAGER)
    // private Collection<PersonRole> roles = new ArrayList<>();

    @NotEmpty
    String snippet;

    @ManyToOne
    private Person person;

    @ManyToOne(fetch = FetchType.EAGER)
    private Level level;

    // IF "null" this code snippet at least reached testcases
    private String error;

    private int testcasesPassed;

    public static void main(String[] args) {
        CodeSnippet c = new CodeSnippet();

        c.setSnippet("if (a > b) { return a; } else { return b; }");

        System.out.println(c.toString());
    }
}
