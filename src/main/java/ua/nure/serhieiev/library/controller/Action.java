package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.model.entities.User;

import java.util.Arrays;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;
import static ua.nure.serhieiev.library.model.entities.User.Role.*;

public enum Action{

    BOOK_LIST(BOOKS_ACTION, GUEST, READER, LIBRARIAN, ADMIN),
    AUTHOR_LIST(AUTHORS_ACTION, GUEST, READER, LIBRARIAN, ADMIN),
    GENRE_LIST(GENRES_ACTION, GUEST, READER, LIBRARIAN, ADMIN),
    PUBLISHER_LIST(PUBLISHERS_ACTION, GUEST, READER, LIBRARIAN, ADMIN),



    ORDER_LIST(CLOSED_ORDERS_ACTION, LIBRARIAN, ADMIN),
    UNCONFIRMED_ORDER_LIST(ORDERS_ACTION, LIBRARIAN, ADMIN),
    CURRENT_ORDER_LIST(CURRENT_ORDERS_ACTION, LIBRARIAN, ADMIN),
    CART(CART_ACTION, READER),
    READERS(READERS_ACTION, LIBRARIAN, ADMIN),
    LIBRARIANS(LIBRARIANS_ACTION, ADMIN),
    REGISTER_LIBRARIAN(REGISTER_LIBRARIAN_ACTION, ADMIN),
    PROFILE(PROFILE_ACTION, READER, LIBRARIAN, ADMIN),
    USER(USER_ACTION, LIBRARIAN, ADMIN),





    REGISTER(REGISTER_ACTION, GUEST),

    LOGIN(LOGIN_ACTION, GUEST),

    ACTIVATE_ACCOUNT(ACTIVATE_ACTION, GUEST),

    RESET_PASSWORD(RESET_ACTION, GUEST),

    LOGOUT(LOGOUT_ACTION, READER, LIBRARIAN, ADMIN);

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
        String LOGIN_ACTION = "/login.do";
        String BOOKS_ACTION = "/books.do";
        String CLOSED_ORDERS_ACTION = "/closed-orders.do";
        String ORDERS_ACTION = "/orders.do";
        String CURRENT_ORDERS_ACTION = "/current-orders.do";
        String CART_ACTION = "/cart.do";
        String READERS_ACTION = "/readers.do";
        String LIBRARIANS_ACTION = "/librarians.do";
        String REGISTER_LIBRARIAN_ACTION = "/new-librarian.do";
        String PROFILE_ACTION = "/profile.do";
        String USER_ACTION = "/user.do";

        String AUTHORS_ACTION = "/authors.do";
        String GENRES_ACTION = "/genres.do";
        String PUBLISHERS_ACTION = "/publishers.do";





        String REGISTER_ACTION = "/register.do";

        String ACTIVATE_ACTION = "/activate.do";
        String RESET_ACTION = "/reset.do";
        String LOGOUT_ACTION = "/logout.do";
        String MAIN_ACTION = "/";

        String USER_PROFILE_ACTION = "/user.do";
        String GENRE_LIST_ACTION = "/genres.do";

    }

}