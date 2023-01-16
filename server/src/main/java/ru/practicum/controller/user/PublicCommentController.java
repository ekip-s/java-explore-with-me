package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.Comment;
import ru.practicum.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/comments")
public class PublicCommentController {

    private final CommentService commentService;

    @GetMapping
    public List<Comment> getCommentsPublic(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getCommentsPublic(from, size);
    }

    @GetMapping("search/{search}")
    public List<Comment> getCommentsSearchPublic(@RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size,
                                                 @PathVariable String search) {
        return commentService.getCommentsSearchPublic(from, size, search);
    }

    @GetMapping("/{commentId}")
    public Comment getCommentByIdPublic(@PathVariable Long commentId) {
        return commentService.getComment(commentId);
    }
}
