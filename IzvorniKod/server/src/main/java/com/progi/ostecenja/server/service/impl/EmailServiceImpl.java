package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.repo.Report;
import com.progi.ostecenja.server.service.CategoryService;
import com.progi.ostecenja.server.service.EmailService;
import com.progi.ostecenja.server.service.UsersService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UsersService usersService;

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

    @Override
    public void sendReportGroupedMain(Report groupLeader, List<Report> groupedReports) throws MessagingException{
        if(groupLeader.getUserID()!=null){
            String subject = "Prijave grupirane";
            String reportDetails = buildReportDetails(groupedReports);
            String htmlBody = loadHtml()
                    .replace("{{mainReportCode}}", groupLeader.getReportID().toString())
                    .replace("{{groupedReportsDetails}}",reportDetails);

            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(usersService.findByUserId(groupLeader.getUserID()).get().getEmail());
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            emailSender.send(mimeMessage);
        }

        for(Report report:groupedReports){
            if(report.getUserID()==null) continue;
            String subject = "Prijava grupirana";
            String mainReportDetails = buildMainReportDetails(groupLeader);
            String htmlBody = loadHtml2()
                    .replace("{{reportCode}}",report.getReportID().toString())
                    .replace("{{mainReportDetails}}",mainReportDetails);
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(usersService.findByUserId(report.getUserID()).get().getEmail());
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            emailSender.send(mimeMessage);
        }

        //reportDetails = buildMainReportDetails(groupLeader);
        //htmlBody = loadHtml2();

    }

    private String buildMainReportDetails(Report groupLeader) {
        StringBuilder detailsBuilder = new StringBuilder();
        detailsBuilder.append("<div class='report-details'>");
        detailsBuilder.append("<p><strong>Naslov:</strong> ").append(groupLeader.getReportHeadline()).append("</p>");
        detailsBuilder.append("<p><strong>ID prijave:</strong> ").append(groupLeader.getReportID()).append("</p>");
        detailsBuilder.append("<p><strong>Opis:</strong> ").append(groupLeader.getDescription()).append("</p>");
        detailsBuilder.append("<p><strong>Kategorija:</strong> ").append(categoryService.findByCategoryId(groupLeader.getCategoryID()).get().getCategoryName()).append("</p>");
        detailsBuilder.append("</div>");

        return detailsBuilder.toString();
    }

    private String buildReportDetails(List<Report> groupedReports) {
        StringBuilder detailsBuilder = new StringBuilder();
        for (Report report : groupedReports) {
            detailsBuilder.append("<div class='report'>");
            detailsBuilder.append("<div class='report-details'>");
            detailsBuilder.append("<p><strong>Naslov:</strong> ").append(report.getReportHeadline()).append("</p>");
            detailsBuilder.append("<p><strong>ID prijave:</strong> ").append(report.getReportID()).append("</p>");
            detailsBuilder.append("<p><strong>Opis:</strong> ").append(report.getDescription()).append("</p>");
            detailsBuilder.append("<p><strong>Kategorija:</strong> ").append(categoryService.findByCategoryId(report.getCategoryID()).get().getCategoryName()).append("</p>");
            detailsBuilder.append("</div>");
            detailsBuilder.append("</div>");
        }
        return detailsBuilder.toString();
    }

    private static String loadHtml(){
        return "<!DOCTYPE html>\n" +
                "<html lang='en'>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n" +
                "    <title>Reports Grouped Notification</title>\n" +
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
                "        .grouped-reports {\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .report {\n" +
                "            margin-bottom: 20px;\n" +
                "            border: 1px solid #ddd; /* Border for visual separation */\n" +
                "            padding: 10px; /* Add some padding for better readability */\n" +
                "            border-radius: 8px; /* Optional: Add rounded corners */\n" +
                "        }\n" +
                "\n" +
                "        .report-details {\n" +
                "            /* Adjust the styling as needed */\n" +
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
                "        <h1>Prijave grupirane</h1>"+
                "        <p>Ove prijave grupirane su na vašu prijavu <strong>{{mainReportCode}}</strong>:</p>\n" +
                "        \n" +
                "        <div class='grouped-reports'>\n" +
                "            {{groupedReportsDetails}}\n" +
                "        </div>\n" +
                "\n" +
                "        <p>Ako imate bilo kakvih pitanja ili zabrinutosti u vezi s ovim grupiranjem, slobodno nas kontaktirajte.</p>\n" +
                "\n" +
                "        <div class='footer'>\n" +
                "            <p>S poštovanjem,<br> Tim web stranice</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }

    private static String loadHtml2(){
        return "<!DOCTYPE html>\n" +
                "<html lang='en'>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n" +
                "    <title>Report Grouped Notification</title>\n" +
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
                "        .main-report-details {\n" +
                "            margin-top: 20px;\n" +
                "            padding: 10px;\n" +
                "            border-radius: 4px;\n" +
                "            background-color: #ffe6e6;\n" +
                "            border: 1px solid #ff6666;\n" +
                /*"            color: #ff3333;\n" +*/
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
                "        <h1>Prijava grupirana</h1>\n" +
                "        <p>Vaša prijava s kodom <strong>{{reportCode}}</strong> je grupirana s drugom prijavom.</p>\n" +
                "        \n" +
                "        <div class='main-report-details'>\n" +
                "            <p><strong>Detalji glavne prijave:</strong></p>\n" +
                "            {{mainReportDetails}}\n" +
                "        </div>\n" +
                "\n" +
                "        <p>Ako imate bilo kakvih pitanja ili zabrinutosti u vezi s ovim grupiranjem, slobodno nas kontaktirajte.</p>\n" +
                "\n" +
                "        <div class='footer'>\n" +
                "            <p>S poštovanjem,<br> Tim web stranice</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }
}