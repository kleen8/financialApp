package nl.sogyo.financialApp;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {

    static final String username = System.getenv("email");
    static final String password = System.getenv("emailPass");

    static public void sendMail(String receivingEmail){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a Session
        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            // Create a Message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(receivingEmail)
            );
            message.setSubject("Balance is low");
            message.setText("Your balance is below 5,00 please check your account!!!");

            // Send the email
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
