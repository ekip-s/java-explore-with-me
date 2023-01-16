package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.CommentDTO;
import ru.practicum.service.CommentService;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {

    private final CommentService service;

    @PostMapping
    public Comment addNewComment(@PathVariable Long userId, @Valid @RequestBody CommentDTO comment) {
        log.info("Получен POST запрос к эндпоинту: '/users/{}/comments'. Строка параметров запроса: {}", userId,
                comment.toString());
        return service.addNewCommentPrivate(userId, new Comment(comment));
    }

    @PatchMapping("/{commentId}")
    public Comment patchComment(@PathVariable Long userId,
                                @PathVariable Long commentId,
                                @RequestBody CommentDTO comment) {
        log.info("Получен PATCH запрос к эндпоинту: '/users/{}/comments/{}'. Строка параметров запроса: {}", userId,
                commentId, comment.toString());
        return service.patchCommentPrivate(userId, commentId, comment.getDescription());
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Получен DELETE запрос к эндпоинту: '/users/{}/comments/{}'.", userId, commentId);
        service.deleteCommentPrivate(userId, commentId);
    }
}
