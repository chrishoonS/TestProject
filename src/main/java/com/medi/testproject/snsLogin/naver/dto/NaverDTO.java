package com.medi.testproject.snsLogin.naver.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NaverDTO {

    private String resultcode;
    private String message;
    private Response response;

    @Builder
    @Data
    public static class Response {
        private String id;
        private String email;
        private String name;
    }

}
