package ru.practicum.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.*;
import ru.practicum.model.publish.*;
import ru.practicum.model.request.QRequest;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.model.user.User;
import ru.practicum.repository.LocationRepository;
import ru.practicum.repository.PublishRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.validation.PublishValidationMaster;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PublishService extends PublishValidationMaster {

    private final UserRepository userRepository;
    private final PublishRepository publishRepository;
    private final LocationRepository locationRepository;

    public PublishService(UserRepository userRepository,
                          PublishRepository publishRepository,
                          LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.publishRepository = publishRepository;
        this.locationRepository = locationRepository;
    }

    public List<AnswerPublishDTO> getUserEvents(long userId,int from, int size) {
        return toDTO(publishRepository.findByInitiatorOrderByCreatedOn(getUser(userId), checkPaginationParams(from, size))
                .stream()
                .collect(Collectors.toList()));
    }

    @Transactional
    public AnswerPublishDTO addUserEvents(long userId, Publish publish) {
        checkId(userId);
        return new AnswerPublishDTO(publishRepository.save(essenceFilling(publish, userId)));
    }

    @Transactional
    public AnswerPublishDTO updateUserEvents(long userId, Publish publish, long eventId) {
        checkId(userId, eventId);
        Publish originalPublish = getPublish(eventId);
        checkUser(userId, originalPublish.getInitiator().getId());
        checkStatus(originalPublish.getState());
        if(publish.getAnnotation() != null) {
            originalPublish.setAnnotation(publish.getAnnotation());
        }
        if(publish.getCategory() != null) {
            originalPublish.setCategory(publish.getCategory());
        }
        if(publish.getDescription() != null) {
            originalPublish.setDescription(publish.getDescription());
        }
        if(publish.getEventDate() != null) {
            originalPublish.setEventDate(publish.getEventDate());
        }
        if(publish.getPaid() != null) {
            originalPublish.setPaid(publish.getPaid());
        }
        if(publish.getParticipantLimit() != null) {
            originalPublish.setParticipantLimit(publish.getParticipantLimit());
        }
        if(publish.getTitle() != null) {
            originalPublish.setTitle(publish.getTitle());
        }
        originalPublish.setState(PublishState.PENDING);
        return new AnswerPublishDTO(publishRepository.save(originalPublish));
    }

    public AnswerPublishDTO getUserEventsById(long userId, long eventId) {
        checkId(userId, eventId);
        Publish originalPublish = getPublish(eventId);
        checkUser(userId, originalPublish.getInitiator().getId());
        return new AnswerPublishDTO(originalPublish);
    }

    @Transactional
    public AnswerPublishDTO cancellationUserEvents(long userId, long eventId) {
        checkId(userId, eventId);
        Publish originalPublish = getPublish(eventId);
        checkUser(userId, originalPublish.getInitiator().getId());
        checkPendingStatus(originalPublish.getState());
        originalPublish.setState(PublishState.CANCELED);
        return new AnswerPublishDTO(publishRepository.save(originalPublish));
    }

    public List<AnswerPublishDTO> getPublishAdmin(List<Long> userId, List<PublishState> states,
                                                  List<Long> categories, LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd, int from, int size) {
        Optional<BooleanExpression> pred = createPredicate(userId, states, categories, rangeStart, rangeEnd);
        if(pred.isEmpty()) {
            return toDTO(publishRepository.findAll(checkPaginationParams(from, size)));
        } else {
            return toDTO(publishRepository.findAll(pred.get(), checkPaginationParams(from, size)));
        }
    }

    public AnswerPublishDTO updatePublishAdmin(long eventId, Publish publish) {
        checkId(eventId);
        Publish originalPublish = getPublish(eventId);
        if(publish.getAnnotation() != null) {
            originalPublish.setAnnotation(publish.getAnnotation());
        }
        if(publish.getCategory() != null) {
            originalPublish.setCategory(publish.getCategory());
        }
        if(publish.getDescription() != null) {
            originalPublish.setDescription(publish.getDescription());
        }
        if(publish.getEventDate() != null) {
            originalPublish.setEventDate(publish.getEventDate());
        }
        if(publish.getLocation() != null) {
            originalPublish.setLocation(getAndPostLocation(publish.getLocation()));
        }
        if(publish.getPaid() != null) {
            originalPublish.setPaid(publish.getPaid());
        }
        if(publish.getParticipantLimit() != null) {
            originalPublish.setParticipantLimit(publish.getParticipantLimit());
        }
        if(publish.getRequestModeration() != null) {
            originalPublish.setRequestModeration(publish.getRequestModeration());
        }
        if(publish.getTitle() != null) {
            originalPublish.setTitle(publish.getTitle());
        }
        return new AnswerPublishDTO(publishRepository.save(originalPublish));
    }

    @Transactional
    public AnswerPublishDTO approvePublishAdmin(long eventId) {
        checkId(eventId);
        Publish publish = getPublish(eventId);
        checkStatusApproveAndRefuse(publish.getState());
        publish.setState(PublishState.PUBLISHED);
        return new AnswerPublishDTO(publishRepository.save(publish));
    }

    @Transactional
    public AnswerPublishDTO refusePublishAdmin(long eventId) {
        checkId(eventId);
        Publish publish = getPublish(eventId);
        checkStatusApproveAndRefuse(publish.getState());
        publish.setState(PublishState.CANCELED);
        return new AnswerPublishDTO(publishRepository.save(publish));
    }

    public List<AnswerPublishDTO> getPublishPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, Boolean onlyAvailable, SortOptions sort,
                                   int from, int size) {
        BooleanExpression predicate = getPredicate(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        if(sort == null) {
            return toDTO(publishRepository.findAll(predicate, PageRequest.of(from, size)));
        } else {
            if(sort.equals(SortOptions.EVENT_DATE)) {
                return toDTO(publishRepository.findAll(predicate,
                        checkPaginationParams(from, size, Sort.by("eventDate"))));
            } else {
                return toDTO(publishRepository.findAll(predicate,
                        checkPaginationParams(from, size, Sort.by("views"))));
            }
        }

    }

    public AnswerPublishDTO getEventsById(long id) {
        checkId(id);
        Optional<Publish> publish = publishRepository.findById(id);
        if(publish.isEmpty()) {
            throw new NotFoundException("Некорректный запрос.", "Такого события нет.");
        } else {
            Publish publish2 = publish.get();
            publish2.setViews(publish2.getViews() + 1);
            publishRepository.save(publish2);
            return new AnswerPublishDTO(publish.get());
        }
    }

    private Location getAndPostLocation(Location location) {
        Optional<Location> dbLocation = locationRepository.findByLatAndLon(location.getLat(), location.getLon());
        if(dbLocation.isEmpty()) {
            return locationRepository.save(location);
        } else {
            return dbLocation.get();
        }
    }

    private User checkUser(long userid, long originalUserId) {
        User user = getUser(originalUserId);
        if(user.getId() != userid) {
            throw new ConflictException("Ошибка: нельзя редактировать/смотреть чужую публикацию.", "");
        } else {
            return user;
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

    private Publish getPublish(long id) {
        Optional<Publish> publishOptional = publishRepository.findById(id);
        if(publishOptional.isEmpty()) {
            throw new NotFoundException("Ошибка: нет события с Id=" + id + ".", "");
        } else {
            return publishOptional.get();
        }
    }

    private Publish essenceFilling(Publish publish, long userId) {
        publish.setConfirmedRequests(0);
        publish.setCreatedOn(LocalDateTime.now());
        publish.setState(PublishState.PENDING);
        publish.setInitiator(getUser(userId));
        publish.setLocation(locationRepository.save(getAndPostLocation(publish.getLocation())));
        return publish;
    }

    private Optional<BooleanExpression> createPredicate(List<Long> userId, List<PublishState> states, List<Long> categories,
                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        List<BooleanExpression> predicate = new ArrayList<>();
        QPublish publish = QPublish.publish;
        if (Objects.nonNull(userId) && !userId.isEmpty()) {
            predicate.add(publish.initiator.id.in(userId));
        }
        if (Objects.nonNull(states) && !states.isEmpty()) {
            predicate.add(publish.state.in(states));
        }
        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            predicate.add(publish.category.id.in(categories));
        }
        if (Objects.nonNull(rangeStart) && Objects.nonNull(rangeEnd)) {
            predicate.add(publish.eventDate.after(rangeStart).and(publish.eventDate.before(rangeEnd)));
        }
        return predicate.stream().reduce(BooleanExpression::and);
    }

    private BooleanExpression getPredicate(String text,
                                           List<Long> categories,
                                           Boolean paid,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Boolean onlyAvailable) {
        List<BooleanExpression> predicate = new ArrayList<>();
        QPublish publish = QPublish.publish;
        QRequest request = QRequest.request;
        predicate.add(publish.state.eq(PublishState.PUBLISHED));
        if (Objects.nonNull(text)) {
            predicate.add(publish.annotation.containsIgnoreCase(text).or(publish.description.containsIgnoreCase(text)));
        }
        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            predicate.add(publish.category.id.in(categories));
        }
        if (Objects.nonNull(paid)) {
            predicate.add(publish.paid.eq(paid));
        }
        if (Objects.nonNull(rangeStart) && Objects.nonNull(rangeEnd)) {
            predicate.add(publish.eventDate.after(rangeStart).and(publish.eventDate.before(rangeEnd)));
        } else {
            predicate.add(publish.eventDate.after(LocalDateTime.now()));
        }
        if (Objects.nonNull(onlyAvailable) && onlyAvailable) {
            predicate.add(publish.participantLimit.eq(0)
                    .or(publish.requestModeration.isFalse().and(publish.participantLimit.goe(request.count())))
                    .or(publish.requestModeration.isTrue().and(publish.participantLimit.goe(request.status
                            .eq(RequestStatus.CONFIRMED).count()))));
        }
        return predicate.stream().reduce(BooleanExpression::and).get();
    }

    private List<AnswerPublishDTO> toDTO(List<Publish> publishPage) {
        return publishPage
                .stream()
                .map(p -> new AnswerPublishDTO(p))
                .collect(Collectors.toList());
    }

    private List<AnswerPublishDTO> toDTO(Page<Publish> publishPage) {
        return publishPage
                .stream()
                .map(p -> new AnswerPublishDTO(p))
                .collect(Collectors.toList());
    }
}
