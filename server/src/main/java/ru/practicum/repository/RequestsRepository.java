package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.publish.Publication;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestsRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByEventId(long id);

    Optional<Request> findByEventAndRequester(Publication event, User user);

    List<Request> findByRequesterOrderByCreated(User user);

    List<Request> findByEventAndStatus(Publication publish, RequestStatus status);
}
