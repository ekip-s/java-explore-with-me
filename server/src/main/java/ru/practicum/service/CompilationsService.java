package ru.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilations.AnswerCompilationsDTO;
import ru.practicum.model.compilations.Compilations;
import ru.practicum.model.compilations.CompilationsDTO;
import ru.practicum.model.publish.Publication;
import ru.practicum.model.publish.PublicationState;
import ru.practicum.repository.CompilationsRepository;
import ru.practicum.repository.PublicationRepository;
import ru.practicum.validation.ValidationMaster;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompilationsService {

    private final CompilationsRepository compilationsRepository;
    private final PublicationRepository publishRepository;


    public CompilationsService(CompilationsRepository compilationsRepository, PublicationRepository publishRepository) {
        this.compilationsRepository = compilationsRepository;
        this.publishRepository = publishRepository;
    }

    public AnswerCompilationsDTO createCompilation(CompilationsDTO compilations) {
        List<Publication> publishes = publishRepository.findAllById(compilations.getEvents());
        return new AnswerCompilationsDTO(compilationsRepository.save(new Compilations(compilations, publishes)));
    }

    public void deleteCompilations(long compId) {
        ValidationMaster.checkIds(compId);
        compilationsRepository.deleteById(compId);
    }

    public void deleteEvent(long compId, long eventId) {
        ValidationMaster.checkIds(compId,eventId);
        Compilations compilations = getCompilations(compId);
        Publication publish = getPublishOptional(eventId);
        compilations.getEvents().remove(publish);
        compilationsRepository.save(compilations);
    }

    public void addEvent(long compId, long eventId) {
        ValidationMaster.checkIds(compId,eventId);
        Compilations compilations = getCompilations(compId);
        List<Publication> publishes = compilations.getEvents();
        publishes.add(getPublishOptional(eventId));
        compilations.setEvents(publishes);
        compilationsRepository.save(compilations);
    }

    public void unpinAndFix(long compId, boolean status) {
        ValidationMaster.checkIds(compId);
        Compilations compilations = getCompilations(compId);
        compilations.setPinned(status);
        compilationsRepository.save(compilations);
    }

    public List<AnswerCompilationsDTO> getCompilationsPublic(Boolean pinned, int from, int size) {
        if (pinned == null) {
            return toDTO(compilationsRepository.findAll(ValidationMaster.checkPaginationParams(from, size)));
        } else {
            return toDTO(compilationsRepository.findByPinned(pinned,
                    ValidationMaster.checkPaginationParams(from, size)));
        }
    }

    public AnswerCompilationsDTO getCompilationsByIdPublic(long compId) {
        ValidationMaster.checkIds(compId);
        return new AnswerCompilationsDTO(compilationsRepository
                .findById(compId)
                .orElseThrow(() -> new NotFoundException("Некорректный запрос.", "Такой подборки нет.")));
    }

    private Compilations getCompilations(long id) {
        return compilationsRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Нет подборки с Id: " + id + ".", ""));
    }

    private Publication getPublishOptional(long id) {
        Optional<Publication> publishOptional = publishRepository.findById(id);
        if (publishOptional.isEmpty()) {
            throw new NotFoundException("Ошибка: нет публикации с id=" + id + ".", "");
        } else if (!publishOptional.get().getState().equals(PublicationState.PUBLISHED)) {
            throw new ConflictException("Ошибка: нельзя добавить не опубликованное событие.", "");
        } else {
            return publishOptional.get();
        }
    }

    private List<AnswerCompilationsDTO> toDTO(Page<Compilations> compilations) {
        return compilations
                .stream()
                .map(AnswerCompilationsDTO::new)
                .collect(Collectors.toList());
    }
}
