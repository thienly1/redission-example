package org.example.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Lec09ListQueueStackTest extends BaseTest{

    //setting a list
    @Test
    public void listTest(){
        //lrange number-input 0 -1
        RListReactive<Long> list = this.client.getList("number-input", LongCodec.INSTANCE);

        //create the long list with ascending order
        List<Long> listAdd = LongStream.rangeClosed(1,10)
                        .boxed()
                                .collect(Collectors.toList());
        StepVerifier.create(list.addAll(listAdd).then())
                        .verifyComplete();
        //create an unordered list
//        Mono<Void> listAdd = Flux.range(1, 10)
//                .map(Long::valueOf)
//                .flatMap(list::add)
//                .then();
//        StepVerifier.create(listAdd)
//                .verifyComplete();
        StepVerifier.create(list.size())
                .expectNext(10)
                .verifyComplete();
    }

    //remove  items from a list from the beginning
    @Test
    public void queueTest(){
        RQueueReactive<Long> queue = this.client.getQueue("number-input", LongCodec.INSTANCE);
        Mono<Void> queuePoll = queue.poll()//queue.poll() will remove items from the beginning, since we added 1 to 10 to same list:number-input
                .repeat(3) //repeat the queue.poll() 3 times, so total it will remove 4 times in order (1, 2, 3,4)
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(queuePoll)
                .verifyComplete();
        StepVerifier.create(queue.size())
                .expectNext(6)   //because we make queue.poll() 4 times. the origin list with size 10, now it should have6 items remained in the list
                .verifyComplete();
    }
    //remove items from a list from the ending
    @Test
    public void stackTest(){   //Deque
        RDequeReactive<Long> deque = this.client.getDeque("number-input", LongCodec.INSTANCE);
        Mono<Void> stackPoll = deque.pollLast() //remove items from the end (10)
                .repeat(3)            //repeat 3 times (9,8,7)
                .doOnNext(System.out::println)  // 10,9,8,7
                .then();
        StepVerifier.create(stackPoll)
                        .verifyComplete();
        StepVerifier.create(deque.size())
                .expectNext(2)   //because we make queue.pollLast() 4 times. the origin list with size 6, now it should have 2 items remained in the list
                .verifyComplete();
    }

}
