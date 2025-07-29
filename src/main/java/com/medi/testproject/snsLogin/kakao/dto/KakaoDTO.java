package com.medi.testproject.snsLogin.kakao.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KakaoDTO {

    private Long id;
    private String email;
    private String nickname;

    // 공통
    private String provider; // Kakao, Naver, Google, Apple

}
