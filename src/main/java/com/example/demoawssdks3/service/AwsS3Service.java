package com.example.demoawssdks3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class AwsS3Service {
    // S3와 업무 수행하는 객체
    @Autowired
    private AmazonS3 amazonS3;

    // 버킷명 획득
    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    // 메소드 선언부에 예외 처리 던지기 구성 X자동으로 처리해줌
    @SneakyThrows
    public String upload(MultipartFile file) {
        // 파일이 비어있는지 , 이름이 널인지 등등 검사
        if (file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
            throw new Exception("파일 누락 혹은 비어있음");
        }
        // 업로드한 파일을 이미지로 제한한다면등 -> 형식 제한 (JS에서 진행, 백엔드 처리 가능함)
        return checkAndUpload(file);
    }

    private String checkAndUpload(MultipartFile file) {
        // check  -> 확장자 검사, 파일의 헤더 검사(매직코드등등) 요휴성 판단 할 수 있다
        // s3 upload
        // 업로드시
        // - 파일명 고려 (원본유지 -> 중복 발생 가능성 존재, 고유한 이름 필요 (UUID 값 추가)) -> 이름 변조
        // - or s3내에 계정별로 디렉토리를 구성하여 업로드(이름변조 추가)
        // 1. 원본 파일명 획득
        String originalFilename = file.getOriginalFilename();
        System.out.println("originalFilename : "+originalFilename);
        // 2. .을 중심으로 이름과 확장자 획득 가능 -> 이름 변조후 결합 or image/png/jpeg... 마임타입(mime)
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        System.out.println("ext : "+ext);
        // 3. 이름+UUID등의 고유값 적절하게 묶어서 파일명 구성
        String s3UploadName = UUID.randomUUID().toString().substring(0,12) + "." + originalFilename;
        System.out.println("s3UploadName : "+s3UploadName);
        String url = "";
        try {
            // 4. 스트림구성(자바는 스트림을 통해서 통신 진행) -> 오픈
            // 파일 -> 스트림 구성(byteArrayInputStream)
            InputStream is = file.getInputStream();
            byte[] bytes = IOUtils.toByteArray(is);

            // 마임타임 구성 -> 메타데이터
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + ext);
            metadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);



            // 5. s3 업로드 수행(API 다양하게 제공되고 있음)
            PutObjectRequest obj = new PutObjectRequest(
                    bucketName,     // 버킷의 이름
                    s3UploadName,   // 업로드될 파일명
                    byteArrayInputStream,   // 실 데이터를 전달할 스트림 -> 업로드된 파일과 연결, 다양하게 API 제공함
                    metadata        // 메타 정보(파일)
            );


            amazonS3.putObject(obj );
            // 6. 스트림 닫기
            byteArrayInputStream.close();
            // 7. 퍼블릭 URL 등록
             url = amazonS3.getUrl(bucketName,s3UploadName).toString();
            // 8. 반환
        } catch (Exception e) {
            System.out.println("파일 업로드간 오류  : " + e.getMessage());
        }
        return url;
    }
}
