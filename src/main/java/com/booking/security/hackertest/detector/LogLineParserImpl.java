package com.booking.security.hackertest.detector;

import com.booking.security.hackertest.model.LoginAttempt;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.booking.security.hackertest.constants.Constants.SUCCESS;

@Component
@Log
public class LogLineParserImpl implements LogLineParser {

    @Override
    public LoginAttempt parseLogLine(String line) {
        if (StringUtils.isEmpty(line)) {
            throw new IllegalArgumentException("Log line can not be empty");
        }

        try {
            String[] logParts = line.trim().split("\\s*,\\s*");

            long epochTime = Long.parseLong(logParts[0]);
            String ip = logParts[1];
            String username = logParts[2];
            boolean success = logParts[3].equals(SUCCESS);

            return LoginAttempt.builder()
                    .time(epochTime)
                    .ip(ip)
                    .username(username)
                    .success(success)
                    .build();
        } catch (RuntimeException rx) {
            log.warning("Log parse error for line " + line);
            throw new IllegalArgumentException("Can not parse log line " + line);
        }
    }
}
