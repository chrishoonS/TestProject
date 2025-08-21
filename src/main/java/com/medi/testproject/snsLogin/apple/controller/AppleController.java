package com.medi.testproject.snsLogin.apple.controller;

import com.medi.testproject.common.MsgEntity;
import com.medi.testproject.snsLogin.apple.service.AppleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("apple")
public class AppleController {

    private final AppleService appleService;

    @GetMapping("/callback")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) throws Exception {
//        AppleDTO kakaoInfo = appleService.getUserInfo(request.getParameter("code"));

        return ResponseEntity.ok()
                .body(new MsgEntity("Success", null));
    }


}
