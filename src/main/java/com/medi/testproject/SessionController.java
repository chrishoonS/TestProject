package com.medi.testproject;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    @GetMapping("/set")
    public String setSession(HttpSession session) {
        session.setAttribute("user", "song");
        return "session set: " + session.getId();
    }

    @GetMapping("/get")
    public String getSession(HttpSession session) {
        return "session id: " + session.getId() + ", user: " + session.getAttribute("user");
    }
}
