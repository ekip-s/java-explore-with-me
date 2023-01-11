package ru.practicum.validation;

import ru.practicum.exception.ConflictException;
import ru.practicum.model.publish.PublishState;

public class PublishValidationMaster extends ValidationMaster {

    protected boolean checkStatusApproveAndRefuse(PublishState state) {
        if(state.equals(PublishState.PENDING)) {
            return true;
        } else {
            throw new ConflictException("Ошибка: неподходящий статус публикации.", "Текущий статус: " + state + ".");
        }
    }

    protected boolean checkPendingStatus(PublishState state) {
        if(state.equals(PublishState.PENDING)) {
            return true;
        } else {
            throw new ConflictException("Ошибка: отменить можно событие только в статусе ожидания публицации.",
                    "Текущий статус: " + state + ".");
        }
    }
}
