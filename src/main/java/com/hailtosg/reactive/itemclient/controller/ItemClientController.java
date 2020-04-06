package com.hailtosg.reactive.itemclient.controller;

import com.hailtosg.reactive.itemclient.domain.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.hailtosg.reactive.itemclient.constants.ItemConstants.ITEMS_END_POINT_V1;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping("client/retrieve")
    public Flux<Item> getAllUsingRetrieve() {
        return webClient
                .get()
                .uri(ITEMS_END_POINT_V1)
                .retrieve()
                .bodyToFlux(Item.class)
                .log("Items in Client Project retrieve");
    }

    @GetMapping("client/exchange")
    public Flux<Item> getAllUsingExchange() {
        return webClient
                .get()
                .uri(ITEMS_END_POINT_V1)
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class))
                .log("Items in Client Project exchange");
    }

    @GetMapping("client/retrieve/{id}")
    public Mono<Item> getOneUsingRetrieve(@PathVariable String id) {
        return webClient
                .get()
                .uri(ITEMS_END_POINT_V1+"/{id}", id)
                .retrieve()
                .bodyToMono(Item.class)
                .log("single item in Client Project retrieve");
    }

    @GetMapping("client/retrieve/{id}")
    public Mono<Item> getOneUsingExchange(@PathVariable String id) {
        return webClient
                .get()
                .uri(ITEMS_END_POINT_V1+"/{id}", id)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log("single item in Client Project exchange");
    }
}
