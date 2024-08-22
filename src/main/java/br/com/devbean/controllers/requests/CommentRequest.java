package br.com.devbean.controllers.requests;

import br.com.devbean.dtos.CommentDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @JsonProperty("public_id")
    private UUID publicId;

    @JsonProperty("body")
    private String body;

    @JsonProperty("user_id")
    private Long userId;

    public CommentDTO toDTO() {
        return CommentDTO.builder()
                .body(this.body)
                .userId(this.userId)
                .build();
    }

}