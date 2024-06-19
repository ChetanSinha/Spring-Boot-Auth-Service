package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Integer id;

  String username;

  String password;

  String name;
  String token;
}
