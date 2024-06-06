package com.khu.gitbox.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.khu.gitbox.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private final S3Config s3Config;

    public String uploadFile(MultipartFile multipartFile) {
        String bucketName = s3Config.getBucket();
        String fileName = multipartFile.getOriginalFilename() + "_" + UUID.randomUUID().toString().substring(0, 5);

        try {
            // MultipartFile로부터 InputStream 가져오기
            InputStream fileInputStream = multipartFile.getInputStream();

            // 파일 메타데이터 생성
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            // S3 버킷에 파일 업로드
            amazonS3.putObject(bucketName, fileName, fileInputStream, metadata);
            fileInputStream.close();

            // 업로드된 파일의 URL 반환
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            log.error("Error uploading file to S3: {}", e.getMessage());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading file to S3");
        }
    }

    public void deleteFile(String fileName) {
        String bucketName = s3Config.getBucket();

        try {
            // 주어진 파일 이름을 사용해 버킷에서 파일 삭제
            amazonS3.deleteObject(bucketName, fileName);
            log.info("File '{}' successfully deleted from bucket '{}'", fileName, bucketName);
        } catch (AmazonServiceException e) {
            log.error("Error deleting file from S3: {}", e.getMessage());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading file to S3");
        }
    }
}

