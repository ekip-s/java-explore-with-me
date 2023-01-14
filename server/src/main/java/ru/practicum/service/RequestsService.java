package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.publish.Publication;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestDto;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.model.user.User;
import ru.practicum.repository.PublicationRepository;
import ru.practicum.repository.RequestsRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.validation.RequestValidationMaster;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RequestsService {

    private final RequestsRepository requestsRepository;
    private final PublicationRepository publishRepository;
    private final UserRepository userRepository;
    private final int SETL_LiMIT = 1; // для изменения лимита участников события в большую или меньшую сторону
    @Autowired
    public RequestsService(RequestsRepository requestsRepository, PublicationRepository publishRepository,
                           UserRepository userRepository) {
        this.requestsRepository = requestsRepository;
        this.publishRepository = publishRepository;
        this.userRepository = userRepository;
    }

    public List<RequestDto> applicationsForOtherEvents(long userId) {
        RequestValidationMaster.checkIds(userId);
        return toDTO(requestsRepository.findByRequesterOrderByCreated(getUser(userId)));
    }

    @Transactional
    public RequestDto addRequest(long userId, long eventId) {
        RequestValidationMaster.checkIds(userId, eventId);
        Publication publish = getPublish(eventId);
        User requester = getUser(userId);
        checkRequest(publish, requester);
        RequestValidationMaster.checkRequester(userId, publish.getInitiator().getId());
        RequestValidationMaster.checkStatusAndLimit(publish.getState(), publish.getParticipantLimit());
        Request request = new Request(publish, requester);
        if (!publish.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            setLimit(publish, true);
        }
        return new RequestDto(requestsRepository.save(request));
    }

    @Transactional
    public RequestDto cancelRequest(long userId, long requestId) {
        RequestValidationMaster.checkIds(userId, requestId);
        getUser(userId);
        Request request = getRequest(requestId);
        RequestValidationMaster.checkRequesterCancel(userId, request.getRequester().getId());
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            setLimit(request.getEvent(), false);
        }
        request.setStatus(RequestStatus.CANCELED);
        return new RequestDto(requestsRepository.save(request));
    }

    public List<RequestDto>  participationRequests(long eventId, long userId) {
        RequestValidationMaster.checkIds(userId, eventId);
        RequestValidationMaster.checkRequesterParticipation(getPublish(eventId).getInitiator().getId(), userId);
        return toDTO(requestsRepository.findAllByEventId(eventId));
    }

    @Transactional
    public RequestDto approveApplicationPrivate(long eventId, long userId, long reqId) {
        RequestValidationMaster.checkIds(userId, eventId, reqId);
        getUser(userId);
        Publication publish = getPublish(eventId);
        Request request = getRequest(reqId);
        RequestValidationMaster.checkRequesterApproveApplication(userId, publish.getInitiator().getId());
        RequestValidationMaster.checkRequest(request.getEvent().getId(), eventId);
        RequestValidationMaster.checkStatusAndLimit(publish.getState(), publish.getParticipantLimit());
        request.setStatus(RequestStatus.CONFIRMED);
        Request  newRequest = requestsRepository.save(request);
        if (publish.getParticipantLimit() - 1 == 0) {
            List<Request> requests = requestsRepository.findByEventAndStatus(publish, RequestStatus.PENDING);
            for (Request r: requests) {
                r.setStatus(RequestStatus.REJECTED);
                requestsRepository.save(r);
            }
        }
        setLimit(publish, true);
        return new RequestDto( newRequest);
    }

    @Transactional
    public RequestDto cancelApplicationPrivate(long eventId, long userId, long reqId) {
        RequestValidationMaster.checkIds(userId, eventId, reqId);
        getUser(userId);
        Publication publish = getPublish(eventId);
        Request request = getRequest(reqId);
        RequestValidationMaster.checkRequesterApproveApplication(userId, publish.getInitiator().getId());
        RequestValidationMaster.checkRequest(request.getEvent().getId(), eventId);
        request.setStatus(RequestStatus.REJECTED);
        return new RequestDto(requestsRepository.save(request));
    }

    private void setLimit(Publication publish, boolean isSubtraction) {
        if (isSubtraction) {
            publish.setParticipantLimit(publish.getParticipantLimit() - SETL_LiMIT);
        } else {
            publish.setParticipantLimit(publish.getParticipantLimit() + SETL_LiMIT);
        }
        publishRepository.save(publish);
    }

    private Request getRequest(long requestId) {
        return requestsRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Ошибка: нет запроса с Id=" + requestId + ".", ""));
    }

    private boolean checkRequest(Publication publish, User user) {
        Optional<Request> request = requestsRepository.findByEventAndRequester(publish, user);
        if (request.isEmpty()) {
            return true;
        } else {
            throw new ConflictException("Ошибка: нельзя оставить повторный запрос на событие. ",
                    "Ваш запрос в статусе: " + request.get().getStatus() + ".");
        }
    }

    private Publication getPublish(long id) {
        return publishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ошибка: нет события с Id=" + id + ".", ""));
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Ошибка: нет пользователя с Id=" + userId + ".", ""));
    }

    private List<RequestDto> toDTO(List<Request> requests) {
        return requests
                .stream()
                .map(RequestDto::new)
                .collect(Collectors.toList());
    }
}
