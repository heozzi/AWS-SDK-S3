package com.example.demoawssdks3.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 업로드한 리소스의 퍼블릭 URL을 가지고 있는 객체
 */
@Data
public class ResDto {
    private String url;
    @Builder
    public ResDto(String url) {
        this.url = url;
    }
}