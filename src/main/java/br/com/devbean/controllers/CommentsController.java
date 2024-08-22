package br.com.devbean.controllers;

import br.com.devbean.controllers.requests.CommentRequest;
import br.com.devbean.dtos.CommentDTO;
import br.com.devbean.services.CommentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/comments")
public class CommentsController {

    private final CommentsService service;

    public CommentsController(CommentsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> listComments() {
        return ResponseEntity.ok(service.listAll());
    }

    @PostMapping
    public ResponseEntity<?> postComments(@RequestBody CommentRequest request) {
        return ResponseEntity.ok(
                service.save(request.toDTO())
        );
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@PathVariable UUID pid) {
        service.delete(pid);
        return ResponseEntity.noContent().build();
    }

}
