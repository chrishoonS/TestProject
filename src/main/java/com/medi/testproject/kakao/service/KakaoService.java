package com.medi.testproject.kakao.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medi.testproject.common.FormDataEncoder;
import com.medi.testproject.kakao.dto.KakaoDTO;
import com.medi.testproject.oauth.OAuthProvider;
import com.medi.testproject.oauth.OAuthTokenResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class KakaoService {

    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    @Value("${kakao.auth.uri}")
    private String KAKAO_AUTH_URI;

    @Value("${kakao.api.uri}")
    private String KAKAO_API_URI;

    // RestTemplate → deprecated 이슈로 RestClient로 대체
    private final RestClient restClient;

    // Jackson 라이브러리의 클래스. JSON <-> Java 객체 변환.
    private final ObjectMapper objectMapper;

    // 생성자 주입: objectMapper는 스프링에서 Bean으로 주입 가능.
    public KakaoService(ObjectMapper objectMapper) {
        this.restClient = RestClient.create(); // RestClient 인스턴스 생성
        this.objectMapper = objectMapper;
    }

    /**
     *  로그인 URL 생성
     **/
    public String getLoginUrl() {
        return UriComponentsBuilder.fromUriString(KAKAO_AUTH_URI + "/oauth/authorize")
                .queryParam("client_id", KAKAO_CLIENT_ID)
                .queryParam("redirect_uri", KAKAO_REDIRECT_URL)
                .queryParam("response_type", "code")
                .toUriString();
    }

    /**
     * 인가코드로 AccessToken 요청 → 사용자 정보 조회
     */
    public KakaoDTO getUserInfo(String code) {
        try {
            // 인가 코드를 이용해 액세스 토큰 발급
            String tokenResponse = restClient.post()
                    .uri(KAKAO_AUTH_URI + "/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(FormDataEncoder.create()
                            .add("grant_type", "authorization_code")
                            .add("client_id", KAKAO_CLIENT_ID)
                            .add("redirect_uri", KAKAO_REDIRECT_URL)
                            .add("code", code)
                            .encode())
                    .retrieve()
                    .body(String.class);

            // JSON 문자열 → KakaoTokenResponseDTO 객체로 변환
            OAuthTokenResponseDTO tokenDTO = objectMapper.readValue(tokenResponse, OAuthTokenResponseDTO.class);

            // 발급된 토큰으로 사용자 정보 조회
            String userResponse = restClient.post()
                    .uri(KAKAO_API_URI + "/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .retrieve()
                    .body(String.class);

            // Jackson에서 제공하는 JSON Tree API. JSON 접근 시 null-safe 방식
            JsonNode root = objectMapper.readTree(userResponse);

            return KakaoDTO.builder()
                    .id(root.path("id").asLong())
                    .email(root.path("kakao_account").path("email").asText(null))
                    .nickname(root.path("kakao_account").path("profile").path("nickname").asText())
                    .provider(String.valueOf(OAuthProvider.KAKAO))
                    .build();

        } catch (Exception e) {
            log.error("카카오 로그인 오류", e);
            throw new RuntimeException("카카오 로그인 실패");
        }
    }
}