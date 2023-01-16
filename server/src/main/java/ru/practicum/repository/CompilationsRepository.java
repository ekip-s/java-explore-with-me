package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.compilations.Compilations;

@Repository
public interface CompilationsRepository extends JpaRepository<Compilations, Long> {

    Page<Compilations> findByPinned(Boolean pinned, Pageable pageable);
}
