package com.example.demo.service;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが存在しません"));
    }

    @Transactional
    public void consumeCoin(Long userId, int amount) {
        if (amount <= 0) throw new RuntimeException("不正な金額です");

        User user = findById(userId);

        if (user.getCoinBalance() < amount) {
            throw new RuntimeException("コインが不足しています");
        }

        user.setCoinBalance(user.getCoinBalance() - amount);
        // save不要（JPAの変更検知で更新される）
    }
    
    @Transactional
    public void addCoin(Long userId, int amount) {
        if (amount <= 0) throw new RuntimeException("不正な金額です");
        User user = findById(userId);
        user.setCoinBalance(user.getCoinBalance() + amount);
    }

}


