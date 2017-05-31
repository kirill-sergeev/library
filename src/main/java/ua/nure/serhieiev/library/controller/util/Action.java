package ua.nure.serhieiev.library.controller.util;

import ua.nure.serhieiev.library.model.entities.User;

import java.util.Arrays;

import static ua.nure.serhieiev.library.controller.util.Action.Constants.*;
import static ua.nure.serhieiev.library.model.entities.User.Role.*;

public enum Action {

    ACTIVATE_ACCOUNT(ACTIVATE_ACTION, GUEST),
    AUTHOR_LIST(AUTHORS_ACTION, GUEST, READER, LIBRARIAN, ADMIN),
    BOOK_LIST(BOOKS_ACTION, GUEST, READER, LIBRARIAN, ADMIN),
    CART(CART_ACTION, READER),
    CHANGE_BOOK(CHANGE_BOOK_ACTION, ADMIN),
    GENRE_LIST(GENRES_ACTION, GUEST, READER, LIBRARIAN, ADMIN),
    LIBRARIAN_LIST(LIBRARIANS_ACTION, ADMIN),
    LOGIN(LOGIN_ACTION, GUEST),
    LOGOUT(LOGOUT_ACTION, READER, LIBRARIAN, ADMIN),
    NEW_BOOK(NEW_BOOK_ACTION, ADMIN),
    ORDER_LIST(ORDERS_ACTION, LIBRARIAN, ADMIN),
    PROFILE(PROFILE_ACTION, READER, LIBRARIAN, ADMIN),
    PUBLISHER_LIST(PUBLISHERS_ACTION, GUEST, READER, LIBRARIAN, ADMIN),
    READER_LIST(READERS_ACTION, LIBRARIAN, ADMIN),
    REGISTER(REGISTER_ACTION, GUEST),
    REGISTER_LIBRARIAN(REGISTER_LIBRARIAN_ACTION, ADMIN),
    RESET_PASSWORD(RESET_ACTION, GUEST),
    USER(USER_ACTION, LIBRARIAN, ADMIN);

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

    public interface Constants {

        String ACTIVATE_ACTION = "/activate.do";
        String AUTHORS_ACTION = "/authors.do";
        String BOOKS_ACTION = "/books.do";
        String CART_ACTION = "/cart.do";
        String CHANGE_BOOK_ACTION = "/change-book.do";
        String GENRES_ACTION = "/genres.do";
        String INDEX_ACTION = "/";
        String LIBRARIANS_ACTION = "/librarians.do";
        String LOGIN_ACTION = "/login.do";
        String LOGOUT_ACTION = "/logout.do";
        String NEW_BOOK_ACTION = "/new-book.do";
        String ORDERS_ACTION = "/orders.do";
        String PROFILE_ACTION = "/profile.do";
        String PUBLISHERS_ACTION = "/publishers.do";
        String READERS_ACTION = "/readers.do";
        String REGISTER_ACTION = "/register.do";
        String REGISTER_LIBRARIAN_ACTION = "/new-librarian.do";
        String RESET_ACTION = "/reset.do";
        String USER_ACTION = "/user.do";
    }

}