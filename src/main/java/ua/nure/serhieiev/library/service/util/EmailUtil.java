package ua.nure.serhieiev.library.service.util;

import ua.nure.serhieiev.library.model.entities.Order;
import ua.nure.serhieiev.library.model.entities.User;
import ua.nure.serhieiev.library.service.ApplicationException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EmailUtil {

    static private Session session;

    static {
        try {
            session = (Session) new InitialContext().lookup("java:comp/env/mail/session");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private static void sendEmail(String email, String subject, String content) {
        try {
            Message message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject);
            message.setContent(content, "text/plain");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new ApplicationException("Cannot send email!", e);
        }
    }

    public static void sendRegistrationLink(User user) {
        String email = user.getEmail();
        String subject = "Library - activate account";
        String content = "Activation link: http://localhost:8080/activate.do?token=" + user.getActivationToken();
        sendEmail(email, subject, content);
    }

    public static void sendResetLink(User user) {
        String email = user.getEmail();
        String subject = "Library - reset password";
        String content = "Reset link: http://localhost:8080/reset.do?token=" + user.getResetPasswordToken();
        sendEmail(email, subject, content);
    }

    public static void sendWarnMessage(User user) {
        String email = user.getEmail();
        String subject = "Library - return books";
        String content = user.getName() + ", please return books.";
        sendEmail(email, subject, content);
    }

    public static void sendRejectMessage(User user) {
        String email = user.getEmail();
        String subject = "Library - reject order";
        String content = "Sorry, but we cannot give you books, that you ordered.";
        sendEmail(email, subject, content);
    }

    public static void sendWelcomeMessage(User user) {
        String email = user.getEmail();
        String subject = "Library - grants rights";
        String content = "You have been given the rights to manage the library, " +
                "get the password from the administrator of the library.";
        sendEmail(email, subject, content);
    }

}