package org.unidue.ub.unidue.almaregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.unidue.ub.alma.shared.user.AlmaUser;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
public class MailSenderService {

    // the email address to appear as "from"
    @Value("${alma.register.email.from}")
    private String registerEmailFrom;

    private final JavaMailSender emailSender;

    private final TemplateEngine templateEngine;

    private final MessageSource messageSource;

    private static final Logger log = LoggerFactory.getLogger(MailSenderService.class);


    MailSenderService(
            // error on unknown bean for emailSender can be ignored, emailSender bean is created from configuration properties
            JavaMailSender emailSender,
            TemplateEngine templateEngine,
            MessageSource messageSource
    ) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
    }

    public void sendNotificationMail(AlmaUser almaUser) {
        String email = almaUser.getContactInfo().getEmail().get(0).getEmailAddress();
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(registerEmailFrom);
            messageHelper.setTo(email);
            String text = buildMailBody(almaUser);
            messageHelper.setSubject("Herzlich willkommen an der Universitätsbibliothek Duisburg-Essen");
            messageHelper.setText(text, true);
        };
        emailSender.send(messagePreparator);
        log.debug("sent email to " + email);
    }

    private String buildMailBody(AlmaUser almaUser) {
        Context context = new Context();
        context.setVariable("name", almaUser.getFirstName() + " " + almaUser.getLastName());
        LocalDate birthdate = almaUser.getBirthDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate now = LocalDate.now();
        long diff = ChronoUnit.YEARS.between(birthdate, now);
        boolean isMinor = diff < 18;
        context.setVariable("isMinor", isMinor);
        context.setVariable("primaryId", almaUser.getPrimaryId());
        context.setVariable("title", almaUser.getUserTitle().getValue());
        return templateEngine.process("registrationSuccessMailTemplate", context);
    }

}