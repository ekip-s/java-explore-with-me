package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.publish.Publish;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestDto;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.model.user.User;
import ru.practicum.repository.PublishRepository;
import ru.practicum.repository.RequestsRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.validation.RequestValidationMaster;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RequestsService extends RequestValidationMaster {

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

    public List<RequestDto> applicationsForOtherEvents(long userId) {
        checkId(userId);
        return toDTO(requestsRepository.findByRequesterOrderByCreated(getUser(userId)));
    }

    @Transactional
    public RequestDto addRequest(long userId, long eventId) {
        checkId(userId, eventId);
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
        return new RequestDto(requestsRepository.save(request));
    }

    @Transactional
    public RequestDto cancelRequest(long userId, long requestId) {
        checkId(userId, requestId);
        getUser(userId);
        Request request = getRequest(requestId);
        checkRequesterCancel(userId, request.getRequester().getId());
        if(request.getStatus().equals(RequestStatus.CONFIRMED)) {
            setLimit(request.getEvent(), false);
        }
        request.setStatus(RequestStatus.CANCELED);
        return new RequestDto(requestsRepository.save(request));
    }

    public List<RequestDto>  participationRequests(long eventId, long userId) {
        checkId(userId, eventId);
        checkRequesterParticipation(getPublish(eventId).getInitiator().getId(), userId);
        return toDTO(requestsRepository.findAllByEventId(eventId));
    }

    @Transactional
    public RequestDto approvalApplicationPrivate(long eventId, long userId, long reqId) {
        checkId(userId, eventId, reqId);
        User user = getUser(userId);
        Publish publish = getPublish(eventId);
        Request request = getRequest(reqId);
        checkRequesterApprovalApplication(userId, publish.getInitiator().getId());
        checkRequest(request.getEvent().getId(), eventId);
        checkStatusAndLimit(publish.getState(), publish.getParticipantLimit());
        request.setStatus(RequestStatus.CONFIRMED);
        Request request1 = requestsRepository.save(request);
        if(publish.getParticipantLimit() - 1 == 0) {
            List<Request> requests = requestsRepository.findByEventAndStatus(publish, RequestStatus.PENDING);
            for(Request r: requests) {
                r.setStatus(RequestStatus.REJECTED);
                requestsRepository.save(r);
            }
        }
        setLimit(publish, true);
        return new RequestDto(request1);
    }

    @Transactional
    public RequestDto cancellationApplicationPrivate(long eventId, long userId, long reqId) {
        checkId(userId, eventId, reqId);
        User user = getUser(userId);
        Publish publish = getPublish(eventId);
        Request request = getRequest(reqId);
        checkRequesterApprovalApplication(userId, publish.getInitiator().getId());
        checkRequest(request.getEvent().getId(), eventId);
        request.setStatus(RequestStatus.REJECTED);
        return new RequestDto(requestsRepository.save(request));
    }

    private void setLimit(Publish publish, boolean isSubtraction) {
        if(isSubtraction) {
            publish.setParticipantLimit(publish.getParticipantLimit() - 1);
        } else {
            publish.setParticipantLimit(publish.getParticipantLimit() + 1);
        }
        publishRepository.save(publish);
    }

    private Request getRequest(long requestId) {
        Optional<Request> request = requestsRepository.findById(requestId);
        if(request.isEmpty()) {
            throw new NotFoundException("Ошибка: нет запроса с Id=" + requestId + ".", "");
        } else {
            return request.get();
        }
    }

    private boolean checkRequest(Publish publish, User user) {
        Optional<Request> request = requestsRepository.findByEventAndRequester(publish, user);
        if(request.isEmpty()) {
            return true;
        } else {
            throw new ConflictException("Ошибка: нельзя оставить повторный запрос на событие. ",
                    "Ваш запрос в статусе: " + request.get().getStatus() + ".");
        }
    }

    private Publish getPublish(long id) {
        Optional<Publish> publishOptional = publishRepository.findById(id);
        if(publishOptional.isEmpty()) {
            throw new NotFoundException("Ошибка: нет события с Id=" + id + ".", "");
        } else {
            return publishOptional.get();
        }
    }

    private User getUser(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("Ошибка: нет пользователя с Id=" + userId + ".", "");
        } else {
            return userOptional.get();
        }
    }

    private List<RequestDto> toDTO(List<Request> requests) {
        return requests
                .stream()
                .map(p -> new RequestDto(p))
                .collect(Collectors.toList());
    }
}
