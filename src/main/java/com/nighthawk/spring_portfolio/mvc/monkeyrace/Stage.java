package com.nighthawk.spring_portfolio.mvc.monkeyrace;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Stage {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int number;

    public static void main(String[] args) {
        Stage s = new Stage();
        s.setId(10l);
        s.setName("Bubble Sort");
        s.setNumber(1);

        System.out.println(s);
    }
}
