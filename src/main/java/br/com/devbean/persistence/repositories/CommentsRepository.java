package br.com.devbean.persistence.repositories;

import br.com.devbean.persistence.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentsRepository extends JpaRepository<CommentEntity, Long> {

    Optional<CommentEntity> findByPublicId(UUID pid);
}
