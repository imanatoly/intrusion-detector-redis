package com.booking.security.hackertest.detector;


import com.booking.security.hackertest.model.LoginAttempt;
import com.booking.security.hackertest.repository.LoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * A log line will look like this:
 * 1507365137,187.218.83.136,John.Smith,SUCCESS
 */
@Service("hackerDetector")
public class HackerDetectorImpl implements HackerDetector {

    private LogLineParser logLineParser;

    private LoginAttemptRepository loginAttemptRepository;

    @Value("${hackerDetector.maxAllowedFail}")
    private int maxAlowedFailAttempts;

    @Autowired
    public HackerDetectorImpl(LogLineParser logLineParser, LoginAttemptRepository loginAttemptRepository) {
        this.logLineParser = logLineParser;
        this.loginAttemptRepository = loginAttemptRepository;
    }

    @Override
    public String parseLogLine(String line) {
        String detectedIP = "";

        LoginAttempt loginAttempt = logLineParser.parseLogLine(line);

        int failCount = loginAttemptRepository.failedLoginCount(loginAttempt.getIp());

        if (failCount >= maxAlowedFailAttempts - 1) {
            detectedIP = loginAttempt.getIp();
        }

        if (!loginAttempt.isSuccess()) {
            // async call
            loginAttemptRepository.save(loginAttempt);
        }

        return detectedIP;
    }
}
