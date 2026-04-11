package edu.cit.lim.artspire.dto;

public class AuthResponse {

    private final boolean success;
    private final String message;
    private final String name;
    private final String email;

    public AuthResponse(boolean success, String message, String name, String email) {
        this.success = success;
        this.message = message;
        this.name = name;
        this.email = email;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
