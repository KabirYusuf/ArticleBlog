package dev.levelupschool.backend.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tokens", schema = "public", indexes = {
    @Index(name = "index_hashedToken", columnList = "hashedToken")
})
@Setter
@Getter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hashedToken;
    private Long userId;
    private boolean isRevoked;
}
