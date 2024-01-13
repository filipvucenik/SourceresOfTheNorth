package com.progi.ostecenja.server.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    public void sendSimpleMessage(String to, String subject, String text);

    public void sendRequestSubmittedEmail(String to, Long id) throws MessagingException;

    public void sendRequestStatusChange(String to, Long id, String newStatus) throws MessagingException;
}
