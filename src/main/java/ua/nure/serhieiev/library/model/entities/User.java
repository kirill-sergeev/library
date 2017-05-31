package ua.nure.serhieiev.library.model.entities;

import java.time.LocalDate;

public class User implements Identified{

    private static final long serialVersionUID = 3761971688699895147L;

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

    /**
     * The user ID is unique for each User. So this should compare User by ID only.
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof User) && (id != null)
                ? id.equals(((User) other).id)
                : (other == this);
    }

    /**
     * The user ID is unique for each User. So User with same ID should return same hashcode.
     */
    @Override
    public int hashCode() {
        return (id != null)
                ? (this.getClass().hashCode() + id.hashCode())
                : super.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
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
