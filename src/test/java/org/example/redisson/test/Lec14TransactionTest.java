package org.example.redisson.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec14TransactionTest extends BaseTest{
    private RBucketReactive<Long> user1Balance;
    private RBucketReactive<Long> user2Balance;

    @BeforeAll
    public void accountSetup(){
        this.user1Balance= this.client.getBucket("user:1:balance", LongCodec.INSTANCE);
        this.user2Balance= this.client.getBucket("user:2:balance", LongCodec.INSTANCE);
        Mono<Void> mono = user1Balance.set(100L)
                .then(user2Balance.set(0L))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();

    }
    @AfterAll
    public void accountBalanceStatus(){
        Mono<Void> mono = Flux.zip(this.user1Balance.get(), this.user2Balance.get()) //[50,50], wrap it in a zip
                .doOnNext(System.out::println)  //[50,50]
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void nonTransactionTest(){
        this.transfer(user1Balance, user2Balance, 50)
                .thenReturn(0) //just simulate an error
                .map(i ->(5/i))  //some error here, don't care about it
                .doOnError(System.out::println)//java.lang.ArithmeticException: / by zero
                .subscribe();
        sleep(1000);
    }

    private Mono<Void> transfer(RBucketReactive<Long> fromAccount, RBucketReactive<Long> toAccount, int amount){
       return Flux.zip(fromAccount.get(), toAccount.get())
                .filter(t -> t.getT1() >= amount)
                .flatMap(t -> fromAccount.set(t.getT1() -amount).thenReturn(t))
                .flatMap(t -> toAccount.set(t.getT2() + amount))
                .then();
    }
}
