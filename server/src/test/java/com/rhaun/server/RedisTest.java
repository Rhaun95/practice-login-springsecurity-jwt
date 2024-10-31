package com.rhaun.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@ActiveProfiles
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // None to use real DB
public class RedisTest {

    @Autowired
    RedisTemplate<String, String>  redisTemplate;

    @Test
    void redisConnectionTest() {
        final String key = "Test";
        final String data = "1";

        redisTemplate.opsForValue().set(key, data);

        final String s = redisTemplate.opsForValue().get(key);
        Assertions.assertEquals(s, data);
    }

    @Test
    void redisExpireTest() throws InterruptedException{
        final String key = "Test";
        final String data = "1a";

        redisTemplate.opsForValue().set(key, data);
        final Boolean expire = redisTemplate.expire(key, 5, TimeUnit.SECONDS);
        Thread.sleep(6000);
        final String s = redisTemplate.opsForValue().get(key);

        Assertions.assertTrue(expire);
        Assertions.assertNull(s);
    }

}
