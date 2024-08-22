package br.com.devbean.dtos;

import br.com.devbean.persistence.entities.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    private Long id;
    private UUID publicId;
    private String body;
    private LocalDateTime createdAt;
    private Long userId;

    // Método para converter de CommentEntity para CommentDTO
    public static CommentDTO fromEntity(CommentEntity entity) {
        return CommentDTO.builder()
            .id(entity.getId())
            .publicId(entity.getPublicId())
            .body(entity.getBody())
            .createdAt(entity.getCreatedAt())
            .userId(entity.getUserId())
            .build();
    }

    // Método para converter de CommentDTO para CommentEntity
    public static CommentEntity toEntity(CommentDTO dto) {
        CommentEntity entity = new CommentEntity();
        entity.setId(dto.getId());
        entity.setPublicId(dto.getPublicId());
        entity.setBody(dto.getBody());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUserId(dto.getUserId());
        return entity;
    }
}
