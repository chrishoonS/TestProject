package com.medi.testproject.snsLogin.apple.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class AppleService {

    @Value("${apple.client.id}")
    private String APPLE_CLIENT_ID;

    @Value("${apple.redirect.url}")
    private String APPLE_REDIRECT_URL;

    @Value("${apple.auth.uri}")
    private String APPLE_AUTH_URI;

    @Value("${apple.team.id}")
    private String APPLE_TEAM_ID;

    @Value("${apple.login.key}")
    private String APPLE_LOGIN_KEY;

    // RestTemplate → deprecated 이슈로 RestClient로 대체
    private final RestClient restClient;

    // Jackson 라이브러리의 클래스. JSON <-> Java 객체 변환.
    private final ObjectMapper objectMapper;

    // 생성자 주입: objectMapper는 스프링에서 Bean으로 주입 가능.
    public AppleService(ObjectMapper objectMapper) {
        this.restClient = RestClient.create(); // RestClient 인스턴스 생성
        this.objectMapper = objectMapper;
    }

    /**
     *  로그인 URL 생성
     **/
    public String getLoginUrl() {

        String loginUrl = UriComponentsBuilder.fromUriString(APPLE_AUTH_URI + "/auth/authorize")
                .queryParam("client_id", APPLE_CLIENT_ID)
                .queryParam("redirect_uri", APPLE_REDIRECT_URL)
                .queryParam("response_type", "code id_token")
                .queryParam("scope", "name email")
                .queryParam("response_mode", "form_post")
                .build()
                .toUriString();;

        log.info("Access Token URL ::::: {}", loginUrl);

        return loginUrl;
    }
}