package ru.mooncess.rcsp4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.mooncess.rcsp4.entities.Hero;
import ru.mooncess.rcsp4.wrappers.HeroListWrapper;

import java.util.List;
@RestController
@RequestMapping("/api/heroes")
public class ChannelController {
    private final RSocketRequester rSocketRequester;
    @Autowired
    public ChannelController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }
    @PostMapping("/exp")
    public Flux<Hero> addHeroesMultiple(@RequestBody HeroListWrapper heroListWrapper) {
        List<Hero> heroList = heroListWrapper.getHeroes();
        Flux<Hero> heroes = Flux.fromIterable(heroList);
        return rSocketRequester
                .route("heroChannel")
                .data(heroes)
                .retrieveFlux(Hero.class);
    }

}
