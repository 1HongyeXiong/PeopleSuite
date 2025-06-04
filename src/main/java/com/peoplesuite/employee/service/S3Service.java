package com.peoplesuite.employee.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Service
public class S3Service {
    private final String bucketName = "peoplesuite-photos";

    private final S3Client s3Client = S3Client.builder()
            .region(Region.US_WEST_2)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

    /**
     * upload the photo to the S3
     */
    public String uploadPhoto(String employeeId, MultipartFile file) throws IOException {
        String key = "employees/" + employeeId + "/photo.jpg";

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize()));


        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    /**
     * get url of photo
     */
    public String getPhotoUrl(String employeeId) {
        String key = "employees/" + employeeId + "/photo.jpg";
        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }
}
