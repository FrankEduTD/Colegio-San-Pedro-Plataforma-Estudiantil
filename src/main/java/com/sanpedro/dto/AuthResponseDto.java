package com.sanpedro.dto;

public class AuthResponseDto {
    
    private String token;
    private String username;
    private String rol;

    // Constructor
    public AuthResponseDto(String token, String username, String rol) {
        this.token = token;
        this.username = username;
        this.rol = rol;
    }

    // Getters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}