package dev.levelupschool.backend.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens", schema = "public")
@Setter
@Getter
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Long userId;
    private LocalDateTime expiredAt;
}
