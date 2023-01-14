package ru.practicum.validation;

import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.model.publish.PublicationState;

@Service
public class RequestValidationMaster extends ValidationMaster {

    public static boolean checkRequest(long eventId, long eventId2) {
        if (eventId == eventId2) {
            return true;
        } else {
            throw new ConflictException("Ошибка: запрос не соответствует событию.", "");
        }
    }

    public static boolean checkRequesterApproveApplication(long userId, long requesterId) {
        if (userId == requesterId) {
            return true;
        } else {
            throw new ConflictException("Ошибка: нельзя подтверждать/отменять запросы на чужие события.", "");
        }
    }

    public static boolean checkRequesterCancel(long userId, long requesterId) {
        if (userId == requesterId) {
            return true;
        } else {
            throw new ConflictException("Ошибка: нельзя отменить чужой запрос.", "");
        }
    }

    public static boolean checkStatusAndLimit(PublicationState status, int limit) {
        if (limit > 0 && status.equals(PublicationState.PUBLISHED)) {
            return true;
        } else {
            if (limit > 0) {
                throw new ConflictException("Ошибка: событие не опубликовано, оставьте заявку на другое событие.", "");
            } else {
                throw new ConflictException("Ошибка: нет мест на это событие.", "");
            }
        }
    }

    public static boolean checkRequester(long userId, long requesterId) {
        if (userId == requesterId) {
            throw new ConflictException("Ошибка: нельзя отправить запрос на свое событие. Приходите так.", "");
        } else {
            return true;
        }
    }

    public static boolean checkRequesterParticipation(long initiatorId, long userId) {
        if (initiatorId != userId) {
            throw new ForbiddenException("Некорректная операция", "Нельзя смотреть заявки на чужое событие.");
        } else {
            return true;
        }
    }
}
