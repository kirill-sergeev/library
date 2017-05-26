package ua.nure.serhieiev.library.controller;

import ua.nure.serhieiev.library.model.entities.User;

import java.util.Arrays;

import static ua.nure.serhieiev.library.controller.Action.Constants.*;
import static ua.nure.serhieiev.library.model.entities.User.Role.*;

public enum Action{

    BOOK_LIST(BOOK_LIST_ACTION, GUEST, READER, LIBRARIAN, ADMIN),
    ORDER_LIST(ORDER_LIST_ACTION, LIBRARIAN, ADMIN),
    UNCONFIRMED_ORDER_LIST(UNCONFIRMED_ORDER_LIST_ACTION, LIBRARIAN, ADMIN),
    CURRENT_ORDER_LIST(CURRENT_ORDER_LIST_ACTION, LIBRARIAN, ADMIN),
    CART(CART_ACTION, READER),


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
        String BOOK_LIST_ACTION = "/books.do";
        String ORDER_LIST_ACTION = "/orders.do";
        String UNCONFIRMED_ORDER_LIST_ACTION = "/unconfirmed-orders.do";
        String CURRENT_ORDER_LIST_ACTION = "/current-orders.do";
        String CART_ACTION = "/cart.do";


        String REGISTER_ACTION = "/register.do";

        String ACTIVATE_ACTION = "/activate.do";
        String RESET_ACTION = "/reset.do";
        String LOGOUT_ACTION = "/logout.do";
        String MAIN_ACTION = "/";
        String READER_LIST_ACTION = "/admin/readers.do";
        String LIBRARIAN_LIST_ACTION = "/admin/librarians.do";
        String REGISTER_LIBRARIAN_ACTION = "/admin/new-librarian.do";

        String USER_PROFILE_ACTION = "/user.do";
        String GENRE_LIST_ACTION = "/genres.do";

    }

}