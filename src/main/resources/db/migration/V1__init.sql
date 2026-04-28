CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE channels (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    type VARCHAR(20) NOT NULL CHECK (type IN ('VK','TELEGRAM','WHATSAPP')),
    config JSONB NOT NULL DEFAULT '{}',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE contacts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    channel_id UUID NOT NULL REFERENCES channels(id),
    external_id VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    metadata JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(channel_id, external_id)
);

CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    channel_id UUID NOT NULL REFERENCES channels(id),
    contact_id UUID NOT NULL REFERENCES contacts(id),
    conversation_id VARCHAR(255),
    parent_message_id UUID REFERENCES messages(id),
    external_message_id VARCHAR(255),
    direction VARCHAR(10) NOT NULL CHECK (direction IN ('IN','OUT')),
    content TEXT NOT NULL,
    content_type VARCHAR(50) DEFAULT 'text',
    idempotency_key VARCHAR(255) UNIQUE,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    processed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_messages_conversation ON messages(channel_id, conversation_id);
CREATE INDEX idx_messages_parent ON messages(parent_message_id);
CREATE INDEX idx_messages_contact ON messages(contact_id);
