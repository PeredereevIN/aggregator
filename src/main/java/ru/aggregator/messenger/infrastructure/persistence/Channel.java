package ru.aggregator.messenger.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;
import ru.aggregator.messenger.domain.model.ChannelType;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "channels")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelType type;

    @Column(columnDefinition = "jsonb")
    private String config; // хранит JSON с токенами и настройками

    @Builder.Default
    private boolean enabled = true;

    private Instant createdAt;
}
