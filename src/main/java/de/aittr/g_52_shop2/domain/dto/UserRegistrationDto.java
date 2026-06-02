package de.aittr.g_52_shop2.domain.dto;

public class UserRegistrationDto {

    private String email;
    private String password;
    private String name;

    public UserRegistrationDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User registration DTO: email - %s, name - %s", email, name);
    }
}
