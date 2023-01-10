package ru.practicum.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.*;
import ru.practicum.repository.LocationRepository;
import ru.practicum.repository.PublishRepository;
import ru.practicum.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PublishService {

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

    public Page<Publish> getUserEvents(long userId,int from, int size) {
        return publishRepository.findByInitiatorOrderByCreatedOn(getUser(userId), getPage(from, size));
    }

    @Transactional
    public Publish addUserEvents(long userId, Publish publish) {
        return publishRepository.save(essenceFilling(publish, userId));
    }

    @Transactional
    public Publish updateUserEvents(long userId, Publish publish, long eventId) {
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
        originalPublish.setState(PublishState.PENDING_PUBLICATION);
        return publishRepository.save(originalPublish);
    }

    public Publish getUserEventsById(long userId, long eventId) {
        Publish originalPublish = getPublish(eventId);
        checkUser(userId, originalPublish.getInitiator().getId());
        return originalPublish;
    }

    @Transactional
    public Publish cancellationUserEvents(long userId, long eventId) {
        Publish originalPublish = getPublish(eventId);
        checkUser(userId, originalPublish.getInitiator().getId());
        checkPendingStatus(originalPublish.getState());
        originalPublish.setState(PublishState.CANCELLATION);
        return publishRepository.save(originalPublish);
    }

    public Page<Publish> getPublishAdmin(List<Long> userId, List<PublishState> states,
                                                  List<Long> categories, LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd, int from, int size) {
        Optional<BooleanExpression> pred = createPredicate(userId, states, categories, rangeStart, rangeEnd);
        if(pred.isEmpty()) {
            return publishRepository.findAll(getPage(from, size));
        } else {
            return publishRepository.findAll(pred.get(), getPage(from, size));
        }
    }

    public Publish updatePublishAdmin(long eventId, Publish publish) {
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
        return publishRepository.save(originalPublish);
    }

    @Transactional
    public Publish approvePublishAdmin(long eventId) {
        Publish publish = getPublish(eventId);
        checkStatusApproveAndRefuse(publish.getState());
        publish.setState(PublishState.PUBLISHED);
        return publishRepository.save(publish);
    }

    @Transactional
    public Publish refusePublishAdmin(long eventId) {
        Publish publish = getPublish(eventId);
        checkStatusApproveAndRefuse(publish.getState());
        publish.setState(PublishState.CANCELLATION);
        return publishRepository.save(publish);
    }

    public Page<Publish> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, Boolean onlyAvailable, SortOptions sort,
                                   int from, int size) {
        BooleanExpression predicate = getPredicate(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        if(sort == null) {
            return publishRepository.findAll(predicate, PageRequest.of(from, size));
        } else {
            if(sort.equals(SortOptions.EVENT_DATE)) {
                return publishRepository.findAll(predicate, PageRequest.of(from, size, Sort.by("eventDate")));
            } else {
                return publishRepository.findAll(predicate,PageRequest.of(from, size, Sort.by("views")));
            }
        }

    }

    public Publish getEventsById(long id) {
        Optional<Publish> publish = publishRepository.findById(id);
        if(publish.isEmpty()) {
            return new Publish();
        } else {
            Publish publish2 = publish.get();
            publish2.setViews(publish2.getViews() + 1);
            publishRepository.save(publish2);
            return publish.get();
        }
    }

    private boolean checkStatusApproveAndRefuse(PublishState state) {
        if(state.equals(PublishState.PENDING_PUBLICATION)) {
            return true;
        } else {
            throw new ConflictException("Ошибка: неподходящий статус публикации." +
                    "Текущий статус: " + state + ".");
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

    private boolean checkPendingStatus(PublishState state) {
        if(state.equals(PublishState.PENDING_PUBLICATION)) {
            return true;
        } else {
            throw new ConflictException("Ошибка: отменить можно событие только в статусе ожидания публицации." +
                    "Текущий статус: " + state + ".");
        }
    }

    private Pageable getPage(int from, int size) {
        return PageRequest.of(from, size);
    }

    private User checkUser(long userid, long originalUserId) {
        User user = getUser(originalUserId);
        if(user.getId() != userid) {
            throw new ConflictException("Ошибка: нельзя редактировать/смотреть чужую публикацию.");
        } else {
            return user;
        }
    }

    private boolean checkStatus(PublishState state) {
        if(state.equals(PublishState.PUBLISHED)) {
            throw new ConflictException("Ошибка: нельзя изменить событие, оно уже опубликовано.");
        } else {
            return true;
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

    private Publish getPublish(long id) {
        Optional<Publish> publishOptional = publishRepository.findById(id);
        if(publishOptional.isEmpty()) {
            throw new NotFoundException("Ошибка: нет события с Id=" + id + ".");
        } else {
            return publishOptional.get();
        }
    }

    private Publish essenceFilling(Publish publish, long userId) {
        publish.setConfirmedRequests(0);
        publish.setCreatedOn(LocalDateTime.now());
        publish.setState(PublishState.PENDING_PUBLICATION);
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
}
