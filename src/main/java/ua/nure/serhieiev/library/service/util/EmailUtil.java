package ua.nure.serhieiev.library.service.util;

import ua.nure.serhieiev.library.model.User;
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

    public static void sendRegistrationLink(User user) {
        try {
            Message message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Ecommerce - activate account");
            message.setContent("Activation link: http://localhost:8080/activate?token=" + user.getActivationToken(), "text/plain");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new ApplicationException("Cannot send email!");
        }
    }

}