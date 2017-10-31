package com.booking.security.hackertest.repository.redis;


import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.booking.security.hackertest.constants.Constants.MILISECONDS_IN_SECOND;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class RedisKeyUtil {

    // keys are in format LoginAttempt:ip:minute
    private static final String KEY_PATTERN = "loginFail:%s:%s";

    private static final String DATE_FORMAT = "yyyy/MM/dd_HH-mm";

    private static final ThreadLocal<SimpleDateFormat> dateFormatter = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE_FORMAT));

    static String createKey(String ip, long epochSeconds) {
        return String.format(KEY_PATTERN, ip, epochToMinuteKey(epochSeconds));
    }

    static Set<String> createPrecedingMinuteKeys(String ip, Date date, int minutesToKeepLoginData){
        List<String> precedingMinutes = precedingMinutes(date, minutesToKeepLoginData);
        return precedingMinutes.stream()
                .map(m -> String.format(KEY_PATTERN, ip, m)).collect(Collectors.toSet());
    }

    private static String epochToMinuteKey(long epoch) {
        Date date = new Date(epoch * MILISECONDS_IN_SECOND);
        return dateFormatter.get().format(date);
    }

    private static List<String> precedingMinutes(Date date, int minutes) {
        List<String> dates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        for (int i = 0; i < minutes; i++) {
            calendar.add(Calendar.MINUTE, -1);
            dates.add(dateFormatter.get().format(calendar.getTime()));
        }

        return dates;
    }
}
