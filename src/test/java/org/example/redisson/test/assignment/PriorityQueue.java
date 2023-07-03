package org.example.redisson.test.assignment;

import org.redisson.api.RScoredSortedSetReactive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PriorityQueue {
    RScoredSortedSetReactive<UserOrder> queue;

    public PriorityQueue(RScoredSortedSetReactive<UserOrder> queue) {
        this.queue = queue;
    }
    public Mono<Void> add(UserOrder userOrder){
        return this.queue.add(userOrder.getCategory().ordinal(), userOrder)
                .then();
    }

    public Flux<UserOrder> takeItems(){
        return this.queue.takeFirstElements()
                .limitRate(1);
    }
}
