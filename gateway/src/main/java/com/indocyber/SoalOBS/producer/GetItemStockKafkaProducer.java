package com.indocyber.SoalOBS.producer;

import com.indocyber.SoalOBS.dto.GetInventoryDTO;
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

import java.util.HashMap;
import java.util.Map;

@Configuration
public class GetItemStockKafkaProducer {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.consumer.group}")
    private String groupId;

    @Value("${kafka.topic.get-item-stock-resp}")
    private String topicResponse;

    @Bean
    public ReplyingKafkaTemplate<String, GetInventoryDTO.Request, String> getItemStockRequestReplyKafkaTemplate(
            ConcurrentKafkaListenerContainerFactory<String, GetInventoryDTO.Request> containerFactory
    ) {
        containerFactory.setReplyTemplate(registerKafkaTemplate(registerProducerFactory()));
        ConcurrentMessageListenerContainer<String, GetInventoryDTO.Request> container = registerReplyContainer(containerFactory);
        return (ReplyingKafkaTemplate<String, GetInventoryDTO.Request, String>) new ReplyingKafkaTemplate(registerProducerFactory(), container);
    }

    public KafkaTemplate<String, GetInventoryDTO.Request> registerKafkaTemplate(ProducerFactory<String, GetInventoryDTO.Request> pf) {
        return new KafkaTemplate<>(pf);
    }

    public ProducerFactory<String, GetInventoryDTO.Request> registerProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    public ConcurrentMessageListenerContainer<String, GetInventoryDTO.Request> registerReplyContainer(
            ConcurrentKafkaListenerContainerFactory<String, GetInventoryDTO.Request> containerFactory
    ) {
        ConcurrentMessageListenerContainer<String, GetInventoryDTO.Request>
                container = containerFactory.createContainer(topicResponse);
        container.getContainerProperties().setGroupId(groupId);
        return container;
    }
}
