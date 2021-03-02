package br.com.italoschramm.bibilinguo.model.rest;

public class UserReturn {

    private long id;

    private String name;

    private String email;

    private boolean active;

    private String[] authorizations;

    private String userImage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String[] getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(String[] authorizations) {
        this.authorizations = authorizations;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
