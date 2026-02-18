package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.MyPageResponse;
import com.example.demo.service.MyPageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/mypage")
    public MyPageResponse mypage(@RequestParam Long userId) {
        return myPageService.getMyPage(userId);
    }

    @PostMapping("/logout")
    public void logout() {
    	
    }
}
