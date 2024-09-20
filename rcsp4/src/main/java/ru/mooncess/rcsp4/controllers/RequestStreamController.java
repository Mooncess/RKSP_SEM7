package ru.mooncess.rcsp4.controllers;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mooncess.rcsp4.entities.Hero;

@RestController
@RequestMapping("/api/heroes")
public class RequestStreamController {
    private final RSocketRequester rSocketRequester;
    @Autowired
    public RequestStreamController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }
    @GetMapping
    public Publisher<Hero> getHeroes() {
        return rSocketRequester
                .route("getHeroes")
                .data(new Hero())
                .retrieveFlux(Hero.class);
    }
}
