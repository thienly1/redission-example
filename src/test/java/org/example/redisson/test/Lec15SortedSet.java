package org.example.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

public class Lec15SortedSet extends BaseTest{

    @Test
    public void sortedSet(){
        RScoredSortedSetReactive<String> sortedSet = this.client.getScoredSortedSet("student:score", StringCodec.INSTANCE);

        Mono<Void> mono = sortedSet.addScore("sam", 12.25)//add score to this "sam" if existed, if not, add new person with these inf
                .then(sortedSet.add(23.25, "mike"))//add this score and member to this set.
                .then(sortedSet.addScore("jake", 7))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
        sortedSet.entryRange(0, 1) // ascending order or entryRangeReversed(1,0) with descending order
                .flatMapIterable(Function.identity())  //flux
                .map(se -> se.getScore() + " : " + se.getValue())
                .doOnNext(System.out::println)
                .subscribe();
        sleep(1000);

        //run this test first time, we got: 7.0 : jake 12.25 : sam
        //run this test one more time, we got :   14.0 : jake  23.25 : mike, because we had these members in the set, just add score with addScore() and reset with add().
    }
}
