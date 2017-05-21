package ua.nure.serhieiev.library.controller.util;

public enum Alert {

    WRONG_EMAIL("alert.warning.wrong_email", Type.WARNING),
    WRONG_TOKEN("alert.warning.wrong_token", Type.WARNING),
    BAD_LOGIN_OR_PASSWORD("alert.warning.bad_login_or_password", Type.WARNING),
    EMAIL_ALREADY_IN_USE("alert.warning.email_already_in_use", Type.WARNING),
    PASSWORD_NOT_CHANGED("alert.warning.password_not_changed", Type.WARNING),

    REGISTRATION_SUCCESSFUL("alert.success.registration_successful", Type.SUCCESS),
    ACTIVATION_SUCCESSFUL("alert.success.activation_successful", Type.SUCCESS),
    PASSWORD_RESET_SUCCESSFUL("alert.success.reset_successful", Type.SUCCESS),
    PASSWORD_CHANGED_SUCCESSFUL("alert.success.password_changed_successful", Type.SUCCESS),

    USER_UNBLOCKED("alert.success.user_unblocked", Type.SUCCESS),
    USER_BLOCKED("alert.success.user_blocked", Type.SUCCESS),
    USER_REMOVED("alert.success.user_removed", Type.SUCCESS);


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

        SUCCESS("alert-success"), DANGER("alert-danger"), INFO("alert-info"), WARNING("alert-warning");

        private String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}