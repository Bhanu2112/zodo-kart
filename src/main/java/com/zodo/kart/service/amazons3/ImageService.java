package com.zodo.kart.service.amazons3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.io.InputStream;
import java.util.UUID;

/**
 * Author : Bhanu prasad
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3 amazonS3Client;
    @Value("${aws.bucket.name}")
    private String bucketName;
    @Value("${aws.region}")
    private String region ;


    @Transactional
    public String uploadFileToS3Bucket(MultipartFile file, String imagebelongsto) {

        // String fileName = file.getOriginalFilename();

        // Generate a new unique file name (e.g., with timestamp or UUID)
        String originalFileName = file.getOriginalFilename();
       // String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));  // Get file extension
        String fileName = imagebelongsto+UUID.randomUUID() + originalFileName; // Rename file

        String fileUrl = "";
        try {

            // Get the InputStream of the file and upload directly to S3
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());


            amazonS3Client.putObject(bucketName, fileName, inputStream,metadata);
            fileUrl = amazonS3Client.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;

    }
}
