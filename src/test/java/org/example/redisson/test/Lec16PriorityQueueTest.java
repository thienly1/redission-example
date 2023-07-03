package org.example.redisson.test;

import org.example.redisson.test.assignment.Category;
import org.example.redisson.test.assignment.PriorityQueue;
import org.example.redisson.test.assignment.UserOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec16PriorityQueueTest extends BaseTest {

    private PriorityQueue priorityQueue;
    @BeforeAll
    public void setupQueue(){
        RScoredSortedSetReactive<UserOrder> sortedSet = this.client.getScoredSortedSet("user:order:queue", new TypedJsonJacksonCodec(UserOrder.class));
        this.priorityQueue = new PriorityQueue(sortedSet);
    }
    @Test
    public void producer(){
        //add 5 members to set then print out priority as order in enum Category
//        UserOrder u1 = new UserOrder(1, Category.GUEST);
//        UserOrder u2 = new UserOrder(2, Category.STD);
//        UserOrder u3 = new UserOrder(3, Category.PRIME);
//        UserOrder u4 = new UserOrder(4, Category.STD);
//        UserOrder u5 = new UserOrder(5, Category.GUEST);
//        Mono<Void> mono = Flux.just(u1, u2, u3, u4, u5)
//                .flatMap(this.priorityQueue::add)
//                .then();
//        StepVerifier.create(mono)
//                .verifyComplete();

        //iteration in adding 5 members to set, then consumer take Items and print out with priority.
        Flux.interval(Duration.ofSeconds(1)) //Long type
                .map(l -> l.intValue()*5) //reverse to integer *5 so the id will be not conflicted
                .doOnNext(i -> {  //with each integer of 1 second duration
                    UserOrder u1 = new UserOrder(i+1, Category.GUEST);
                    UserOrder u2 = new UserOrder(i+2, Category.STD);
                    UserOrder u3 = new UserOrder(i+3, Category.PRIME);
                    UserOrder u4 = new UserOrder(i+4, Category.STD);
                    UserOrder u5 = new UserOrder(i+5, Category.GUEST);
                    Mono<Void> mono = Flux.just(u1, u2, u3, u4, u5)
                            .flatMap(this.priorityQueue::add)
                            .then();
                    StepVerifier.create(mono)
                            .verifyComplete();
                }).subscribe();
        sleep(60_000);
    }
    @Test
    public void consumer(){
        this.priorityQueue.takeItems()
                .delayElements(Duration.ofMillis(500))  //duration of 0.5 second. So it will print out 2 UserOrders in 1 second of producer( Priority is PRIME first, then STD second). if you stop producer, it will give priority to print out(sorted out) STD first, because it printed out all PRIME already. then when finish with STD, it will print out the rest with GUEST.
                .doOnNext(System.out::println)
                .subscribe();
        sleep(600_000);
    }

}
