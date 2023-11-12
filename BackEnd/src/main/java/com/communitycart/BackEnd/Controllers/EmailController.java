package com.communitycart.BackEnd.Controllers;

import com.communitycart.BackEnd.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailSenderService service;

//    public ResponseEntity<?> sendEmail(String toEmail, String body, String subject)
}
