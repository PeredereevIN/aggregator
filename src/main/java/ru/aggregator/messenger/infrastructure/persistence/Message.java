package ru.aggregator.messenger.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;
import ru.aggregator.messenger.domain.model.Direction;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    private String conversationId;

    // Adjacency List: ссылка на родительское сообщение в цепочке
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_message_id")
    private Message parentMessage;

    private String externalMessageId;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Column(nullable = false, length = 5000)
    private String content;
    private String contentType = "text";

    // Ключ идемпотентности (уникальный)
    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    private Instant timestamp;
    private Instant processedAt;
}
