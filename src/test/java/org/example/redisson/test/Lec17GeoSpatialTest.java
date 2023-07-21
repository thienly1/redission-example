package org.example.redisson.test;

import org.example.redisson.test.dto.Restaurant;
import org.example.redisson.test.util.RestaurantUtil;
import org.junit.jupiter.api.Test;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeoReactive;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.api.geo.OptionalGeoSearch;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

public class Lec17GeoSpatialTest extends BaseTest{

    //add the restaurant.json file to redis
    @Test
    public void add(){
        RGeoReactive<Restaurant> geo = this.client.getGeo("restaurants", new TypedJsonJacksonCodec(Restaurant.class));
        Mono<Void> mono = Flux.fromIterable(RestaurantUtil.getRestaurants())
                .flatMap(r -> geo.add(r.getLongitude(), r.getLatitude(), r))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
            /*
    "latitude": 33.762134,
    "longitude": -96.581587,
    */
        OptionalGeoSearch radius = GeoSearchArgs.from(-96.581587, 33.762134).radius(3, GeoUnit.MILES);
        geo.search(radius)
                .flatMapIterable(Function.identity())
                .doOnNext(System.out::println)
                .subscribe();
    }


}
