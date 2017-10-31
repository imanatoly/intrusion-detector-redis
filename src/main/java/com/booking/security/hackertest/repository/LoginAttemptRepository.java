package com.booking.security.hackertest.repository;

import com.booking.security.hackertest.model.LoginAttempt;
import org.springframework.scheduling.annotation.Async;

public interface LoginAttemptRepository {

    @Async
    void save(LoginAttempt loginAttempt);

    int failedLoginCount(String ip);
}
