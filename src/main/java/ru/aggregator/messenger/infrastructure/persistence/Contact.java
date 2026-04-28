package ru.aggregator.messenger.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "contacts",
       uniqueConstraints = @UniqueConstraint(columnNames = {"channel_id", "external_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    private String firstName;
    private String lastName;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    private Instant createdAt;
}
