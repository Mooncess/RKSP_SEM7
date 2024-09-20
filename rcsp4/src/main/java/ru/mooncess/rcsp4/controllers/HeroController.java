package ru.mooncess.rcsp4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mooncess.rcsp4.entities.Hero;
import ru.mooncess.rcsp4.repositories.HeroRepo;

import java.util.Objects;

@Controller
public class HeroController {
    private final HeroRepo heroRepository;
    @Autowired
    public HeroController(HeroRepo heroRepository) {
        this.heroRepository = heroRepository;
    }
    @MessageMapping("getHero")
    public Mono<Hero> getHero(Long id) {
        return Mono.justOrEmpty(heroRepository.findHeroById(id));
    }
    @MessageMapping("addHero")
    public Mono<Hero> addHero(Hero hero) {
        return Mono.justOrEmpty(heroRepository.save(hero));
    }
    @MessageMapping("getHeroes")
    public Flux<Hero> getHeroes() {
        return Flux.fromIterable(heroRepository.findAll());
    }

    @MessageMapping("deleteHero")
    public Mono<Void> deleteHero(Long id){
        Hero hero = heroRepository.findHeroById(id);
        heroRepository.delete(hero);
        return Mono.empty();
    }
//    @MessageMapping("heroChannel")
//    public Flux<Hero> heroChannel(Flux<Hero> heroes){
//        // block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-nio-3
//        // return
//        Flux.fromIterable(heroRepository.saveAll(Objects.requireNonNull(heroes.collectList().block())));
//        // Используем Mono.fromCallable, чтобы асинхронно вызвать метод catRepository::save для каждого кота и вернуть результаты как Flux.
//        return heroes.flatMap(hero -> Mono.fromCallable(() ->
//                        heroRepository.save(hero)))
//                .collectList()
//                .flatMapMany(Flux::fromIterable);
//    }

    @MessageMapping("heroChannel")
    public Flux<Hero> heroChannel(Flux<Hero> heroes) {
        return heroes.flatMap(hero ->
                Mono.fromCallable(() -> heroRepository.save(hero))
        );
    }
}

