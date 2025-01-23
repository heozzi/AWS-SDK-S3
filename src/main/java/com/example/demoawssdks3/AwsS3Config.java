package com.example.demoawssdks3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * S3 리소스 컨트롤 위한 객체를 빈으로 구성
 */
@Slf4j
@Configuration
public class AwsS3Config {
    /**
     * 엑세스키, 시크릿키, 리전 정보 필요
     * - 환경변수 -> 읽어서 -> 변수에 세팅
     */
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 amazonS3() {
        log.info("Creating Amazon S3");
        // 차후 제거
        log.info("access-key " + accessKey);
        log.info("secret-key " + secretKey);
        log.info("region "     + region);

        // 인증정보 구성
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        // 객체반환
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region)
                .build();
    }



}