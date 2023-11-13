package com.communitycart.BackEnd.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailSenderServiceTest {

    @Autowired
    private EmailSenderService emailSenderService;

//    @Test
//    public void sendEmail(){
//        emailSenderService.sendSimpleEmail("abhinavtv97@gmail.com", "Hello TV Pula!!", ":)");
//    }

//    @Test
//    public void sendHtmlEmailTest(){
//        emailSenderService.sendHtmlEmail("anshumanp97@gmail.com");
//    }

}