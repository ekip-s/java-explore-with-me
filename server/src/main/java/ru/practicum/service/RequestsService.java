package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.*;
import ru.practicum.repository.PublishRepository;
import ru.practicum.repository.RequestsRepository;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RequestsService {

    private final RequestsRepository requestsRepository;
    private final PublishRepository publishRepository;
    private final UserRepository userRepository;

    @Autowired
    public RequestsService(RequestsRepository requestsRepository, PublishRepository publishRepository,
                           UserRepository userRepository) {
        this.requestsRepository = requestsRepository;
        this.publishRepository = publishRepository;
        this.userRepository = userRepository;
    }

    public List<Request> applicationsForOtherEvents(long userId) {
        return requestsRepository.findByRequesterOrderByCreated(getUser(userId));
    }

    @Transactional
    public Request addRequest(long userId, long eventId) {
        Publish publish = getPublish(eventId);
        User requester = getUser(userId);
        checkRequest(publish, requester);
        checkRequester(userId, publish.getInitiator().getId());
        checkStatusAndLimit(publish.getState(), publish.getParticipantLimit());
        Request request = new Request(publish, requester);
        if (!publish.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            setLimit(publish, true);
        }
        return requestsRepository.save(request);
    }

    @Transactional
    public Request cancelRequest(long userId, long requestId) {
        getUser(userId);
        Request request = getRequest(requestId);
        checkRequesterCancel(userId, request.getRequester().getId());
        if(request.getStatus().equals(RequestStatus.CONFIRMED)) {
            setLimit(request.getEvent(), false);
        }
        request.setStatus(RequestStatus.CANCELED);
        return requestsRepository.save(request);
    }

    public Request participationRequests(long eventId, long userId) {
        return requestsRepository.findByEventAndRequester(getPublish(eventId), getUser(userId)).get();
    }

    @Transactional
    public void approvalApplicationPrivate(long eventId, long userId, long reqId) {
        User user = getUser(userId);
        Publish publish = getPublish(eventId);
        Request request = getRequest(reqId);
        checkRequesterApprovalApplication(userId, publish.getInitiator().getId());
        checkRequest(request.getEvent().getId(), eventId);
        checkStatusAndLimit(publish.getState(), publish.getParticipantLimit());
        request.setStatus(RequestStatus.CONFIRMED);
        requestsRepository.save(request);
        if(publish.getParticipantLimit() - 1 == 0) {
            List<Request> requests = requestsRepository.findByEventAndStatus(publish, RequestStatus.NEW);
            for(Request r: requests) {
                r.setStatus(RequestStatus.CANCELED);
                requestsRepository.save(r);
            }
        }
        setLimit(publish, true);
    }

    @Transactional
    public void cancellationApplicationPrivate(long eventId, long userId, long reqId) {
        User user = getUser(userId);
        Publish publish = getPublish(eventId);
        Request request = getRequest(reqId);
        checkRequesterApprovalApplication(userId, publish.getInitiator().getId());
        checkRequest(request.getEvent().getId(), eventId);
        request.setStatus(RequestStatus.CANCELED);
    }

    private boolean checkRequest(long eventId, long eventId2) {
        if(eventId == eventId2) {
            return true;
        } else {
            throw new ConflictException("Ошибка: запрос не соответствует событию.");
        }
    }

    private boolean checkRequesterApprovalApplication(long userId, long requesterId) {
        if(userId == requesterId) {
            return true;
        } else {
            throw new ConflictException("Ошибка: нельзя подтверждать/отменять запросы на чужие события.");
        }
    }


    private boolean checkRequesterCancel(long userId, long requesterId) {
        if(userId == requesterId) {
            return true;
        } else {
            throw new ConflictException("Ошибка: нельзя отменить чужой запрос.");
        }
    }

    private void setLimit(Publish publish, boolean isSubtraction) {
        if(isSubtraction) {
            publish.setParticipantLimit(publish.getParticipantLimit() - 1);
        } else {
            publish.setParticipantLimit(publish.getParticipantLimit() + 1);
        }
        publishRepository.save(publish);
    }

    private boolean checkStatusAndLimit(PublishState status, int limit) {
        if(limit > 0 && status.equals(PublishState.PUBLISHED)) {
            return true;
        } else {
            if (limit > 0) {
                throw new ConflictException("Ошибка: событие не опубликовано, оставьте заявку на другое событие.");
            } else {
                throw new ConflictException("Ошибка: нет мест на это событие.");
            }
        }
    }

    private boolean checkRequester(long userId, long requesterId) {
        if(userId == requesterId) {
            throw new ConflictException("Ошибка: нельзя отправить запрос на свое событие. Приходите так.");
        } else {
            return true;
        }
    }

    private Request getRequest(long requestId) {
        Optional<Request> request = requestsRepository.findById(requestId);
        if(request.isEmpty()) {
            throw new NotFoundException("Ошибка: нет запроса с Id=" + requestId + ".");
        } else {
            return request.get();
        }
    }

    private boolean checkRequest(Publish publish, User user) {
        Optional<Request> request = requestsRepository.findByEventAndRequester(publish, user);
        if(request.isEmpty()) {
            return true;
        } else {
            throw new ConflictException("Ошибка: нельзя оставить повторный запрос на событие. " +
                    "Ваш запрос в статусе: " + request.get().getStatus() + ".");
        }
    }

    private Publish getPublish(long id) {
        Optional<Publish> publishOptional = publishRepository.findById(id);
        if(publishOptional.isEmpty()) {
            throw new NotFoundException("Ошибка: нет события с Id=" + id + ".");
        } else {
            return publishOptional.get();
        }
    }

    private User getUser(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("Ошибка: нет пользователя с Id=" + userId + ".");
        } else {
            return userOptional.get();
        }
    }
}
