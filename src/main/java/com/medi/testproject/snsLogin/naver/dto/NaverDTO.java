package com.medi.testproject.snsLogin.naver.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NaverDTO {

    private String id;
    private String email;
    private String name;

    // 공통
    private String provider; // Kakao, Naver, Google, Apple

}
