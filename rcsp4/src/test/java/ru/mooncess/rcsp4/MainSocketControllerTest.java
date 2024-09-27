package ru.mooncess.rcsp4;

import io.rsocket.frame.decoder.PayloadDecoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.mooncess.rcsp4.entities.Hero;
import ru.mooncess.rcsp4.repositories.HeroRepo;
import reactor.core.publisher.Flux;


import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MainSocketControllerTest {
    @Autowired
    private HeroRepo heroRepository;
    private RSocketRequester requester;
    @BeforeEach
    public void setup() {
        requester = RSocketRequester.builder()
                .rsocketStrategies(builder -> builder.decoder(new
                        Jackson2JsonDecoder()))
                .rsocketStrategies(builder -> builder.encoder(new
                        Jackson2JsonEncoder()))
                .rsocketConnector(connector -> connector
                        .payloadDecoder(PayloadDecoder.ZERO_COPY)
                        .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp("localhost", 5200);
    }
    @AfterEach
    public void cleanup() {
        requester.dispose();
    }
    @Test
    public void testGetHero() {
        Hero hero = new Hero();
        hero.setName("TestHero");
        hero.setAge(2);
        hero.setWeapon("Sword");
        hero.setSpeciality("Paladin");
        Hero savedHero = heroRepository.save(hero);
        Mono<Hero> result = requester.route("getHero")
                .data(savedHero.getId())
                .retrieveMono(Hero.class);
        assertNotNull(result.block());
    }
    @Test
    public void testAddHero() {
        Hero hero = new Hero();
        hero.setName("TestHero");
        hero.setAge(2);
        hero.setWeapon("Sword");
        hero.setSpeciality("Paladin");
        Mono<Hero> result = requester.route("addHero")
                .data(hero)
                .retrieveMono(Hero.class);
        Hero savedHero = result.block();
        assertNotNull(savedHero);
        assertNotNull(savedHero.getId());
        assertTrue(savedHero.getId() > 0);
    }
    @Test
    public void testGetHeroes() {
        Flux<Hero> result = requester.route("getHeroes")
                .retrieveFlux(Hero.class);
        assertNotNull(result.blockFirst());
    }
    @Test
    public void testDeleteHero() {
        Hero hero = new Hero();
        hero.setName("TestHero");
        hero.setAge(2);
        hero.setWeapon("Sword");
        hero.setSpeciality("Paladin");
        Hero savedHero = heroRepository.save(hero);
        Mono<Void> result = requester.route("deleteHero")
                .data(savedHero.getId())
                .send();
        result.block();
        Hero deletedHero = heroRepository.findHeroById(savedHero.getId());
        assertNotSame(deletedHero, savedHero);
    }
}