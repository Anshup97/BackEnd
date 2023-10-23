package com.communitycart.BackEnd.service;

import java.io.File;
import java.io.FileDescriptor;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Demo {
    public static void main(String[] args) {
        Path path = Paths.get("images/product");
        System.out.println(path.toString());
        File file = new File(path.toString());
        for(File fi : file.listFiles()){
            System.out.println(fi.getName());

        }

    }
}
