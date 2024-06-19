package com.example.demo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {
  Integer userId;
  String AT;
}
