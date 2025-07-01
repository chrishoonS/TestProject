package com.medi.testproject.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 로그인 화면 호출시 뷰를 담당하는 컨트롤러에서 인증 URL을 모델에 담아 화면에 전달하고,
 * 로그인시 redirect_uri을 통해 넘어오는 컨트롤러는 @RestController로 json 데이터를 응답하는 기능
 *
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgEntity {

    private String msg;
    private Object result;

    public MsgEntity(String msg, Object result) {
        this.msg = msg;
        this.result  = result;
    }
}
