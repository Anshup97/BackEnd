package com.communitycart.BackEnd.service;

import java.io.File;
import java.io.FileDescriptor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Demo {
    public static void main(String[] args) {
        Date date = new Date();
        LocalDate localDate = date.toInstant()
                        .atZone(ZoneId.systemDefault())
                                .toLocalDate();
        System.out.println(localDate.toString());

    }
}
