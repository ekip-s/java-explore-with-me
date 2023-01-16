package ru.practicum.validation;

import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;

@Service
public class CommentsValidationMaster extends ValidationMaster {

    public static boolean checkUser(long userId, long userId2) {
        if (userId == userId2) {
            throw new ConflictException("Так нельзя.", "Нельзя комментировать свое событие.");
        } else {
            return true;
        }
    }

    public static boolean checkCommentOwner(long userId, long userId2) {
        if (userId == userId2) {
            return true;
        } else {
            throw new ConflictException("Так нельзя.", "Нельзя изменять/удалять чужой комментарий.");
        }
    }
}
