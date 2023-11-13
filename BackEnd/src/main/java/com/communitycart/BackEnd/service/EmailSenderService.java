package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.entity.Customer;
import com.communitycart.BackEnd.entity.Order;
import com.communitycart.BackEnd.repository.CustomerRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CustomerRepository customerRepository;

    private TemplateEngine templateEngine;

    @Autowired
    private ThymeLeafService thymeLeafService;

    private final String from = "cartcommunityltd@gmail.com";

    public void sendSimpleEmail(String toEmail,
                                String body,
                                String subject) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("Mail Send...");
    }

    public void sendEmailWithAttachment(String toEmail,
                                        String body,
                                        String subject,
                                        String attachment) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("spring.email.from@gmail.com");
        mimeMessageHelper.setTo(toEmail);
//        mimeMessageHelper.setText(thymeLeafService.createContent());
        mimeMessageHelper.setSubject(subject);

        FileSystemResource fileSystem
                = new FileSystemResource(new File(attachment));

        mimeMessageHelper.addAttachment(fileSystem.getFilename(),
                fileSystem);

        mailSender.send(mimeMessage);
        System.out.println("Mail Send...");

    }

    @Async
    public void sendHtmlEmail(Order order) {
        try {
            Customer customer = customerRepository.findByCustomerId(order.getCustomerId());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setPriority(1);
            helper.setSubject("Your CommunityCart order " + order.getOrderId() + " of "
                    + order.getItems().size() + " items.");
            helper.setFrom(from);
            helper.setTo(customer.getEmail());
            helper.setText(thymeLeafService.createContent("orderConfirmation.html",
                    Map.of("orderId", order.getOrderId(), "totalPrice", order.getTotalPrice(), "name",
                            customer.getName())), true);
            mailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void sendUpdateDeliveryDate(Order order, String date) {
        try {
            Customer customer = customerRepository.findByCustomerId(order.getCustomerId());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setPriority(1);
            helper.setSubject("Your order will be delivered by " + date + ".");
            helper.setFrom(from);
            helper.setTo(customer.getEmail());
            helper.setText(thymeLeafService.createContent("deliverydate.html",
                    Map.of("orderId", order.getOrderId(), "deliveryDate", date,
                            "totalPrice", order.getTotalPrice(),
                            "status", order.getStatus(),
                            "name",
                            customer.getName())), true);
            mailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void sendDeliveryStatus(Order order, String status) {
        try {
            Customer customer = customerRepository.findByCustomerId(order.getCustomerId());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setPriority(1);
            helper.setSubject("Your order is " + status + ".");
            helper.setFrom(from);
            helper.setTo(customer.getEmail());
            helper.setText(thymeLeafService.createContent("orderStatus.html",
                    Map.of("orderId", order.getOrderId(), "status", status,
                            "totalPrice", order.getTotalPrice(),
                            "deliveryDate", order.getDeliveryDate(),
                            "name",
                            customer.getName())), true);
            mailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void sendDeliveredStatus(Order order, String status) {
        try {
            Customer customer = customerRepository.findByCustomerId(order.getCustomerId());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setPriority(1);
            helper.setSubject("Your order " + order.getOrderId() + " is " + status + ".");
            helper.setFrom(from);
            helper.setTo(customer.getEmail());
            helper.setText(thymeLeafService.createContent("delivered.html",
                    Map.of("orderId", order.getOrderId(),
                            "name",
                            customer.getName())), true);
            mailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
}
