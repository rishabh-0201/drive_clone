//package com.org.googledrive.config;
//
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//
//
//@Configuration
//public class GoogleCloudConfig {
//
//
//    @Value("${google.cloud.credentials-file-path}")
//    private String credentialsFilePath;
//
//    @Value("${}")
//    private String bucketName;
//
//    @Bean
//    public Storage storage(){
//
//        Path credentialsPath = Paths.get(credentialsFilePath);
//
//        // Set the environment variable to use the credentials file
//        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", credentialsFilePath.toString());
//
//        // initialize google cloud storage client
//        return StorageOptions.getDefaultInstance().getService();
//
//    }
//
//    @Bean
//    public String bucketName(){
//        return bucketName;
//    }
//
//
//}
