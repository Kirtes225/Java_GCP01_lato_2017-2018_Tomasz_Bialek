package Loggers;

import Student.Student;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailLogger implements Logger {

    private static String MAIL = "javacrawlerprog1@gmail.com";
    private static String PASSWORD = "Javapasword3";
    private static String RECIPIENT = "javacrawlertest3@onet.pl";

    @Override
    public void log(String status, Student student) {
        String subject = "Crawler update - " + status;

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.port","465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(MAIL, PASSWORD);
                    }
                });
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(RECIPIENT));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(RECIPIENT));
            message.setSubject(subject);
            message.setText(status + ":" + student.toString());
            Transport.send(message);
        }catch (MessagingException e){
            e.printStackTrace();
            System.out.println("MailLogger Error");
        }
    }
}
