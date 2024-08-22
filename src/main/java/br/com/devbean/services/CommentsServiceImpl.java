package br.com.devbean.services;

import br.com.devbean.annotations.LogMethod;
import br.com.devbean.annotations.NotifyMessage;
import br.com.devbean.constants.NotifyQueueEvent;
import br.com.devbean.dtos.CommentDTO;
import br.com.devbean.persistence.entities.CommentEntity;
import br.com.devbean.persistence.repositories.CommentsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository repository;

    public CommentsServiceImpl(CommentsRepository repository) {
        this.repository = repository;
    }

    @LogMethod
    @Override
    public List<CommentDTO> listAll() {
        return this.repository
                .findAll()
                .stream()
                .map(CommentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @NotifyMessage(queue = NotifyQueueEvent.NOTIFY)
    @Override
    public Optional<CommentDTO> save(CommentDTO dto) {
        CommentEntity saved = repository.save(CommentDTO.toEntity(dto));
        return Optional.of(CommentDTO.fromEntity(saved));
    }

    @Override
    public void delete(UUID pid) {
        Optional<CommentEntity> commentOptional = repository.findByPublicId(pid);
        commentOptional.ifPresent(repository::delete);
    }
}
