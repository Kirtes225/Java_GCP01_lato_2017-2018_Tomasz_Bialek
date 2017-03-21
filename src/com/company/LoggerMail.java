package com.company;

import com.example.Student;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class LoggerMail implements Logger {
    private final String username = "javacrawlertest3@onet.eu";
    private final String password = "javapassword3";
    private final String recipient = "javacrawlertest3@onet.eu";

    /*public LoggerMail(String userName, String password, String recipient) {
        this.userName = username;
        this.password = password;
        this.recipient = recipient;
    }*/

    @Override
    public void log(String status, Student student) {
        String subject = "Crawler - change: " + status;
        String msg = status + ":" + student.toString();
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.poczta.onet.pl");
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

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);

            message.setText("");
            if (student != null) {
                message.setText(student.toString());
            }

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("LoggerMail Error");
        }
    }
}