package com.booking.security.hackertest.repository.redis;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Set;

import static com.booking.security.hackertest.constants.Constants.MILISECONDS_IN_SECOND;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class RedisKeyUtilTest {

    @Test
    public void shouldFindPrecedingMinuteKeys(){
        // given
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 01);
        int minuteCount = 5;
        String ip = "anyIp";

        // when
        Set<String> minutesKeys = RedisKeyUtil.createPrecedingMinuteKeys(ip, calendar.getTime(), minuteCount);

        // then
        assertThat(minutesKeys, hasSize(minuteCount));
        assertThat(minutesKeys, hasItems("loginFail:anyIp:2017/10/01_23-00", "loginFail:anyIp:2017/10/01_22-59", "loginFail:anyIp:2017/10/01_22-58", "loginFail:anyIp:2017/10/01_22-57", "loginFail:anyIp:2017/10/01_22-56"));
    }

    @Test
    public void shouldFormatDate() {
        // given
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 1);
        String ip = "anyIp";

        // when
        String key = RedisKeyUtil.createKey(ip, calendar.getTime().getTime() / MILISECONDS_IN_SECOND);

        // then
        Assert.assertEquals("loginFail:anyIp:2017/10/01_23-01", key);
    }
}
