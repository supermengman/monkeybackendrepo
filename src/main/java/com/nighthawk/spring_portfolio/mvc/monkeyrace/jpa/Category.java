package com.nighthawk.spring_portfolio.mvc.monkeyrace.jpa;

import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
   // id
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

    String name;
}
