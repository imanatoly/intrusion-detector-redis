package com.booking.security.hackertest.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginAttempt {

    private String ip;
    private String username;
    private boolean success;
    private Long time;
}
