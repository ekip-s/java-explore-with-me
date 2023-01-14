package ru.practicum.validation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.model.publish.PublicationState;

@Service
public class ValidationMaster {

    public static void checkIds(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("Ошибка валидации: id не может быть меньше 1.");
        }
    }

    public static void checkIds(long id, long id2) {
        checkIds(id);
        checkIds(id2);
    }

    public static void checkIds(long id, long id2, long id3) {
        checkIds(id, id2);
        checkIds(id3);
    }

    public static Pageable checkPaginationParams(int from, int size) {
        return checkPaginationParams(from, size, null);
    }

    public static Pageable checkPaginationParams(int from, int size, Sort sort) {
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

    public static boolean checkStatus(PublicationState state) {
        if (state.equals(PublicationState.PUBLISHED)) {
            throw new ConflictException("Ошибка: нельзя изменить событие, оно уже опубликовано.", "");
        } else {
            return true;
        }
    }
}
