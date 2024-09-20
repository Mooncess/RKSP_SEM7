package ru.mooncess.rcsp5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mooncess.rcsp5.entities.FileEntity;

@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
}
