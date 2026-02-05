package com.example.demo.dto;

import com.example.demo.entity.User;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String name;
    private int coinBalance;

    public static UserResponse from(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setCoinBalance(user.getCoinBalance());
        return dto;
    }
}
