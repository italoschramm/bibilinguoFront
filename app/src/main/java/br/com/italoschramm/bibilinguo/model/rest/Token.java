package br.com.italoschramm.bibilinguo.model.rest;

public class Token {

    private String token;

    private UserReturn user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserReturn getUser() {
        return user;
    }

    public void setUser(UserReturn user) {
        this.user = user;
    }
}
