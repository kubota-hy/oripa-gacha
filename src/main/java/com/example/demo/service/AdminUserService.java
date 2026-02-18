package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CoinLedger;
import com.example.demo.entity.User;
import com.example.demo.enums.CoinLedgerAction;
import com.example.demo.repository.CoinLedgerRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final CoinLedgerRepository coinLedgerRepository;

    @Transactional
    public CoinAdjustResult grantCoin(Long userId, int amount, String reason, Long adminUserId) {
        validateAmountPositive(amount);
        validateReason(reason);

        User user = findUser(userId);

        int before = user.getCoinBalance();
        int after = before + amount;

        user.setCoinBalance(after);

        saveLedger(userId, adminUserId, CoinLedgerAction.GRANT, amount, before, after, reason);

        return new CoinAdjustResult(userId, before, after);
    }

    @Transactional
    public CoinAdjustResult deductCoin(Long userId, int amount, String reason, Long adminUserId) {
        validateAmountPositive(amount);
        validateReason(reason);

        User user = findUser(userId);

        int before = user.getCoinBalance();
        int after = before - amount;

        if (after < 0) {
            throw new IllegalArgumentException("coin would be negative");
        }

        user.setCoinBalance(after);

        saveLedger(userId, adminUserId, CoinLedgerAction.DEDUCT, amount, before, after, reason);

        return new CoinAdjustResult(userId, before, after);
    }

    @Transactional
    public CoinAdjustResult setCoin(Long userId, int newBalance, String reason, Long adminUserId) {
        if (newBalance < 0) {
            throw new IllegalArgumentException("newBalance must be >= 0");
        }
        validateReason(reason);

        User user = findUser(userId);

        int before = user.getCoinBalance();
        int after = newBalance;

        int diff = after - before; // ledgerのamountは差分で残す運用

        user.setCoinBalance(after);

        saveLedger(userId, adminUserId, CoinLedgerAction.SET, diff, before, after, reason);

        return new CoinAdjustResult(userId, before, after);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    private void validateAmountPositive(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
    }

    private void validateReason(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("reason is required");
        }
        if (reason.length() > 255) {
            throw new IllegalArgumentException("reason too long");
        }
    }

    private void saveLedger(Long userId, Long adminUserId, CoinLedgerAction action, int amount,
                            int before, int after, String reason) {
        // coin_ledger テーブルがある前提。無いならここをコメントアウトでもOK。
        CoinLedger ledger = CoinLedger.builder()
                .userId(userId)
                .adminUserId(adminUserId) // まだ管理者認証弱いなら nullでもOK
                .action(action)
                .amount(amount)
                .beforeBalance(before)
                .afterBalance(after)
                .reason(reason)
                .build();

        coinLedgerRepository.save(ledger);
    }

    public record CoinAdjustResult(Long userId, int before, int after) {}
}
