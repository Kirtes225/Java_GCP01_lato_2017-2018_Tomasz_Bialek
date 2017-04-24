package com.company.loggers;

import com.company.events.CrawlerEventType;
import com.example.Student;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailLogger implements Logger {

    //private final String username = "javacrawlerprog1@gmail.com";
    //private final String password = "Javapassword3";
    //private final String recipient = "javacrawlertest3@onet.pl";

    private final String userName;
    private final String password;
    private final String recipient;

    public MailLogger(String userName, String password, String recipient) {
        this.userName = userName;
        this.password = password;
        this.recipient = recipient;
    }

    @Override
    public void log(CrawlerEventType crawlerEventType, Student student) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "poczta.agh.edu.pl");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        // session.setDebug(true); //todo delete this later

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(crawlerEventType.toString());

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