package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.comment.Comment;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT b from Comment b WHERE b.description LIKE %:search%")
    Page<Comment> getCommentsSearchPublic(@Param("search") String search, Pageable pageable);
}
