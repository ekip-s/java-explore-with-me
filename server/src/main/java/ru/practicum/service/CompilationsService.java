package ru.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilations.AnswerCompilationsDTO;
import ru.practicum.model.compilations.Compilations;
import ru.practicum.model.compilations.CompilationsDTO;
import ru.practicum.model.publish.Publish;
import ru.practicum.model.publish.PublishState;
import ru.practicum.repository.CompilationsRepository;
import ru.practicum.repository.PublishRepository;
import ru.practicum.validation.ValidationMaster;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompilationsService extends ValidationMaster {

    private final CompilationsRepository compilationsRepository;
    private final PublishRepository publishRepository;


    public CompilationsService(CompilationsRepository compilationsRepository, PublishRepository publishRepository) {
        this.compilationsRepository = compilationsRepository;
        this.publishRepository = publishRepository;
    }

    public AnswerCompilationsDTO createCompilation(CompilationsDTO compilations) {
        List<Publish> publishes = publishRepository.findAllById(compilations.getEvents());
        Compilations compilations1 = new Compilations(compilations, publishes);
        return new AnswerCompilationsDTO(compilationsRepository.save(compilations1));
    }

    public void deleteCompilations(long compId) {
        checkId(compId);
        compilationsRepository.deleteById(compId);
    }

    public void deleteEvent(long compId, long eventId) {
        checkId(compId,eventId);
        Compilations compilations = getCompilations(compId);
        Publish publish = getPublishOptional(eventId);
        compilations.getEvents().remove(publish);
        compilationsRepository.save(compilations);
    }

    public void addEvent(long compId, long eventId) {
        checkId(compId,eventId);
        Compilations compilations = getCompilations(compId);
        List<Publish> publishes = compilations.getEvents();
        publishes.add(getPublishOptional(eventId));
        compilations.setEvents(publishes);
        compilationsRepository.save(compilations);
    }

    public void unpinAndFix(long compId, boolean status) {
        checkId(compId);
        Compilations compilations = getCompilations(compId);
        compilations.setPinned(status);
        compilationsRepository.save(compilations);
    }

    public List<AnswerCompilationsDTO> getCompilationsPublic(Boolean pinned, int from, int size) {
        if(pinned == null) {
            return toDTO(compilationsRepository.findAll(checkPaginationParams(from, size)));
        } else {
            return toDTO(compilationsRepository.findByPinned(pinned, checkPaginationParams(from, size)));
        }
    }

    public AnswerCompilationsDTO getCompilationsByIdPublic(long compId) {
        checkId(compId);
        Optional<Compilations> compilations = compilationsRepository.findById(compId);
        if(compilations.isEmpty()) {
            throw new NotFoundException("Некорректный запрос.", "Такой подборки нет.");
        } else {
            return new AnswerCompilationsDTO(compilations.get());
        }
    }

    private List<Publish> filterPublish(List<Publish> publishes, long eventId) {
        return publishes.stream()
                .filter(p -> p.getId() != eventId)
                .collect(Collectors.toList());
    }

    private Compilations getCompilations(long id) {
        Optional<Compilations> optionalCompilations = compilationsRepository.findById(id);
        if(optionalCompilations.isEmpty()) {
            throw new NotFoundException("Нет подборки с Id: " + id + ".", "");
        } else {
            return optionalCompilations.get();
        }
    }

    private Publish getPublishOptional(long id) {
        Optional<Publish> publishOptional = publishRepository.findById(id);
        if(publishOptional.isEmpty()) {
            throw new NotFoundException("Ошибка: нет публикации с id=" + id + ".", "");
        } else if(!publishOptional.get().getState().equals(PublishState.PUBLISHED)) {
            throw new ConflictException("Ошибка: нельзя добавить не опубликованное событие.", "");
        } else {
            return publishOptional.get();
        }
    }

    private List<AnswerCompilationsDTO> toDTO(Page<Compilations> compilations) {
        return compilations
                .stream()
                .map(c -> new AnswerCompilationsDTO(c))
                .collect(Collectors.toList());
    }
}
