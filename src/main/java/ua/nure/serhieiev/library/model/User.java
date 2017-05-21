package ua.nure.serhieiev.library.model;

import java.time.LocalDate;

public class User implements Identified{

    private Integer id;
    private String email;
    private String name;
    private String password;
    private String authToken;
    private String activationToken;
    private String resetPasswordToken;
    private Boolean enabled;
    private LocalDate registrationDate;
    private LocalDate lastVisit;
    private Role role;

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getAuthToken() {
        return authToken;
    }

    public User setAuthToken(String authToken) {
        this.authToken = authToken;
        return this;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public User setActivationToken(String activationToken) {
        this.activationToken = activationToken;
        return this;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public User setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public User setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public User setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public LocalDate getLastVisit() {
        return lastVisit;
    }

    public User setLastVisit(LocalDate lastVisit) {
        this.lastVisit = lastVisit;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", authToken='" + authToken + '\'' +
                ", activationToken='" + activationToken + '\'' +
                ", resetPasswordToken='" + resetPasswordToken + '\'' +
                ", enabled=" + enabled +
                ", registrationDate=" + registrationDate +
                ", lastVisit=" + lastVisit +
                ", role=" + role +
                '}';
    }

    public enum Role {

        GUEST, READER, LIBRARIAN, ADMIN;

        public String value() {
            return this.name().toLowerCase();
        }

    }

}
