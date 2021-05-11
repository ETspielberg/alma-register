package org.unidue.ub.unidue.almaregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
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

    private final TemplateEngine templateEngine;

    private static final Logger log = LoggerFactory.getLogger(MailSenderService.class);


    MailSenderService(
            // potential error on unknown bean for emailSender can be ignored, emailSender bean is created from configuration properties
            JavaMailSender emailSender,
            TemplateEngine templateEngine
    ) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    public void sendNotificationMail(RegistrationRequest registrationRequest, String duplicatedId, String duplicatedIdType) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(userExistsEmailFrom);
            messageHelper.setTo(userExistsEmailTo);
            String text = buildMailBody(registrationRequest, duplicatedId, duplicatedIdType);
            messageHelper.setSubject("Nutzer eventuell dublett vorhanden");
            messageHelper.setText(text);
        };
        emailSender.send(messagePreparator);
        log.debug("sent dublettencheck email");
    }

    private String buildMailBody(RegistrationRequest registrationRequest, String duplicatedId, String duplicatedIdType) {
        return String.format("Der Nutzer %s %s hat sich gerade angemeldet.\n", registrationRequest.firstName, registrationRequest.lastName) +
                String.format("Die ID %s vom Typ %s ist bereits im System vorhanden.\n", duplicatedId, duplicatedIdType) +
                "Mit freundlichen Grüßen\n" +
                "\nDies ist eine automatisch generierte E-Mail.\n";
    }

}