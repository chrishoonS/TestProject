package com.medi.testproject.snsLogin.naver.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medi.testproject.common.FormDataEncoder;
import com.medi.testproject.oauth.OAuthProvider;
import com.medi.testproject.oauth.OAuthTokenResponseDTO;
import com.medi.testproject.snsLogin.naver.dto.NaverDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class NaverService {

    @Value("${naver.client.id}")
    private String NAVER_CLIENT_ID;

    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${naver.redirect.url}")
    private String NAVER_REDIRECT_URL;

    @Value("${naver.auth.uri}")
    private String NAVER_AUTH_URI;

    @Value("${naver.api.uri}")
    private String NAVER_API_URI;

    // RestTemplate → deprecated 이슈로 RestClient로 대체
    private final RestClient restClient;

    // Jackson 라이브러리의 클래스. JSON <-> Java 객체 변환.
    private final ObjectMapper objectMapper;

    // 생성자 주입: objectMapper는 스프링에서 Bean으로 주입 가능.
    public NaverService(ObjectMapper objectMapper) {
        this.restClient = RestClient.create(); // RestClient 인스턴스 생성
        this.objectMapper = objectMapper;
    }

    /**
     *  로그인 URL 생성
     **/
    public String getLoginUrl() {

        String loginUrl = UriComponentsBuilder.fromUriString(NAVER_AUTH_URI + "/oauth2.0/authorize")
                .queryParam("client_id", NAVER_CLIENT_ID)
                .queryParam("redirect_uri", NAVER_REDIRECT_URL)
                .queryParam("response_type", "code")
                .toUriString();

        log.info("Access Token URL ::::: {}", loginUrl);

        return loginUrl;

    }

    /**
     * 인가코드로 AccessToken 요청 → 사용자 정보 조회
     */
    public NaverDTO getUserInfo(String code) {

        try{
            // 인가 코드를 이용해 액세스 토큰 발급
            String tokenResponse = restClient.post()
                    .uri(NAVER_AUTH_URI + "/oauth2.0/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(FormDataEncoder.create()
                            .add("grant_type", "authorization_code")
                            .add("client_id", NAVER_CLIENT_ID)
                            .add("client_secret", NAVER_CLIENT_SECRET)
                            .add("redirect_uri", NAVER_REDIRECT_URL)
                            .add("code", code)
                            .encode())
                    .retrieve()
                    .body(String.class);

            // JSON 문자열 → TokenResponseDTO 객체로 변환
            OAuthTokenResponseDTO tokenDTO = objectMapper.readValue(tokenResponse, OAuthTokenResponseDTO.class);

            log.info("access_token  ::::: {}", tokenDTO.getAccessToken());
            log.info("refresh_token ::::: {}", tokenDTO.getRefreshToken());
            log.info("token_type ::::: {}", tokenDTO.getTokenType());

            // 발급된 토큰으로 사용자 정보 조회
            String userResponse = restClient.post()
                    .uri(NAVER_API_URI + "/v1/nid/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .retrieve()
                    .body(String.class);

            // Jackson에서 제공하는 JSON Tree API. JSON 접근 시 null-safe 방식
            JsonNode root = objectMapper.readTree(userResponse);

            return NaverDTO.builder()
                    .id(root.path("response").path("id").asText())
                    .email(root.path("response").path("email").asText(null))
                    .name(root.path("response").path("name").asText())
                    .provider(String.valueOf(OAuthProvider.NAVER))
                    .build();

        }catch (Exception e) {
            log.error("네이버 로그인 오류", e);
            throw new RuntimeException("네이버 로그인 실패");
        }

    }

}
