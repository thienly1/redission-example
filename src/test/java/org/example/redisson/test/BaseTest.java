package org.example.redisson.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

import org.example.redisson.test.config.RedissonConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // so it will not ask me to create methods inside as a static method
public abstract class BaseTest {
    protected RedissonReactiveClient client;
    private final RedissonConfig redissonConfig = new RedissonConfig();

    @BeforeAll
    public void setClient(){
        this.client= this.redissonConfig.getReactiveClient();
    }

    @AfterAll
    public void shutdown(){
        this.client.shutdown();
    }
}
