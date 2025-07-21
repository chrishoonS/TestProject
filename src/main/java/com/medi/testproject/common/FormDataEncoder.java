package com.medi.testproject.common;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class FormDataEncoder {

    // MultiValueMap : Spring 제공 인터페이스
    // params.add(key, value) 시 하나의 키에 여러 value를 가질 수 있음 또는 List를 put할 수 있음
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    public static FormDataEncoder create() {
        return new FormDataEncoder();
    }

    public FormDataEncoder add(String key, String value) {
        params.add(key, value);
        return this;
    }

    public MultiValueMap<String, String> encode() {
        return params;
    }
}
