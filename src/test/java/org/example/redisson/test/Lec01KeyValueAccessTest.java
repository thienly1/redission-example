package org.example.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class Lec01KeyValueAccessTest extends BaseTest {

    @Test
    public void keyValueAccessTest(){
        RBucketReactive<String> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);//bucket just an object,a key in redis
        Mono<Void> set = bucket.set("sam");
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }
    @Test
    public void keyValueExpiryTest(){
        RBucketReactive<String> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);//bucket just an object,a key in redis
        Mono<Void> set = bucket.set("sam", 10, TimeUnit.SECONDS); //set value:sam, ex:10, unit:second
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

        //extend
        sleep(5000);
        Mono<Boolean> mono = bucket.expire(60, TimeUnit.SECONDS); //set expire : 60 seconds
        StepVerifier.create(mono)
                .expectNext(true)
                .verifyComplete();
        //access expiration time
        Mono<Void> ttl = bucket.remainTimeToLive()
                .doOnNext(System.out::println) //print out time to live (ttl)
                .then();
        StepVerifier.create(ttl)
                .verifyComplete();
    }


}
