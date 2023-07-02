package org.example.redisson.test;

import org.example.redisson.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lec07MapCacheTest extends BaseTest{

    @Test
    public void mapCacheTest(){
        RMapCacheReactive<Integer, Student> mapCache = this.client.getMapCache("users:cache", new TypedJsonJacksonCodec(Integer.class, Student.class));
        Student student1 = new Student("sam", 10, "atlanda", List.of(1,2,3));
        Student student2 = new Student("ly", 20, "växjö", List.of(10,20,30));
        Mono<Student> st1 = mapCache.put(1, student1, 5, TimeUnit.SECONDS); // ex(ttl): 5 seconds
        Mono<Student> st2 = mapCache.put(2, student2, 10, TimeUnit.SECONDS);
        StepVerifier.create(st1.concatWith(st2).then()).verifyComplete();
        sleep(3000);
        mapCache.get(1).doOnNext(System.out::println).subscribe(); //Student(name=sam, age=10, city=atlanda, marks=[1, 2, 3])
        mapCache.get(2).doOnNext(System.out::println).subscribe();//Student(name=ly, age=20, city=växjö, marks=[10, 20, 30])

        sleep(3000);
        mapCache.get(1).doOnNext(System.out::println).subscribe(); //expired, so it didn't print out
        mapCache.get(2).doOnNext(System.out::println).subscribe(); //Student(name=ly, age=20, city=växjö, marks=[10, 20, 30])
    }
}
