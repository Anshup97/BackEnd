package com.communitycart.BackEnd.service;

import com.communitycart.BackEnd.entity.Customer;
import com.communitycart.BackEnd.entity.Order;
import com.communitycart.BackEnd.entity.Seller;
import com.communitycart.BackEnd.repository.CustomerRepository;
import com.communitycart.BackEnd.repository.SellerRepository;
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

/**
 * Service class to send emails to customers
 * and sellers.
 */
@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ThymeLeafService thymeLeafService;

    @Autowired
    private SellerRepository sellerRepository;

    //Application email id.
    private final String from = "cartcommunityltd@gmail.com";

    /**
     * Used to send simple email - plain text subject and body.
     * Used for sending OTP for password change.
     * @param toEmail
     * @param body
     * @param subject
     */
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

    /**
     * Used to send emails with body as HTML.
     * Used to send order confirmation emails to customers.
     * Template is present in resources/templates folder.
     * Details are sent dynamically to the template using Thymeleaf.
     * @param order
     */
    @Async
    public void sendHtmlEmail(Order order) {
        try {
            Customer customer = customerRepository.findByCustomerId(order.getCustomerId());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setPriority(1);
            helper.setSubject("Your CommunityCart order " + order.getOrderId() + " of "
                    + order.getItems().size() + " items placed.");
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

    /**
     * Used to send emails with body as HTML.
     * Used to send order delivery date emails to customers.
     * Template is present in resources/templates folder.
     * Details are sent dynamically to the template using Thymeleaf.
     * @param order
     * @param date
     */
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

    /**
     * Used to send emails with body as HTML.
     * Used to send order delivery status to customers.
     * Template is present in resources/templates folder.
     * Details are sent dynamically to the template using Thymeleaf.
     * @param order
     * @param status
     */
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
            if(order.getDeliveryDate() != null){
                helper.setText(thymeLeafService.createContent("orderStatus.html",
                        Map.of("orderId", order.getOrderId(), "status", status,
                                "totalPrice", order.getTotalPrice(),
                                "deliveryDate", order.getDeliveryDate(),
                                "name",
                                customer.getName())), true);
            } else {
                helper.setText(thymeLeafService.createContent("orderStatus.html",
                        Map.of("orderId", order.getOrderId(), "status", status,
                                "totalPrice", order.getTotalPrice(),
                                "deliveryDate", "To be confirmed by the seller.",
                                "name",
                                customer.getName())), true);
            }
            mailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    /**
     * Used to send emails with body as HTML.
     * Used to send order delivery confirmation to customers.
     * Template is present in resources/templates folder.
     * Details are sent dynamically to the template using Thymeleaf.
     * @param order
     * @param status
     */
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


    public void sendCancelledStatus(Order order, String status) {
        try {
            Seller seller = sellerRepository.findById(order.getSellerId()).get();
            Customer customer = customerRepository.findByCustomerId(order.getCustomerId());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setPriority(1);
            helper.setSubject("Your order " + order.getOrderId() + " is " + status + ".");
            helper.setFrom(from);
            helper.setTo(customer.getEmail());
            helper.setText(thymeLeafService.createContent("cancelled.html",
                    Map.of("orderId", order.getOrderId(),
                            "name",
                            customer.getName())), true);
            mailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void sendCancelledStatusSeller(Order order, String status) {
        try {
            Seller seller = sellerRepository.findById(order.getSellerId()).get();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setPriority(1);
            helper.setSubject("Order " + order.getOrderId() + " is " + status + ".");
            helper.setFrom(from);
            helper.setTo(seller.getEmail());
            helper.setText(thymeLeafService.createContent("cancelledSeller.html",
                    Map.of("orderId", order.getOrderId(),
                            "name",
                            seller.getName())), true);
            mailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
}
