package br.com.devbean.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_COMMENTS")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( name = "ID", unique = true, nullable = false)
    private Long id;

    @Column( name = "PUBLIC_ID", nullable = false)
    private UUID publicId;

    @Column( name = "BODY", nullable = false)
    private String body;

    @Column( name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column( name = "USER_ID", nullable = false)
    private Long userId;

    @PrePersist
    private void init() {
        this.publicId = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

}
