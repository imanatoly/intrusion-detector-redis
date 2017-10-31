package com.booking.security.hackertest.detector;

import com.booking.security.hackertest.model.LoginAttempt;

public interface LogLineParser {

    LoginAttempt parseLogLine(String logline);
}
