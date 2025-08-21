package com.medi.testproject.snsLogin.apple.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppleDTO {

    private Long id;
    private String conneted_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Builder
    @Data
    public static class Properties {
        private String nickname;
        private String profile_image;
        private String thumbnail_image;
    }

    @Builder
    @Data
    public static class KakaoAccount {
        private String profile_nickname_needs_agreement;
        private String profile_image_needs_agreement;
        private Profile profile;
        private String has_email;
        private String email_needs_agreement;
        private String is_email_valid;
        private String is_email_verified;
        private String email;
    }

    @Builder
    @Data
    public static class Profile {
        private String nickname;
        private String thumbnail_image_url;
        private String profile_image_url;
        private String is_default_image;
        private String is_default_nickname;
    }
}
