package com.medi.testproject;

import com.medi.testproject.snsLogin.apple.service.AppleService;
import com.medi.testproject.snsLogin.kakao.service.KakaoService;
import com.medi.testproject.snsLogin.naver.service.NaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final AppleService appleService;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("kakaoUrl", kakaoService.getLoginUrl());
        model.addAttribute("naverUrl", naverService.getLoginUrl());
        model.addAttribute("appleUrl", appleService.getLoginUrl());
        model.addAttribute("appleClientId", "com.mediplussolution.hime.dev");

        return "index";
    }

}
