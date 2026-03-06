package com.indocyber.SoalOBS.producer;

import com.indocyber.SoalOBS.dto.GetItemDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class InsertItemKafkaProducer {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.consumer.group}")
    private String groupId;

    @Value("${kafka.topic.add-item-resp}")
    private String topicResponse;

    @Bean
    public ReplyingKafkaTemplate<String, GetItemDTO.Request, String> insertItemRequestReplyKafkaTemplate(
            ConcurrentKafkaListenerContainerFactory<String, GetItemDTO.Request> containerFactory
    ) {
        containerFactory.setReplyTemplate(registerKafkaTemplate(registerProducerFactory()));
        ConcurrentMessageListenerContainer<String, GetItemDTO.Request> container = registerReplyContainer(containerFactory);
        return (ReplyingKafkaTemplate<String, GetItemDTO.Request, String>) new ReplyingKafkaTemplate(registerProducerFactory(), container);
    }

    public KafkaTemplate<String, GetItemDTO.Request> registerKafkaTemplate(ProducerFactory<String, GetItemDTO.Request> pf) {
        return new KafkaTemplate<>(pf);
    }

    public ProducerFactory<String, GetItemDTO.Request> registerProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    public ConcurrentMessageListenerContainer<String, GetItemDTO.Request> registerReplyContainer(
            ConcurrentKafkaListenerContainerFactory<String, GetItemDTO.Request> containerFactory
    ) {
        ConcurrentMessageListenerContainer<String, GetItemDTO.Request>
                container = containerFactory.createContainer(topicResponse);
        container.getContainerProperties().setGroupId(groupId);
        return container;
    }
}
