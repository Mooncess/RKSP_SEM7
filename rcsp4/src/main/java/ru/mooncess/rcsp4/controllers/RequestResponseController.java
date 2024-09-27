package ru.mooncess.rcsp4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.mooncess.rcsp4.entities.Hero;

@RestController
@RequestMapping("/api/heroes")
public class RequestResponseController {
    private final RSocketRequester rSocketRequester;
    @Autowired
    public RequestResponseController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }
    @GetMapping("/{id}")
    public Mono<Hero> getHero(@PathVariable Long id) {
        return rSocketRequester
                .route("getHero")
                .data(id)
                .retrieveMono(Hero.class);
    }
    @PostMapping
    public Mono<Hero> addHero(@RequestBody Hero hero) {
        return rSocketRequester
                .route("addHero")
                .data(hero)
                .retrieveMono(Hero.class);
    }
}
