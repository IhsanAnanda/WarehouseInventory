package com.indocyber.SoalOBS.producer;

import com.indocyber.SoalOBS.dto.GetOrderDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
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
public class InsertOrderKafkaProducer {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.consumer.group}")
    private String groupId;

    @Value("${kafka.topic.add-order-resp}")
    private String topicResponse;

    @Bean
    public ReplyingKafkaTemplate<String, GetOrderDTO.Request, String> insertOrderRequestReplyKafkaTemplate(
            ConcurrentKafkaListenerContainerFactory<String, GetOrderDTO.Request> containerFactory
    ) {
        containerFactory.setReplyTemplate(registerKafkaTemplate(registerProducerFactory()));
        ConcurrentMessageListenerContainer<String, GetOrderDTO.Request> container = registerReplyContainer(containerFactory);
        return (ReplyingKafkaTemplate<String, GetOrderDTO.Request, String>) new ReplyingKafkaTemplate(registerProducerFactory(), container);
    }

    public KafkaTemplate<String, GetOrderDTO.Request> registerKafkaTemplate(ProducerFactory<String, GetOrderDTO.Request> pf) {
        return new KafkaTemplate<>(pf);
    }

    public ProducerFactory<String, GetOrderDTO.Request> registerProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    public ConcurrentMessageListenerContainer<String, GetOrderDTO.Request> registerReplyContainer(
            ConcurrentKafkaListenerContainerFactory<String, GetOrderDTO.Request> containerFactory
    ) {
        ConcurrentMessageListenerContainer<String, GetOrderDTO.Request>
                container = containerFactory.createContainer(topicResponse);
        container.getContainerProperties().setGroupId(groupId);
        return container;
    }
}
