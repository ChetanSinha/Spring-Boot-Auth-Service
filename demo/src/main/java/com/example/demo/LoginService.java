package com.example.demo;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  JwtService jwtService;

  public UserDTO login(String username, String password) {
    // get user details
    UserEntity userEntity = userRepository.findByUsername(username).orElse(null);
    if (userEntity == null) {
      userEntity = UserEntity.builder()
          .name(username)
          .username(username)
          .password(password)
          .build();
      userEntity = userRepository.save(userEntity);
    }
    // generate AT token from user id
    String token = jwtService.generate(userEntity.getId());

    userEntity.setToken(token);
    // insert token in user
    userEntity = userRepository.save(userEntity);

    return UserDTO.builder().userId(userEntity.getId()).AT(token).build();
  }


  public Integer validate(HttpServletRequest httpServletRequest) {

    // get AT cookie
    String atCookie = getATCookie(httpServletRequest);
    DecodedJWT decodedJWT;

    // decode JWT token
    decodedJWT = jwtService.validate(atCookie);


    // get userId that we've set while creating AT token
    Map<String, String> mp = decodedJWT.getClaims().entrySet().stream()
        .filter(claim -> "userId".contains(claim.getKey()))
        .collect(Collectors.toMap(Entry::getKey
            , entry -> Objects.toString(entry.getValue().as(Object.class))));

    if (mp.containsKey("userId")) {
      // return userId that was decoded
      return Integer.parseInt(mp.get("userId"));
    }
    // throw forbidden error
    throw new RuntimeException("Forbidden");
  }

  public String getATCookie(HttpServletRequest httpServletRequest) {
    // gets AT cookie by analysis the cookie
    Cookie[] cookies = httpServletRequest.getCookies();
    if (cookies.length < 1) {
      throw new RuntimeException("Cookie not found");
    }
    Map<String, Object> map = new HashMap<>();
    for (Cookie c : cookies) {
      map.put(c.getName(), c.getValue());
    }
    return (String) map.get("AT");
  }
}
