package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import ru.practicum.model.publish.Publication;
import ru.practicum.model.user.User;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long>, QuerydslPredicateExecutor<Publication> {

    Page<Publication> findByInitiatorOrderByCreatedOn(User user, Pageable pageable);
}
