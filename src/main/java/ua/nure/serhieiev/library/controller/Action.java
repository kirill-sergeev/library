package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.model.User;

import java.util.Arrays;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;
import static ua.nure.serhieiev.library.model.User.Role.*;

public enum Action{

    REGISTER(REGISTER_ACTION, GUEST),

    LOGIN(LOGIN_ACTION, GUEST),

    ACTIVATE_ACCOUNT(ACTIVATE_ACTION, GUEST),

    RESET_PASSWORD(RESET_ACTION, GUEST),

    LOGOUT(LOGOUT_ACTION, READER, MANAGER, ADMIN);

    private String path;
    private User.Role[] allowedUsers;
    Action(String path, User.Role... allowedUsers) {
        this.path = path;
        this.allowedUsers = allowedUsers;
    }

    public String getPath() {
        return path;
    }

    public User.Role[] getAllowedUsers() {
        return Arrays.copyOf(allowedUsers, allowedUsers.length);
    }

    @Override
    public String toString() {
        return "Action{" +
                "path='" + path + '\'' +
                "} " + super.toString();
    }

    public static class Constants {
        public static final String REGISTER_ACTION = "/register.do";
        public static final String LOGIN_ACTION = "/login.do";
        public static final String ACTIVATE_ACTION = "/activate.do";
        public static final String RESET_ACTION = "/reset.do";
        public static final String LOGOUT_ACTION = "/logout.do";
        public static final String MAIN_ACTION = "/";
        public static final String USER_LIST_ACTION = "/admin/users.do";
        public static final String BOOK_LIST_ACTION = "/books.do";
        public static final String USER_PROFILE_ACTION = "/user.do";
        public static final String GENRE_LIST_ACTION = "/genres.do";

        private Constants() {
        }
    }

}