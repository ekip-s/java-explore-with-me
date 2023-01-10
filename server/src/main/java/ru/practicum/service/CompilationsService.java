package ru.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Compilations;
import ru.practicum.model.Publish;
import ru.practicum.model.PublishState;
import ru.practicum.repository.CompilationsRepository;
import ru.practicum.repository.PublishRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompilationsService {

    private final CompilationsRepository compilationsRepository;
    private final PublishRepository publishRepository;


    public CompilationsService(CompilationsRepository compilationsRepository, PublishRepository publishRepository) {
        this.compilationsRepository = compilationsRepository;
        this.publishRepository = publishRepository;
    }

    public Compilations createCompilation(Compilations compilations) {
        compilations.setEvents(compilations.getEvents());
        return compilationsRepository.save(compilations);
    }

    public void deleteCompilations(long compId) {
        compilationsRepository.deleteById(compId);
    }

    public Compilations deleteEvent(long compId, long eventId) {
        Compilations compilations = getCompilations(compId);
        List<Publish> publishes = filterPublish(compilations.getEvents(), eventId);
        if(publishes.isEmpty()) {
            deleteCompilations(eventId);
            return null;
        } else {
            compilations.setEvents(publishes);
            return compilationsRepository.save(compilations);
        }
    }

    public Compilations addEvent(long compId, long eventId) {
        Compilations compilations = getCompilations(compId);
        List<Publish> publishes = compilations.getEvents();
        publishes.add(getPublishOptional(eventId));
        compilations.setEvents(publishes);
        return compilationsRepository.save(compilations);
    }

    public void unpinAndFix(long compId, boolean status) {
        Compilations compilations = getCompilations(compId);
        compilations.setPinned(status);
        compilationsRepository.save(compilations);
    }

    public Page<Compilations> getCompilationsPublic(Boolean pinned, int from, int size) {
        if(pinned == null) {
            return compilationsRepository.findAll(PageRequest.of(from, size));
        } else {
            return compilationsRepository.findByPinned(pinned, PageRequest.of(from, size));
        }
    }

    public Compilations getCompilationsByIdPublic(long compId) {
        Optional<Compilations> compilations = compilationsRepository.findById(compId);
        if(compilations.isEmpty()) {
            return new Compilations();
        } else {
            return compilations.get();
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
            throw new NotFoundException("Нет подборки с Id: " + id + ".");
        } else {
            return optionalCompilations.get();
        }
    }

    private List<Publish> checkPublish(List<Publish> publishList) {
        List<Publish> newPublishList = new ArrayList<>();
        for(Publish p : publishList) {
            Publish pub = getPublish(p.getId());
            if(pub != null) {
                newPublishList.add(pub);
            }
        }
        if(newPublishList.isEmpty()) {
            throw new ConflictException("Ошибка: нет опубликовонных событий для подборки.");
        } else {
            return newPublishList;
        }
    }

    private Publish getPublishOptional(long id) {
        Optional<Publish> publishOptional = publishRepository.findById(id);
        if(publishOptional.isEmpty()) {
            throw new NotFoundException("Ошибка: нет публикации с id=" + id + ".");
        } else if(!publishOptional.get().getState().equals(PublishState.PUBLISHED)) {
            throw new ConflictException("Ошибка: нельзя добавить не опубликованное событие.");
        } else {
            return publishOptional.get();
        }
    }

    private Publish getPublish(long id) {
        Optional<Publish> publishOptional = publishRepository.findById(id);
        if(publishOptional.isEmpty()) {
            return null;
        } else {
            if(publishOptional.get().getState().equals(PublishState.PUBLISHED)) {
                return publishOptional.get();
            } else {
                return null;
            }
        }
    }
}
