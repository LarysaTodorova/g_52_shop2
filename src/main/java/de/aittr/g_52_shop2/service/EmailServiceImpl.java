package de.aittr.g_52_shop2.service;

import de.aittr.g_52_shop2.domain.entity.User;
import de.aittr.g_52_shop2.exception_handling.exceptions.EmailSendingException;
import de.aittr.g_52_shop2.service.interfaces.ConfirmationCodeService;
import de.aittr.g_52_shop2.service.interfaces.EmailService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender sender;
    private final Configuration mailConfig;
    private final ConfirmationCodeService confirmationCodeService;
    private final String mailFrom;
    private final String host;
    private final String port;

    public EmailServiceImpl(
            JavaMailSender sender,
            Configuration mailConfig,
            ConfirmationCodeService confirmationCodeService,
            @Value("${spring.mail.username}") String mailFrom,
            @Value("${server.host}") String host,
            @Value("${server.port}") String port
    ) {
        this.sender = sender;
        this.mailConfig = mailConfig;
        this.confirmationCodeService = confirmationCodeService;
        this.mailFrom = mailFrom;
        this.host = host;
        this.port = port;

        // устанавливаем кодировку для всех шаблонов чтобы корректно отображалось письмо
        mailConfig.setDefaultEncoding("UTF-8");
        // ищем шаблоны относительно EmailServiceImpl
        TemplateLoader loader = new ClassTemplateLoader(EmailServiceImpl.class, "/mail/");
        // специальный класс умеет загружать шаблон из папки
        mailConfig.setTemplateLoader(loader);
    }

    @Override
    public void sendConfirmationEmail(User user) {
        //создаем пустое письмо
        MimeMessage message = sender.createMimeMessage();
        // создаем помощника, который заполняет письмо
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        String text = generateConfirmationEmail(user);

        try {
            // от кого письмо
            helper.setFrom(mailFrom);
            // кому письмо отправляем
            helper.setTo(user.getEmail());
            // тема письма
            helper.setSubject("Registration confirmation");
            helper.setText(text, true);

            sender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException("Email sending error", e);
        }
    }

    private String generateConfirmationEmail(User user) {
        try {
            Template template = mailConfig.getTemplate("confirm_registration_mail.ftlh");
            String code = confirmationCodeService.generateConfirmationCode(user);
            // http://localhost:8080/users/confirm/sd675ft-su6t-fs6f -
            // при клике на эту ссылку будет отправляться GET запрос на наш бэкенд
            String link = String.format("http://%s:%s/users/confirm/%s", host, port, code);

            Map<String, Object> mailValues = new HashMap<>();
            mailValues.put("name", user.getUsername());
            mailValues.put("link", link);

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, mailValues);

        } catch (IOException | TemplateException e) {
            throw new EmailSendingException("Email text generation error", e);
        }
    }
}
