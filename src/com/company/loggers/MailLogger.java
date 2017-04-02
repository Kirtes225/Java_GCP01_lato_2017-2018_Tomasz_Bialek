package com.company.loggers;

import com.example.Student;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailLogger implements Logger {

    private final String username = "javacrawlerprog1@gmail.com";
    private final String password = "Javapassword3";
    private final String recipient = "javacrawlertest3@onet.pl";


    @Override
    public void log(String status, Student student) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // session.setDebug(true); //todo delete this later

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(status);

            message.setText("");
            if (student != null) {
                message.setText(student.toString());
            }

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("MailLogger Error");
        }
    }
}