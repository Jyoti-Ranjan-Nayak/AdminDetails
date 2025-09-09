package com.example.emplyee.sevice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSuccessEmail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to Our Company!");
        message.setText("Hi " + name + ",\n\nYour profile has been successfully created in our system.\n\nBest regards,\nHR Team");

        mailSender.send(message);
    }
}

