package com.medi.testproject.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medi.testproject.common.FormDataEncoder;
import com.medi.testproject.kakao.dto.KakaoDTO;
import com.medi.testproject.kakao.dto.KakaoTokenResponseDTO;
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
     *  카카오 로그인 URL 생성
     **/
    public String getKakaoLogin() {
        return UriComponentsBuilder.fromUriString(KAKAO_AUTH_URI + "/oauth/authorize")
                .queryParam("client_id"    , KAKAO_CLIENT_ID)
                .queryParam("redirect_uri" , KAKAO_REDIRECT_URL)
                .queryParam("response_type", "code")
                .toUriString();
    }

    /**
     * 인가코드로 AccessToken 요청 → 사용자 정보 조회
     */
    public KakaoDTO getKakaoInfo(String code) {

        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Authorization code is missing");
        }

        try {

            // 인가 코드를 이용해 액세스 토큰 발급
            KakaoTokenResponseDTO tokenResponse = getAccessToken(code);
            // 발급된 토큰으로 사용자 정보 조회
            return getUserInfoWithToken(tokenResponse.getAccessToken());

        } catch (Exception e) {
            log.error("카카오 로그인 실패 ::::: {}", e.getMessage(), e);
            throw new RuntimeException("카카오 로그인 중 오류가 발생했습니다.");
        }
    }

    /**
     * Access Token 요청
     */
    private KakaoTokenResponseDTO getAccessToken(String code) throws JsonProcessingException {
        String response = restClient.post()
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

        log.info("AccessToken Response ::::: {}", response);

        // JSON 문자열 → KakaoTokenResponseDTO 객체로 변환
        return objectMapper.readValue(response, KakaoTokenResponseDTO.class);
    }


    /**
     * 토큰을 가진 사용자 정보 조회
     */
    private KakaoDTO getUserInfoWithToken(String accessToken) throws JsonProcessingException {
        String response = restClient.post()
                .uri(KAKAO_API_URI + "/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // 헤더에 Bearer 토큰 추가
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .body(String.class);

        log.info("UserInfo Response ::::: {}", response);

        // Jackson에서 제공하는 JSON Tree API. JSON 접근 시 null-safe 방식
        JsonNode root = objectMapper.readTree(response);
        long id = root.path("id").asLong();
        String email = root.path("kakao_account").path("email").asText(null);
        String nickname = root.path("kakao_account").path("profile").path("nickname").asText();

        return KakaoDTO.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .build();
    }
}
