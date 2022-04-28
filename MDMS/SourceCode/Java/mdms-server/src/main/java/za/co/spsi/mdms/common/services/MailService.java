package za.co.spsi.mdms.common.services;

import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Dependent
@Data @Log
public class MailService {


    @Inject
    @ConfValue(value = "mail.host", folder = "server")
    private String host;

    @Inject
    @ConfValue(value = "mail.username", folder = "server")
    private String username;

    @Inject
    @ConfValue(value = "mail.password", folder = "server")
    private String password;

    @Inject
    @ConfValue(value = "mail.from", folder = "server", defaultValue = "ice-noreply@pecgroup.co.za")
    private String from;

    @Inject
    @ConfValue(value = "mail.port", folder = "server", defaultValue = "25")
    private int port;

    public void sendHtml(String to, String from, String subject, String body) {
        Properties props = new Properties();

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", username != null?"true":"false");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Authenticator authenticator = username != null?new Authenticator() {
            private PasswordAuthentication pa = new PasswordAuthentication(username, password);

            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return pa;
            }
        }:null;


        MimeMessage message = new MimeMessage(Session.getInstance(props, authenticator));
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setContent(body, "text/html");
            Transport.send(message);
        } catch (MessagingException ex) {
            log.severe(ex.getMessage());
        }
    }
}
