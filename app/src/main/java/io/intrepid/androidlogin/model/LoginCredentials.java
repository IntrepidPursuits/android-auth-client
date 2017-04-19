package io.intrepid.androidlogin.model;

public class LoginCredentials {

    @SuppressWarnings("unused")
    public String email;
    @SuppressWarnings("unused")
    public String password;

    public LoginCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
