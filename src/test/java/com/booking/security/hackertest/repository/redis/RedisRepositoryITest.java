package com.booking.security.hackertest.repository.redis;

import com.booking.security.hackertest.IntTestBase;
import com.booking.security.hackertest.model.LoginAttempt;
import com.booking.security.hackertest.repository.LoginAttemptRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class RedisRepositoryITest extends IntTestBase {

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    @Test
    public void shouldSaveLoginAttempt() throws Exception {
        // given
        String ip = UUID.randomUUID().toString();
        Calendar calendar = Calendar.getInstance();

        LoginAttempt loginAttempt = LoginAttempt.builder()
                .ip(ip)
                .success(false)
                .time(calendar.toInstant().getEpochSecond())
                .username("john.doe")
                .build();

        // when
        loginAttemptRepository.save(loginAttempt);
        int count = loginAttemptRepository.failedLoginCount(ip);
        assertEquals(1, count);
    }

    @Test
    public void shouldSaveMultipleLoginAttempts() throws Exception {
        // given
        String ip = UUID.randomUUID().toString();
        Calendar calendar = Calendar.getInstance();

        LoginAttempt loginAttempt1 = LoginAttempt.builder()
                .ip(ip)
                .success(false)
                .time(calendar.toInstant().getEpochSecond())
                .username("john.doe")
                .build();
        LoginAttempt loginAttempt2 = LoginAttempt.builder()
                .ip(ip)
                .success(false)
                .time(calendar.toInstant().getEpochSecond() + 1)
                .username("jane.doe")
                .build();

        // when
        loginAttemptRepository.save(loginAttempt1);
        loginAttemptRepository.save(loginAttempt2);
        int count = loginAttemptRepository.failedLoginCount(ip);

        // then
        assertEquals(2, count);
    }
}
