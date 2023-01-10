package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Publish;
import ru.practicum.model.User;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Long>, QuerydslPredicateExecutor<Publish> {

    Page<Publish> findByInitiatorOrderByCreatedOn(User user, Pageable pageable);
}
