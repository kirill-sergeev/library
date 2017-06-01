package ua.nure.serhieiev.library.controller.util;

import static ua.nure.serhieiev.library.controller.util.Alert.Type.*;

public enum Alert {
    /**
     * Alerts for registration page
     */
    EMAIL_ALREADY_IN_USE(WARNING),
    REGISTRATION_SUCCESSFUL(SUCCESS),
    /**
     * Alerts for login page
     */
    BAD_LOGIN_OR_PASSWORD(WARNING),
    ACTIVATION_SUCCESSFUL(SUCCESS),
    /**
     * Alerts for reset-password page
     */
    WRONG_EMAIL(WARNING),
    WRONG_TOKEN(WARNING),
    PASSWORD_RESET_SUCCESSFUL(SUCCESS),
    /**
     * Alerts for change-password page
     */
    PASSWORD_NOT_CHANGED(WARNING),
    PASSWORD_CHANGED_SUCCESSFUL(SUCCESS),
    /**
     * Alerts for users page
     */
    USER_UNBLOCKED(SUCCESS),
    USER_BLOCKED(SUCCESS),
    USER_REMOVED(SUCCESS),
    /**
     * Alerts for search action
     */
    NOT_FOUND(WARNING),
    /**
     * Alerts for add book page
     */
    BOOK_ADDED(SUCCESS),
    BOOK_NOT_ADDED(WARNING),
    /**
     * Alerts for change book page
     */
    BOOK_CHANGED(SUCCESS),
    BOOK_NOT_CHANGED(WARNING),
    AVAILABLE_MORE_THAN_QUANTITY(WARNING),
    /**
     * Alerts for cart page
     */
    ACCOUNT_BLOCKED_CART(WARNING),
    ACCOUNT_BLOCKED_ORDER(WARNING),
    BOOK_ALREADY_IN_CART(WARNING),
    BOOK_NOT_AVAILABLE(WARNING),
    BOOK_NOT_IN_CART(WARNING),
    HAVE_THIS_BOOK(WARNING),
    LIMIT_BOOKS(WARNING),
    ORDER_IS_EMPTY(WARNING),
    ORDER_CREATED(SUCCESS);

    private final String description;
    private final Type type;

    Alert(Type type) {
        this.type = type;
        this.description = String.format("%s.%s.%s",
                this.getClass().getSimpleName().toLowerCase(),
                type.name().toLowerCase(),
                this.name().toLowerCase()) ;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type.getName();
    }


    public enum Type {

        SUCCESS("positive"), WARNING("negative");

        private String name;

        Type(String type) {
            this.name = type;
        }

        public String getName() {
            return name;
        }
    }

}