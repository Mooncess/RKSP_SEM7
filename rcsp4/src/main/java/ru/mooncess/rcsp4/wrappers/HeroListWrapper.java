package ru.mooncess.rcsp4.wrappers;

import ru.mooncess.rcsp4.entities.Hero;

import java.util.List;
public class HeroListWrapper {
    private List<Hero> heroes;
    public List<Hero> getHeroes() {
        return heroes;
    }
    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes;
    }
}
