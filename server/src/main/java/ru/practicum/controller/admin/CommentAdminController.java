package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.CommentDTO;
import ru.practicum.service.CommentService;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/comments/{commentId}")
public class CommentAdminController {

    private final CommentService commentService;

    @PatchMapping
    public Comment patchCommentAdmin(@PathVariable Long commentId, @RequestBody CommentDTO comment) {
        log.info("Получен PATCH запрос к эндпоинту: '/admin/comments/{}'. Строка параметров запроса: {}", commentId,
                comment.toString());
        return commentService.patchCommentAdmin(commentId, comment.getDescription());
    }

    @PatchMapping("/confirm")
    public Comment confirmCommentAdmin(@PathVariable Long commentId) {
        log.info("Получен PATCH запрос к эндпоинту: '/admin/comments/{}/confirm'.",
                commentId);
        return commentService.confirmCommentAdmin(commentId);
    }

    @PatchMapping("/reject/reason/{reasonId}")
    public Comment rejectCommentAdmin(@PathVariable Long commentId, @PathVariable Long reasonId) {
        log.info("Получен PATCH запрос к эндпоинту: '/admin/comments/{}/reject/reason/{}'.",
                commentId, reasonId);
        return commentService.rejectCommentAdmin(commentId, reasonId);
    }

    @DeleteMapping
    public void deleteCommentAdmin(@PathVariable Long commentId) {
        log.info("Получен DELETE запрос к эндпоинту: '/admin/comments/{}'.",
                commentId);
        commentService.deleteCommentAdmin(commentId);
    }
}
