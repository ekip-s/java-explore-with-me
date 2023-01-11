package ru.practicum.validation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.exception.ConflictException;
import ru.practicum.model.publish.PublishState;

public class ValidationMaster {

    protected void checkId(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("Ошибка валидации: id не может быть меньше 1.");
        }
    }

    protected void checkId(long id, long id2) {
        checkId(id);
        checkId(id2);
    }

    protected void checkId(long id, long id2, long id3) {
        checkId(id, id2);
        checkId(id3);
    }

    protected Pageable checkPaginationParams(int from, int size) {
        return checkPaginationParams(from, size, null);
    }

    protected Pageable checkPaginationParams(int from, int size, Sort sort) {
        if (from < 0) {
            throw new IllegalArgumentException("Ошибка валидации: параметр from не может быть отрицательным.");
        } else if (size < 1) {
            throw new IllegalArgumentException("Ошибка валидации: параметр size должен быть больше нуля.");
        }

        if (sort == null) {
            return PageRequest.of(from / size, size);
        } else {
            return PageRequest.of(from / size, size, sort);
        }
    }

    protected boolean checkStatus(PublishState state) {
        if(state.equals(PublishState.PUBLISHED)) {
            throw new ConflictException("Ошибка: нельзя изменить событие, оно уже опубликовано.", "");
        } else {
            return true;
        }
    }
}
