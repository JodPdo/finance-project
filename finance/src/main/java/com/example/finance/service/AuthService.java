package com.example.finance.service;

import com.example.finance.model.User;
import com.example.finance.config.JwtUtil;
import com.example.finance.model.AuthRequest;
import com.example.finance.model.AuthResponse;
import com.example.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class AuthService {

    // เพิ่ม @Autowired ก่อน
@Autowired
private JwtUtil jwtUtil;



    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public AuthResponse signup(AuthRequest request) {
    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    
    // เข้ารหัสรหัสผ่านก่อนบันทึก
    String encodedPassword = passwordEncoder.encode(request.getPassword());
    user.setPassword(encodedPassword);
    
    userRepository.save(user);

    String token = jwtUtil.generateToken(user.getEmail()); // ใช้ email

    return new AuthResponse(token);
}


    public AuthResponse login(AuthRequest request) {
    User user = userRepository.findByEmail(request.getEmail());
    if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid credentials");
    }

    String token = jwtUtil.generateToken(user.getEmail()); // ใช้ email

    return new AuthResponse(token);
}

}
