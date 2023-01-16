package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.CommentsStatus;
import ru.practicum.model.publish.Publication;
import ru.practicum.model.reason.Reason;
import ru.practicum.model.user.User;
import ru.practicum.repository.CommentsRepository;
import ru.practicum.repository.PublicationRepository;
import ru.practicum.repository.ReasonRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.validation.CommentsValidationMaster;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final PublicationRepository publicationRepository;
    private final ReasonRepository reasonRepository;
    private final String standardReason = "Без объяснения причин.";
    private final Sort sort = Sort.by("created");

    @Autowired
    public CommentService(CommentsRepository commentsRepository,
                          UserRepository userRepository,
                          PublicationRepository publicationRepository,
                          ReasonRepository reasonRepository) {
        this.commentsRepository = commentsRepository;
        this.userRepository = userRepository;
        this.publicationRepository = publicationRepository;
        this.reasonRepository = reasonRepository;
    }

    @Transactional
    public Comment addNewCommentPrivate(long userId, Comment comment) {
        CommentsValidationMaster.checkIds(userId);
        Publication publication = getPublication(comment.getPublication().getId());
        CommentsValidationMaster.checkUser(userId, publication.getInitiator().getId());
        comment.setPublication(publication);
        comment.setAuthor(getUser(userId));
        return commentsRepository.save(comment);
    }

    @Transactional
    public Comment patchCommentPrivate(long userId, long commentId, String description) {
        CommentsValidationMaster.checkIds(userId);
        getUser(userId);
        Comment comment = getComment(commentId);
        CommentsValidationMaster.checkCommentOwner(userId, comment.getAuthor().getId());
        comment.setDescription(description);
        return commentsRepository.save(comment);
    }

    @Transactional
    public void deleteCommentPrivate(long userId, long commentId) {
        CommentsValidationMaster.checkIds(userId, commentId);
        getUser(userId);
        Comment comment = getComment(commentId);
        CommentsValidationMaster.checkCommentOwner(userId, comment.getAuthor().getId());
        commentsRepository.deleteById(commentId);
    }

    @Transactional
    public Comment patchCommentAdmin(long commentId, String description) {
        CommentsValidationMaster.checkIds(commentId);
        Comment comment = getComment(commentId);
        comment.setDescription(description);
        comment.setStatus(CommentsStatus.APPROVED);
        return commentsRepository.save(comment);
    }

    @Transactional
    public Comment confirmCommentAdmin(long commentId) {
        CommentsValidationMaster.checkIds(commentId);
        Comment comment = getComment(commentId);
        comment.setStatus(CommentsStatus.APPROVED);
        return commentsRepository.save(comment);
    }

    @Transactional
    public Comment rejectCommentAdmin(long commentId, long reasonId) {
        CommentsValidationMaster.checkIds(commentId, reasonId);
        Reason reason = getReason(reasonId);
        Comment comment = getComment(commentId);
        comment.setStatus(CommentsStatus.CANCELED);
        comment.setReason(reason);
        return commentsRepository.save(comment);
    }

    @Transactional
    public void deleteCommentAdmin(long commentId) {
        CommentsValidationMaster.checkIds(commentId);
        getComment(commentId);
        commentsRepository.deleteById(commentId);
    }

    public List<Comment> getCommentsPublic(int from, int size) {
        return commentsRepository
                .findAll(CommentsValidationMaster.checkPaginationParams(from, size, sort))
                .stream()
                .collect(Collectors.toList());
    }

    public List<Comment> getCommentsSearchPublic(int from, int size, String search) {
        return commentsRepository
                .getCommentsSearchPublic(search.toLowerCase(),
                        CommentsValidationMaster.checkPaginationParams(from, size, sort))
                .stream()
                .collect(Collectors.toList());
    }

    public Comment getComment(long commentId) {
        return commentsRepository
                .findById(commentId)
                .orElseThrow(() -> new NotFoundException("Нет объекта.",
                        "Ошибка: нет комментария с Id=" + commentId + "."));
    }

    private User getUser(long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Нет объекта.",
                        "Ошибка: нет пользователя с Id=" + userId + "."));
    }

    private Publication getPublication(long eventId) {
       return publicationRepository
               .findById(eventId)
               .orElseThrow(() -> new NotFoundException("Нет объекта.",
                       "Ошибка: нет события с Id=" + eventId + "."));
    }

    private Reason getReason(long reasonId) {
        Optional<Reason> reason = reasonRepository.findById(reasonId);
        if (reason.isEmpty()) {
            reason = reasonRepository.findByName(standardReason);
            if (reason.isEmpty()) {
                return reasonRepository.save(new Reason(standardReason));
            } else {
                return reason.get();
            }
        } else {
            return reason.get();
        }
    }
}
