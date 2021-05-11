package com.phamtan.upload_file;

import com.phamtan.upload_file.client.config.FileUploadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UploadFileApplication implements CommandLineRunner {
    @Autowired
    FileUploadConfig fileUploadConfig;
    public static void main(String[] args) {
        SpringApplication.run(UploadFileApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Grpc upload file with chunk size : "+fileUploadConfig.getChunkSize());
    }
}
