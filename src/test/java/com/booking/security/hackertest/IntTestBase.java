package com.booking.security.hackertest;

import com.booking.security.hackertest.config.RedisConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {RedisConfig.class, HackertestApplication.class})
@PropertySource("classpath:application-test.properties")
public abstract class IntTestBase {

}
