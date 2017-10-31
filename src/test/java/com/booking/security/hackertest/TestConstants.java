package com.booking.security.hackertest;

import com.booking.security.hackertest.model.LoginAttempt;

public class TestConstants {

    public static final String LOG_FILE = "accesslog.txt";

    public static final String SAMPLE_LOG_LINE = "1507365137,187.218.83.136,John.Smith,SUCCESS";

    public static final LoginAttempt SAMPLE_LOGIN_ATTEMPT = LoginAttempt.builder()
            .time(1507365137L)
            .ip("187.218.83.136")
            .username("John.Smith")
            .success(true)
            .build();
}
