package com.hailtosg.reactive.itemclient.controller;

import com.hailtosg.reactive.itemclient.domain.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.hailtosg.reactive.itemclient.constants.ItemConstants.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping(CLIENT_RETRIEVE)
    public Flux<Item> getAllUsingRetrieve() {
        return webClient
                .get()
                .uri(ITEMS_END_POINT_V1)
                .retrieve()
                .bodyToFlux(Item.class)
                .log("Items in Client Project retrieve");
    }

    @GetMapping(CLIENT_EXCHANGE)
    public Flux<Item> getAllUsingExchange() {
        return webClient
                .get()
                .uri(ITEMS_END_POINT_V1)
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class))
                .log("Items in Client Project exchange");
    }

    @GetMapping(CLIENT_RETRIEVE + ID_SUFFIX)
    public Mono<Item> getOneUsingRetrieve(@PathVariable String id) {
        return webClient
                .get()
                .uri(ITEMS_END_POINT_V1 + ID_SUFFIX, id)
                .retrieve()
                .bodyToMono(Item.class)
                .log("single item in Client Project retrieve");
    }

    @GetMapping(CLIENT_EXCHANGE + ID_SUFFIX)
    public Mono<Item> getOneUsingExchange(@PathVariable String id) {
        return webClient
                .get()
                .uri(ITEMS_END_POINT_V1 + ID_SUFFIX, id)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("single item in Client Project exchange");
    }

    @PostMapping(CLIENT_RETRIEVE + "/create")
    public Mono<Item> createOneUsingRetrieve(@RequestBody Item item) {
        return webClient
                .post()
                .uri(ITEMS_END_POINT_V1, item)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Created single item in Client Project retrieve");
    }

    @PostMapping(CLIENT_EXCHANGE + "/create")
    public Mono<Item> createOneUsingExchange(@RequestBody Item item) {
        return webClient
                .post()
                .uri(ITEMS_END_POINT_V1, item)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("Created single item in Client Project exchange");
    }
    @PutMapping(CLIENT_RETRIEVE + ID_SUFFIX)
    public Mono<Item> updateOneUsingRetrieve(@PathVariable String id, @RequestBody Item item) {
        return webClient
                .put()
                .uri(ITEMS_END_POINT_V1 + ID_SUFFIX, id, item)
                .body(Mono.just(item), Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("single item updated in Client Project retrieve");
    }

    @PutMapping(CLIENT_EXCHANGE + ID_SUFFIX)
    public Mono<Item> updateOneUsingExchange(@PathVariable String id, @RequestBody Item item) {
        return webClient
                .put()
                .uri(ITEMS_END_POINT_V1 + ID_SUFFIX, id, item)
                .body(Mono.just(item), Item.class)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("single item updated in Client Project exchange");
    }
}
