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
import ru.practicum.repository.PublicationRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.validation.PublicationValidationMaster;
import ru.practicum.validation.ValidationMaster;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PublicationService {

    private final UserRepository userRepository;
    private final PublicationRepository publicationRepository;
    private final LocationRepository locationRepository;

    public PublicationService(UserRepository userRepository,
                              PublicationRepository publicationRepository,
                              LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.publicationRepository = publicationRepository;
        this.locationRepository = locationRepository;
    }

    public List<AnswerPublicationDTO> getUserPublication(long userId, int from, int size) {
        return toDTO(publicationRepository.findByInitiatorOrderByCreatedOn(getUser(userId),
                        PublicationValidationMaster.checkPaginationParams(from, size))
                .stream()
                .collect(Collectors.toList()));
    }

    @Transactional
    public AnswerPublicationDTO addUserPublication(long userId, Publication publish) {
        PublicationValidationMaster.checkIds(userId);
        return new AnswerPublicationDTO(publicationRepository.save(fillingEntity(publish, userId)));
    }

    @Transactional
    public AnswerPublicationDTO updateUserPublication(long userId, Publication publish, long eventId) {
        PublicationValidationMaster.checkIds(userId, eventId);
        Publication originalPublish = getPublication(eventId);
        checkUser(userId, originalPublish.getInitiator().getId());
        PublicationValidationMaster.checkStatus(originalPublish.getState());
        if (publish.getAnnotation() != null) {
            originalPublish.setAnnotation(publish.getAnnotation());
        }
        if (publish.getCategory() != null) {
            originalPublish.setCategory(publish.getCategory());
        }
        if (publish.getDescription() != null) {
            originalPublish.setDescription(publish.getDescription());
        }
        if (publish.getEventDate() != null) {
            originalPublish.setEventDate(publish.getEventDate());
        }
        if (publish.getPaid() != null) {
            originalPublish.setPaid(publish.getPaid());
        }
        if (publish.getParticipantLimit() != null) {
            originalPublish.setParticipantLimit(publish.getParticipantLimit());
        }
        if (publish.getTitle() != null) {
            originalPublish.setTitle(publish.getTitle());
        }
        originalPublish.setState(PublicationState.PENDING);
        return new AnswerPublicationDTO(publicationRepository.save(originalPublish));
    }

    public AnswerPublicationDTO getUserPublicationById(long userId, long eventId) {
        PublicationValidationMaster.checkIds(userId, eventId);
        Publication originalPublish = getPublication(eventId);
        checkUser(userId, originalPublish.getInitiator().getId());
        return new AnswerPublicationDTO(originalPublish);
    }

    @Transactional
    public AnswerPublicationDTO cancellationUserPublication(long userId, long eventId) {
        PublicationValidationMaster.checkIds(userId, eventId);
        Publication originalPublish = getPublication(eventId);
        checkUser(userId, originalPublish.getInitiator().getId());
        PublicationValidationMaster.checkState(originalPublish.getState());
        originalPublish.setState(PublicationState.CANCELED);
        return new AnswerPublicationDTO(publicationRepository.save(originalPublish));
    }

    public List<AnswerPublicationDTO> getPublicationAdmin(List<Long> userId, List<PublicationState> states,
                                                      List<Long> categories, LocalDateTime rangeStart,
                                                      LocalDateTime rangeEnd, int from, int size) {
        Optional<BooleanExpression> pred = createPredicate(userId, states, categories, rangeStart, rangeEnd);
        if (pred.isEmpty()) {
            return toDTO(publicationRepository.findAll(PublicationValidationMaster.checkPaginationParams(from, size)));
        } else {
            return toDTO(publicationRepository.findAll(pred.get(), PublicationValidationMaster.checkPaginationParams(from, size)));
        }
    }

    public AnswerPublicationDTO updatePublicationAdmin(long eventId, Publication publish) {
        PublicationValidationMaster.checkIds(eventId);
        Publication originalPublish = getPublication(eventId);
        if (publish.getAnnotation() != null) {
            originalPublish.setAnnotation(publish.getAnnotation());
        }
        if (publish.getCategory() != null) {
            originalPublish.setCategory(publish.getCategory());
        }
        if (publish.getDescription() != null) {
            originalPublish.setDescription(publish.getDescription());
        }
        if (publish.getEventDate() != null) {
            originalPublish.setEventDate(publish.getEventDate());
        }
        if (publish.getLocation() != null) {
            originalPublish.setLocation(getAndPostLocation(publish.getLocation()));
        }
        if (publish.getPaid() != null) {
            originalPublish.setPaid(publish.getPaid());
        }
        if (publish.getParticipantLimit() != null) {
            originalPublish.setParticipantLimit(publish.getParticipantLimit());
        }
        if (publish.getRequestModeration() != null) {
            originalPublish.setRequestModeration(publish.getRequestModeration());
        }
        if (publish.getTitle() != null) {
            originalPublish.setTitle(publish.getTitle());
        }
        return new AnswerPublicationDTO(publicationRepository.save(originalPublish));
    }

    @Transactional
    public AnswerPublicationDTO approveOrRefusePublicationAdmin(long eventId, PublicationState state) {
        PublicationValidationMaster.checkIds(eventId);
        Publication publish = getPublication(eventId);
        PublicationValidationMaster.checkState(publish.getState());
        publish.setState(state);
        return new AnswerPublicationDTO(publicationRepository.save(publish));
    }



    public List<AnswerPublicationDTO> getPublicationPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd, Boolean onlyAvailable, SortOptions sort,
                                                       int from, int size) {
        BooleanExpression predicate = getPredicate(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        if (sort == null) {
            return toDTO(publicationRepository.findAll(predicate, PageRequest.of(from, size)));
        } else {
            if (sort.equals(SortOptions.EVENT_DATE)) {
                return toDTO(publicationRepository.findAll(predicate,
                        PublicationValidationMaster.checkPaginationParams(from, size, Sort.by("eventDate"))));
            } else {
                return toDTO(publicationRepository.findAll(predicate,
                        PublicationValidationMaster.checkPaginationParams(from, size, Sort.by("views"))));
            }
        }

    }

    public AnswerPublicationDTO getPublicationByIdPublic(long id) {
        ValidationMaster.checkIds(id);
        Optional<Publication> publish = publicationRepository.findById(id);
        if (publish.isEmpty()) {
            throw new NotFoundException("Некорректный запрос.", "Такого события нет.");
        } else {
            Publication publish2 = publish.get();
            publish2.setViews(publish2.getViews() + 1);
            publicationRepository.save(publish2);
            return new AnswerPublicationDTO(publish.get());
        }
    }

    private Location getAndPostLocation(Location location) {
        return locationRepository
                .findByLatAndLon(location.getLat(), location.getLon())
                .orElseGet(() -> locationRepository.save(location));
    }

    private User checkUser(long userid, long originalUserId) {
        if (originalUserId != userid) {
            throw new ConflictException("Ошибка: нельзя редактировать/смотреть чужую публикацию.", "");
        } else {
            return getUser(originalUserId);
        }
    }

    private User getUser(long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Ошибка: нет пользователя с Id=" + userId + ".", ""));
    }

    private Publication getPublication(long id) {
        return publicationRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Ошибка: нет события с Id=" + id + ".", ""));
    }

    private Publication fillingEntity(Publication publish, long userId) {
        publish.setConfirmedRequests(0);
        publish.setCreatedOn(LocalDateTime.now());
        publish.setState(PublicationState.PENDING);
        publish.setInitiator(getUser(userId));
        publish.setLocation(locationRepository.save(getAndPostLocation(publish.getLocation())));
        return publish;
    }

    private Optional<BooleanExpression> createPredicate(List<Long> userId, List<PublicationState> states, List<Long> categories,
                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        List<BooleanExpression> predicate = new ArrayList<>();
        QPublication publication = QPublication.publication;
        if (Objects.nonNull(userId) && !userId.isEmpty()) {
            predicate.add(publication.initiator.id.in(userId));
        }
        if (Objects.nonNull(states) && !states.isEmpty()) {
            predicate.add(publication.state.in(states));
        }
        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            predicate.add(publication.category.id.in(categories));
        }
        if (Objects.nonNull(rangeStart) && Objects.nonNull(rangeEnd)) {
            predicate.add(publication.eventDate.after(rangeStart).and(publication.eventDate.before(rangeEnd)));
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
        QPublication publication = QPublication.publication;
        QRequest request = QRequest.request;
        predicate.add(publication.state.eq(PublicationState.PUBLISHED));
        if (Objects.nonNull(text)) {
            predicate.add(publication.annotation.containsIgnoreCase(text).or(publication.description.containsIgnoreCase(text)));
        }
        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            predicate.add(publication.category.id.in(categories));
        }
        if (Objects.nonNull(paid)) {
            predicate.add(publication.paid.eq(paid));
        }
        if (Objects.nonNull(rangeStart) && Objects.nonNull(rangeEnd)) {
            predicate.add(publication.eventDate.after(rangeStart).and(publication.eventDate.before(rangeEnd)));
        } else {
            predicate.add(publication.eventDate.after(LocalDateTime.now()));
        }
        if (Objects.nonNull(onlyAvailable) && onlyAvailable) {
            predicate.add(publication.participantLimit.eq(0)
                    .or(publication.requestModeration.isFalse().and(publication.participantLimit.goe(request.count())))
                    .or(publication.requestModeration.isTrue().and(publication.participantLimit.goe(request.status
                            .eq(RequestStatus.CONFIRMED).count()))));
        }
        return predicate.stream().reduce(BooleanExpression::and).get();
    }

    private List<AnswerPublicationDTO> toDTO(List<Publication> publishPage) {
        return publishPage
                .stream()
                .map(AnswerPublicationDTO::new)
                .collect(Collectors.toList());
    }

    private List<AnswerPublicationDTO> toDTO(Page<Publication> publishPage) {
        return publishPage
                .stream()
                .map(AnswerPublicationDTO::new)
                .collect(Collectors.toList());
    }
}
