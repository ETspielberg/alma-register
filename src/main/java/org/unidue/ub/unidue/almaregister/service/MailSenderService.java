package org.unidue.ub.unidue.almaregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.unidue.ub.unidue.almaregister.model.RegistrationRequest;

@Service
public class MailSenderService {

    // the email address to appear as "from"
    @Value("${alma.user.exists.email.from}")
    private String userExistsEmailFrom;

    // the email address to appear as "from"
    @Value("${alma.user.exists.email.to}")
    private String userExistsEmailTo;

    private final JavaMailSender emailSender;

    private static final Logger log = LoggerFactory.getLogger(MailSenderService.class);


    MailSenderService(
            // potential error on unknown bean for emailSender can be ignored, emailSender bean is created from configuration properties
            JavaMailSender emailSender
    ) {
        this.emailSender = emailSender;
    }

    public void sendNotificationMail(RegistrationRequest registrationRequest) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(userExistsEmailFrom);
            messageHelper.setTo(userExistsEmailTo);
            String text = buildMailBody(registrationRequest);
            messageHelper.setSubject("Nutzer eventuell dublett vorhanden");
            messageHelper.setText(text);
        };
        emailSender.send(messagePreparator);
        log.debug("sent dublettencheck email");
    }

    private String buildMailBody(RegistrationRequest registrationRequest) {
        return "Liebe Kolleg:innen\n\n" +
                String.format("Nutzer:in %s %s hat sich gerade über das Webformular angemeldet.\n",
                        registrationRequest.firstName, registrationRequest.lastName) +
                String.format("Die ID %s vom Typ %s ist bereits im System vorhanden.\n",
                        registrationRequest.duplicateId, registrationRequest.duplicateIdType) +
                "Mit freundlichen Grüßen\n" +
                "Das Registrierungsformular\n" +
                "\n\nDies ist eine automatisch generierte E-Mail.\n";
    }

}