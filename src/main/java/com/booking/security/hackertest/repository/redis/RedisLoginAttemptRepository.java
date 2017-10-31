package com.booking.security.hackertest.repository.redis;

import com.booking.security.hackertest.model.LoginAttempt;
import com.booking.security.hackertest.repository.LoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.booking.security.hackertest.constants.Constants.MILISECONDS_IN_SECOND;
import static com.booking.security.hackertest.constants.Constants.SECONDS_IN_MINUTE;
import static com.booking.security.hackertest.repository.redis.RedisKeyUtil.createKey;
import static com.booking.security.hackertest.repository.redis.RedisKeyUtil.createPrecedingMinuteKeys;

/**
 * Keeps failed login attempts in Redis as Key(ip:minute) : Set(loginTimestamp).
 * This approach give us the ability to expire keys by time.
 */
@Repository
public class RedisLoginAttemptRepository implements LoginAttemptRepository {

    private RedisTemplate<String, Long> redisTemplate;
    private SetOperations<String, Long> setOperations;

    @Value("${hackerDetector.minutesToKeepLoginData}")
    private int minutesToKeepLoginData;

    @Autowired
    public RedisLoginAttemptRepository(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        setOperations = redisTemplate.opsForSet();
    }

    @Override
    public void save(LoginAttempt loginAttempt) {
        if (loginAttempt.isSuccess())
            return;
        String key = createKey(loginAttempt.getIp(), loginAttempt.getTime());
        boolean keyExists = redisTemplate.hasKey(key);
        setOperations.add(key, loginAttempt.getTime());
        if (!keyExists) {
            redisTemplate.expire(key, minutesToKeepLoginData, TimeUnit.MINUTES);
        }
    }

    @Override
    public int failedLoginCount(String ip) {
        Date now = new Date();
        long nowInEpoch = now.getTime() / MILISECONDS_IN_SECOND;
        long timeThreshold = nowInEpoch - minutesToKeepLoginData * SECONDS_IN_MINUTE;

        String key = createKey(ip, nowInEpoch);
        Set<String> precedingKeys = createPrecedingMinuteKeys(ip, now, minutesToKeepLoginData);

        Set<Long> timestamps = setOperations.union(key, precedingKeys);

        return timestamps.stream()
                .filter(t -> t >= timeThreshold).collect(Collectors.toSet()).size();
    }
}
