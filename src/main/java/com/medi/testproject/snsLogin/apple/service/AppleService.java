package com.medi.testproject.snsLogin.apple.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppleService {

    @Value("${apple.client.id}")
    private String APPLE_CLIENT_ID;

    @Value("${apple.redirect.url}")
    private String APPLE_REDIRECT_URL;

    @Value("${apple.auth.uri}")
    private String APPLE_AUTH_URI;

    /**
     *  로그인 URL 생성
     **/
    public String getLoginUrl() {

        String loginUrl = APPLE_AUTH_URI + "/auth/authorize"
                + "?client_id=" + APPLE_CLIENT_ID
                + "&redirect_uri=" + APPLE_REDIRECT_URL
                + "&response_type=code&scope=name%20email&response_mode=form_post";

        log.info("Access Token URL ::::: {}", loginUrl);

        return loginUrl;
    }
}