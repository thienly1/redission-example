package org.example.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec04BucketsAsMap extends BaseTest{
    // user:1:name
    // user:2:name
    // user:3:name
    @Test
    public void bucketsAsMap(){
        //set 3 keys with 3 values first (can do by test or in cli), then with this test, we put these keys in a Map.
        Mono<Void> mono = this.client.getBuckets(StringCodec.INSTANCE)
                .get("user:1:name", "user:2:name", "user:3:name","user:4:name" )
                .doOnNext(System.out::println)  //if the fourth key "user:4:name" doesn't exist, it will ignore this key and print out: {user:1:name=sam, user:2:name=jake, user:3:name=mike}
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
    }
}
