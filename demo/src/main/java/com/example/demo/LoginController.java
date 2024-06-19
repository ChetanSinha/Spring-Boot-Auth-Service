package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  @Autowired
  private LoginService loginService;


  @GetMapping("/validate")
  public ResponseEntity<ValidatedResponse> validate(
      HttpServletRequest httpServletRequest
  ) {
    // controller to validate if AT generated is correct
    try {
      Integer userID = loginService.validate(httpServletRequest);
      return ResponseEntity.status(HttpStatus.OK).body(ValidatedResponse.builder()
          .userId(userID)
          .build());
    } catch (Exception e) { // we can use custom exception to specifically handle forbidden case
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }


  @GetMapping("/login")
  public ResponseEntity<Void> login(
      @RequestParam String username,
      @RequestParam String password
  ) {
    // controller to login and generate AT and set cookie
    UserDTO userDTO = loginService.login(username, password);
    HttpHeaders headers = new HttpHeaders();

    // create cookie to set in response header
    ResponseCookie cookie = ResponseCookie.from("AT", userDTO.getAT())
        .httpOnly(false)
        .secure(true)
        .path("/")
        .maxAge(24 * 60 * 60)
        .domain("demo.com")
        .build();

    headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
    return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
  }
}
