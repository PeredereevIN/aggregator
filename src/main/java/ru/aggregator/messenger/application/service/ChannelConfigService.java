package ru.aggregator.messenger.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aggregator.messenger.domain.model.ChannelType;
import ru.aggregator.messenger.infrastructure.persistence.Channel;
import ru.aggregator.messenger.infrastructure.persistence.ChannelRepository;

import java.util.List;
import java.util.UUID;

/**
 * Сервис управления конфигурацией каналов.
 */
@Service
@RequiredArgsConstructor
public class ChannelConfigService {

    private final ChannelRepository channelRepository;

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Transactional
    public Channel addChannel(ChannelType type, String configJson) {
        Channel channel = Channel.builder()
                .type(type)
                .config(configJson)
                .enabled(true)
                .createdAt(java.time.Instant.now())
                .build();
        return channelRepository.save(channel);
    }

    @Transactional
    public void disableChannel(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found: " + id));
        channel.setEnabled(false);
        channelRepository.save(channel);
    }
}
