package ru.aggregator.messenger.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aggregator.messenger.domain.model.ChannelType;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    Optional<Channel> findByTypeAndEnabledTrue(ChannelType type);
}
