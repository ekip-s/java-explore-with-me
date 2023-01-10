package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Publish;
import ru.practicum.model.Request;
import ru.practicum.model.RequestStatus;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestsRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByEventAndRequester(Publish event, User user);

    List<Request> findByRequesterOrderByCreated(User user);

    List<Request> findByEventAndStatus(Publish publish, RequestStatus status);
}
