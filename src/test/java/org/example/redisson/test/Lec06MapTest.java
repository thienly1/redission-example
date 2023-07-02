package org.example.redisson.test;

import org.example.redisson.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

public class Lec06MapTest extends BaseTest{

    @Test
    public void mapTest(){
        RMapReactive<String, String> map = this.client.getMap("user:1", StringCodec.INSTANCE);
        Mono<String> name = map.put("name", "sam");
        Mono<String> age = map.put("age", "10");
        Mono<String> city = map.put("city", "atlanda");

        StepVerifier.create(name.concatWith(age).concatWith(city).then()).verifyComplete();
        //run the test then check on cli
    }
    @Test
    public void mapTest2(){
        RMapReactive<String, String> map = this.client.getMap("user:2", StringCodec.INSTANCE);
        Map<String, String> javaMap = Map.of(
                "name", "jake",
                "age", "30",
                "city", "hanoi"
        );
        StepVerifier.create(map.putAll(javaMap).then()).verifyComplete();
        //run the test then check on cli
        //hgetall user:2
        //hget user:2 name
    }
    @Test
    public void mapTest3(){
        //Map<Integer, Student>
        RMapReactive<Integer, Student> map = this.client.getMap("users", new TypedJsonJacksonCodec(Integer.class, Student.class));
        Student student1 = new Student("sam", 10, "atlanda", List.of(1,2,3));
        Student student2 = new Student("ly", 20, "växjö", List.of(10,20,30));
        Mono<Student> mono1 = map.put(1, student1);
        Mono<Student> mono2 = map.put(2, student2);
        StepVerifier.create(mono1.concatWith(mono2).then()).verifyComplete();
        // - Run the test then check on Cli
        //keys *
        //hgetall users
        //hget users 1
    }

}
