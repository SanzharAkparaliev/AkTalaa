package com.example.taxiaktalaa.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {
        public boolean sendEmail(String subject,String message,String to){
            boolean f = false;
            String from = "sansoft00@gmail.com";
            String host = "smtp.gmail.com";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host",host);
            properties.put("mail.smtp.port","465");
            properties.put("mail.smtp.ssl.enable","true");
            properties.put("mail.smtp.auth","true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return null;
                }
            });

            session.setDebug(true);
            MimeMessage m = new MimeMessage(session);

            try {
                m.setFrom(from);
                m.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
                m.setSubject(subject);
                m.setText(message);
                m.setContent(message,"text/html");
                Transport.send(m);
                f=true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return f;

        }
}
