package br.com.devbean.services;

import br.com.devbean.dtos.CommentDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentsService {

    List<CommentDTO> listAll();

    Optional<CommentDTO> save(CommentDTO dto);

    void delete(UUID pid);
}
