package org.frequency.frequencyapi.aws;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Service
public class S3Service {

    private final String bucketName = "your-bucket-name";
    private final S3Client s3Client;

    public S3Service() {
        this.s3Client = S3Client.builder()
                .region(Region.US_WEST_2)
                .build();
    }


    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        this.s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(inputStream, file.getSize()));

        inputStream.close();

        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, Region.US_WEST_2.toString(), fileName);
    }

}
