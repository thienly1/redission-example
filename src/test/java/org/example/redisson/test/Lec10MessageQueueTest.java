package org.example.redisson.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDequeReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec10MessageQueueTest extends BaseTest{
    private RBlockingDequeReactive<Long> msgQueue;

    //set a list "message-queue
    @BeforeAll
    public void setupQueue(){
        msgQueue = this.client.getBlockingDeque("message-queue", LongCodec.INSTANCE);
    }

    //take elements(items) out of the list, run this test first, nothing happens because the list is empty now
    @Test
    public void consumer1(){
        this.msgQueue.takeElements()
                .doOnNext(i-> System.out.println("Consumer 1: " +i))
                .doOnError(System.out::println)
                .subscribe();
        sleep(600000);
    }

    //take elements(items) out of the list, run this test thirdly, it starts to remove items from the list, now we have 2 services that removing items out of the list from the last, so 1 will remove even number, 1 will remove odd number
    @Test
    public void consumer2(){
        this.msgQueue.takeElements()
                .doOnNext(i-> System.out.println("Consumer 2: " +i))
                .doOnError(System.out::println)
                .subscribe();
        sleep(600000);
    }

    //add the items to the list, range from 1-100, run this test secondly. it will add items to the list, while its working, the first test starts to remove items out of the list
    @Test
    public void producer(){
        Mono<Void> mono = Flux.range(1, 100)
                .delayElements(Duration.ofMillis(500))
                .doOnNext(i -> System.out.println("Iam going to add " + i))
                .flatMap(i -> this.msgQueue.add(Long.valueOf(i)))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
    }

}
