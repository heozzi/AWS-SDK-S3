package com.example.demoawssdks3.controller;

import com.example.demoawssdks3.dto.ResDto;
import com.example.demoawssdks3.service.AwsS3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * restapi 전용, aws s3와 통신하면서 업로드 처리를 수행 컨트롤러
 */
@Slf4j
@RequestMapping("/api/s3")
@RestController
public class AwsS3Controller {
    // 의존성 주입(DI)
    @Autowired
    private AwsS3Service awsS3Service;

    // 실제 업로드 처리를 요청받는 라우터 구성, post,
    @PostMapping("/upload")
    public ResDto upload(@RequestParam("file") MultipartFile file) {
        // IO , sb -> aws sdk -> s3 -> sb
        String url = ""; // 업로드 결과
        try{
            // 1. 업로드 처리 (서비스)
            url = awsS3Service.upload( file );
        }catch (Exception e){
            log.info("업로드 오류 " + e.getMessage());
        }
        return ResDto.builder()
                .url(url)
                .build();
    }
}