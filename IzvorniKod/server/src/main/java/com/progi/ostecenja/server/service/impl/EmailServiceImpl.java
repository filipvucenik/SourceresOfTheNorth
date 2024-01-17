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
        String subject = "Prijava uspješno podnesena!";
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
                "        <h1>Hvala što ste podnjeli prijavu!</h1>\n" +
                "        <p>Cijenimo vaš doprinos u poboljšanju našeg grada. Vaš izvještaj s kodom <strong>"+ id.toString() +"</strong> uspješno je podnesen i bit će pregledan. Ako imate dodatnih pitanja ili briga, slobodno nas kontaktirajte.</p>\n" +
                "\n" +
                "        <div class='footer'>\n" +
                "            <p>S poštovanjem,<br> Tim web stranice</p>\n" +
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

    @Override
    public void sendRequestStatusChange(String to, Long id, String newStatus) throws MessagingException {
        String subject = "Status izvještaja "+ id + " je ažuriran!";
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
                "        <h1>Status izvještaja je ažuriran</h1>\n" +
                "        <p>Status vašeg izvještaja s kodom <strong>"+id+"</strong> je ažuriran.</p>\n" +
                "        \n" +
                "        <div class='status-update'>\n" +
                "            <p><strong>Novi Status:</strong> "+newStatus+"</p>\n" +
                "        </div>\n" +
                "\n" +
                "        <p>Ako imate dodatnih pitanja ili briga, slobodno nas kontaktirajte.</p>\n" +
                "\n" +
                "        <div class='footer'>\n" +
                "            <p>S poštovanjem,<br> Tim web stranice</p>\n" +
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

    @Override
    public void sendRequestCategoryChange(String to, Long id, String newCategoryName) throws MessagingException {
        String subject = "Kategorija izvještaja "+ id + " je promijenjena!";
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
                "        <h1>Kategorija izvještaja je promijenjena</h1>\n" +
                "        <p>Kategorija vašeg izvještaja s kodom <strong>"+id+"</strong> je promijenjena.</p>\n" +
                "        \n" +
                "        <div class='status-update'>\n" +
                "            <p><strong>Nova kategorija:</strong> "+newCategoryName+"</p>\n" +
                "        </div>\n" +
                "\n" +
                "        <p>Ako imate dodatnih pitanja ili briga, slobodno nas kontaktirajte.</p>\n" +
                "\n" +
                "        <div class='footer'>\n" +
                "            <p>S poštovanjem,<br> Tim web stranice</p>\n" +
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

    @Override
    public void sendRequestDeleted(String to, Long id) throws MessagingException {
        String subject = "Izvještaj "+ id + " obrisan!";
        String htmlBody = "<!DOCTYPE html>\n" +
                "<html lang='hr'>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n" +
                "    <title>>Izvještaj obrisan</title>\n" +
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
                "        .deleted-report-info {\n" +
                "            margin-top: 20px;\n" +
                "            padding: 10px;\n" +
                "            border-radius: 4px;\n" +
                "            background-color: #ffe6e6;\n" +
                "            border: 1px solid #ff6666;\n" +
                "            color: #ff3333;\n" +
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
                "        <h1>Izvještaj "+ id + " obrisan!</h1>\n" +
                "        <p>Žao nam je obavijestiti vas da je vaš izvještaj s kodom <strong>" + id + "</strong> obrisan.</p>\n" +
                "        \n" +
                "        <p>Ako imate bilo kakvih pitanja ili zabrinutosti u vezi s ovim brisanjem, slobodno nas kontaktirajte.</p>\n" +
                "\n" +
                "        <div class='footer'>\n" +
                "            <p>S poštovanjem,<br> Tim web stranice</p>\n" +
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