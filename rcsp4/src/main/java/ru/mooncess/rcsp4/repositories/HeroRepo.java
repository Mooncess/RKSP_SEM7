package ru.mooncess.rcsp4.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mooncess.rcsp4.entities.Hero;

@Repository
public interface HeroRepo extends JpaRepository<Hero, Long> {
    Hero findHeroById(Long id);
}

