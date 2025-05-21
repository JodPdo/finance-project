package com.example.finance.model;

public class AuthRequest {
    private String username; // ✅ เพิ่มบรรทัดนี้
    private String email;
    private String password;

    public AuthRequest() {}

    public String getUsername() {      // ✅ เพิ่ม getter/setter
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


