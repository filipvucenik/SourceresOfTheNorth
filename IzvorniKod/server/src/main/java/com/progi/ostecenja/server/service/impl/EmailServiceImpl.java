package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendRequestSubmittedEmail(String to, Long id) throws MessagingException {
        String subject = "Report successfuly submited!";
        String htmlBody = "<!DOCTYPE html>\n" +
                "<html lang='en'>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n" +
                "    <title>Report Submission Confirmation</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Arial', sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            background-color: #f4f4f4;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            background-color: #fff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "        }\n" +
                "\n" +
                "        p {\n" +
                "            color: #555;\n" +
                "        }\n" +
                "\n" +
                "        .footer {\n" +
                "            margin-top: 20px;\n" +
                "            text-align: center;\n" +
                "            color: #777;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class='container'>\n" +
                "        <h1>Thank You for Your Report Submission!</h1>\n" +
                "        <p>We appreciate your contribution to improving our city. Your report with the code <strong>"+ id.toString() +"</strong> has been successfully submitted and will be reviewed.</p>\n" +
                "        <p>If you have any further questions or concerns, please feel free to contact us.</p>\n" +
                "\n" +
                "        <div class='footer'>\n" +
                "            <p>Best Regards,<br> The Website Team</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        emailSender.send(mimeMessage);
    }

    public void sendRequestStatusChange(String to, Long id, String newStatus) throws MessagingException {
        String subject = "Report "+ id + " status updated!";
        String htmlBody = "<!DOCTYPE html>\n" +
                "<html lang='en'>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n" +
                "    <title>Report Status Update</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Arial', sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            background-color: #f4f4f4;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            background-color: #fff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "        }\n" +
                "\n" +
                "        p {\n" +
                "            color: #555;\n" +
                "        }\n" +
                "\n" +
                "        .status-update {\n" +
                "            margin-top: 20px;\n" +
                "            padding: 10px;\n" +
                "            border-radius: 4px;\n" +
                "            background-color: #e6f7ff;\n" +
                "            border: 1px solid #66b3ff;\n" +
                "            color: #3366ff;\n" +
                "        }\n" +
                "\n" +
                "        .footer {\n" +
                "            margin-top: 20px;\n" +
                "            text-align: center;\n" +
                "            color: #777;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class='container'>\n" +
                "        <h1>Report Status Update</h1>\n" +
                "        <p>The status of your report with the code <strong>"+id+"</strong> has been updated.</p>\n" +
                "        \n" +
                "        <div class='status-update'>\n" +
                "            <p><strong>New Status:</strong> "+newStatus+"</p>\n" +
                "        </div>\n" +
                "\n" +
                "        <p>If you have any further questions or concerns, please feel free to contact us.</p>\n" +
                "\n" +
                "        <div class='footer'>\n" +
                "            <p>Best Regards,<br> The Website Team</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        emailSender.send(mimeMessage);
    }
}