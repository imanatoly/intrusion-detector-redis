package com.booking.security.hackertest.detector;


import com.booking.security.hackertest.repository.LoginAttemptRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static com.booking.security.hackertest.TestConstants.SAMPLE_LOGIN_ATTEMPT;
import static com.booking.security.hackertest.TestConstants.SAMPLE_LOG_LINE;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

public class HackerDetectorTest {

    private HackerDetector hackerDetector;

    @Mock
    private LogLineParser logLineParser;

    @Mock
    private LoginAttemptRepository loginAttemptRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        hackerDetector = new HackerDetectorImpl(logLineParser, loginAttemptRepository);
        ReflectionTestUtils.setField(hackerDetector, "maxAlowedFailAttempts", 5);
    }

    @Test
    public void shouldReturnEmptyIfNotEnoughFailedLoginAttemps() {
        // given
        String line = SAMPLE_LOG_LINE;

        given(logLineParser.parseLogLine(line)).willReturn(SAMPLE_LOGIN_ATTEMPT);
        given(loginAttemptRepository.failedLoginCount(SAMPLE_LOGIN_ATTEMPT.getIp())).willReturn(1);

        // when
        String result = hackerDetector.parseLogLine(line);

        // then
        assertEquals("", result);
    }

    @Test
    public void shouldReturnIpWithEnoughFailedLoginAttemps() {
        // given
        String line = SAMPLE_LOG_LINE;

        given(logLineParser.parseLogLine(line)).willReturn(SAMPLE_LOGIN_ATTEMPT);
        given(loginAttemptRepository.failedLoginCount(SAMPLE_LOGIN_ATTEMPT.getIp())).willReturn(5);

        // when
        String result = hackerDetector.parseLogLine(line);

        // then
        assertEquals(SAMPLE_LOGIN_ATTEMPT.getIp(), result);
    }
}
