package org.example.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RTopicReactive;
import org.redisson.client.codec.StringCodec;

public class Lec12PubSubTest extends BaseTest{

    //publish slack-room hi
    //publish slack-room "how are you, guys?"
    @Test
    public void subscribe1() {

        RTopicReactive topic = this.client.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();
        sleep(600_000);
    }

    @Test
    public void subscribe2() {

        RTopicReactive topic = this.client.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();
        sleep(600_000);
    }
}
