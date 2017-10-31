package com.booking.security.hackertest.detector;


import com.booking.security.hackertest.model.LoginAttempt;
import org.junit.Test;

import static com.booking.security.hackertest.TestConstants.SAMPLE_LOGIN_ATTEMPT;
import static com.booking.security.hackertest.TestConstants.SAMPLE_LOG_LINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogLineParserTest {

    private LogLineParser parser = new LogLineParserImpl();

    @Test
    public void shouldParseLogLine() {
        // given
        String logLine = SAMPLE_LOG_LINE;
        LoginAttempt loginAttempt = SAMPLE_LOGIN_ATTEMPT;

        // when
        LoginAttempt result = parser.parseLogLine(logLine);

        // then
        assertEquals(loginAttempt.getTime(), result.getTime());
        assertEquals(loginAttempt.getIp(), result.getIp());
        assertEquals(loginAttempt.getUsername(), result.getUsername());
        assertTrue(result.isSuccess());
    }

    @Test
    public void shouldParseLogLineWithWhitespace() {
        // given
        String logLine = "1507365137 , 187.218.83.136\t,John.Smith, SUCCESS ";

        // when
        LoginAttempt loginAttempt = parser.parseLogLine(logLine);

        // then
        assertEquals(Long.valueOf(1507365137l), loginAttempt.getTime());
        assertEquals("187.218.83.136", loginAttempt.getIp());
        assertEquals("John.Smith", loginAttempt.getUsername());
        assertTrue(loginAttempt.isSuccess());
    }
}
