package org.demo.employeedetails.controller;

import org.demo.employeedetails.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        try {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (user == null) return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials"));
            List<String> roles = user.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
            String token = jwtUtil.generateToken(username, roles);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }
}
