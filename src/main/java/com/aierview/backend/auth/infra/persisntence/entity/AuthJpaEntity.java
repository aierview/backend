package com.aierview.backend.auth.infra.persisntence.entity;


import com.aierview.backend.auth.domain.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_auth")
public class AuthJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String picture;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserJpaEntity user;
}
