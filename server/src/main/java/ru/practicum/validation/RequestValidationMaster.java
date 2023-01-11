package ru.practicum.validation;

import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.model.publish.PublishState;

public class RequestValidationMaster extends ValidationMaster {

    protected boolean checkRequest(long eventId, long eventId2) {
        if (eventId == eventId2) {
            return true;
        } else {
            throw new ConflictException("Ошибка: запрос не соответствует событию.", "");
        }
    }

    protected boolean checkRequesterApprovalApplication(long userId, long requesterId) {
        if (userId == requesterId) {
            return true;
        } else {
            throw new ConflictException("Ошибка: нельзя подтверждать/отменять запросы на чужие события.", "");
        }
    }

    protected boolean checkRequesterCancel(long userId, long requesterId) {
        if (userId == requesterId) {
            return true;
        } else {
            throw new ConflictException("Ошибка: нельзя отменить чужой запрос.", "");
        }
    }

    protected boolean checkStatusAndLimit(PublishState status, int limit) {
        if (limit > 0 && status.equals(PublishState.PUBLISHED)) {
            return true;
        } else {
            if (limit > 0) {
                throw new ConflictException("Ошибка: событие не опубликовано, оставьте заявку на другое событие.", "");
            } else {
                throw new ConflictException("Ошибка: нет мест на это событие.", "");
            }
        }
    }

    protected boolean checkRequester(long userId, long requesterId) {
        if (userId == requesterId) {
            throw new ConflictException("Ошибка: нельзя отправить запрос на свое событие. Приходите так.", "");
        } else {
            return true;
        }
    }

    protected boolean checkRequesterParticipation(long initiatorId, long userId) {
        if (initiatorId != userId) {
            throw new ForbiddenException("Некорректная операция", "Нельзя смотреть заявки на чужое событие.");
        } else {
            return true;
        }
    }
}
