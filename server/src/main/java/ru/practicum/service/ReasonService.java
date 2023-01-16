package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.reason.Reason;
import ru.practicum.repository.ReasonRepository;
import ru.practicum.validation.ValidationMaster;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReasonService {

    private final ReasonRepository repository;

    @Autowired
    public ReasonService(ReasonRepository repository) {
        this.repository = repository;
    }

    public Reason getReasonById(long reasonId) {
        return repository
                .findById(reasonId)
                .orElseThrow(() -> new NotFoundException("Некорректный запрос.",
                        "Причины отказа с id=" + reasonId + " нет."));
    }

    public List<Reason> getReason(int from, int size) {
        return repository
                .findAll(ValidationMaster.checkPaginationParams(from, size))
                .stream()
                .collect(Collectors.toList());
    }

    @Transactional
    public Reason addReason(Reason reason) {
        return repository.save(reason);
    }

    @Transactional
    public Reason patchReason(long reasonId, Reason reason) {
        ValidationMaster.checkIds(reasonId);
        Reason reasonDB = getReasonById(reasonId);
        reasonDB.setName(reason.getName());
        return repository.save(reasonDB);
    }

    @Transactional
    public void deleteReason(long reasonId) {
        ValidationMaster.checkIds(reasonId);
        getReasonById(reasonId);
        repository.deleteById(reasonId);
    }
}
