package com.ehr.services;

import java.io.StringWriter;
import java.io.Writer;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ehr.dto.SendEmailRequest;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
// import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final Configuration configuration;
    private final JavaMailSender javaMailSender;

    public void sendEmail(SendEmailRequest request) {
        System.out.println("Sending activation mail.........");
        
        Writer out = new StringWriter();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom(request.getFrom(), "Admin");
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());

            Template template = configuration.getTemplate(request.getFrom());


            template.process(request.getTemplateName(), out);

            helper.setText(out.toString(), true);

            javaMailSender.send(mimeMessage);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }

    }


}