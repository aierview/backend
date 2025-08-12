package com.aierview.backend.interview.infra.persistence.entity;

import com.aierview.backend.auth.infra.persistence.entity.UserJpaEntity;
import com.aierview.backend.interview.domain.enums.InterviewLevel;
import com.aierview.backend.interview.domain.enums.InterviewRole;
import com.aierview.backend.interview.domain.enums.InterviewStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_interviews")
public class InterviewJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @Enumerated(EnumType.STRING)
    private InterviewRole role;

    @Enumerated(EnumType.STRING)
    private InterviewLevel level;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "interview", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionJpaEntity> questions = new ArrayList<>();
}
