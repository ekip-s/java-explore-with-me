package ru.practicum.validation;

import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.model.publish.PublicationState;

@Service
public class PublicationValidationMaster extends ValidationMaster {

    public static boolean checkState(PublicationState state) {
        if (state.equals(PublicationState.PENDING)) {
            return true;
        } else {
            throw new ConflictException("Ошибка: неподходящий статус публикации.", "Текущий статус: " + state + ".");
        }
    }
}
