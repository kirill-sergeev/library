package ua.nure.serhieiev.library.controller.util;

import static ua.nure.serhieiev.library.controller.util.Alert.Type.*;

public enum Alert {

    /**
     * Alerts for registration page
     */
    EMAIL_ALREADY_IN_USE("alert.warning.email_already_in_use", WARNING),
    REGISTRATION_SUCCESSFUL("alert.success.registration_successful", SUCCESS),
    /**
     * Alerts for login page
     */
    BAD_LOGIN_OR_PASSWORD("alert.warning.bad_login_or_password", WARNING),
    ACTIVATION_SUCCESSFUL("alert.success.activation_successful", SUCCESS),
    /**
     * Alerts for reset-password page
     */
    WRONG_EMAIL("alert.warning.wrong_email", WARNING),
    WRONG_TOKEN("alert.warning.wrong_token", WARNING),
    PASSWORD_RESET_SUCCESSFUL("alert.success.reset_successful", SUCCESS),
    /**
     * Alerts for change-password page
     */
    PASSWORD_NOT_CHANGED("alert.warning.password_not_changed", WARNING),
    PASSWORD_CHANGED_SUCCESSFUL("alert.success.password_changed_successful", SUCCESS),
    /**
     * Alerts for users page
     */
    USER_UNBLOCKED("alert.success.user_unblocked", SUCCESS),
    USER_BLOCKED("alert.success.user_blocked", SUCCESS),
    USER_REMOVED("alert.success.user_removed", SUCCESS),
    /**
     * Alerts for search action
     */
    NOT_FOUND("alert.warning.not_found", WARNING),
    /**
     * Alerts for add book page
     */
    BOOK_ADDED("alert.success.book_added", SUCCESS),
    BOOK_NOT_ADDED("alert.warning.book_not_added", WARNING);

    private final String description;
    private final Type type;

    Alert(String description, Type type) {
        this.description = description;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type.getType();
    }

    public enum Type {

        SUCCESS("positive"), DANGER("negative"), INFO("positive"), WARNING("negative");

        private String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}