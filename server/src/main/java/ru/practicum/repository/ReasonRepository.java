package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.reason.Reason;

import java.util.Optional;


@Repository
public interface ReasonRepository extends JpaRepository<Reason, Long> {

    Optional<Reason> findByName(String name);
}
