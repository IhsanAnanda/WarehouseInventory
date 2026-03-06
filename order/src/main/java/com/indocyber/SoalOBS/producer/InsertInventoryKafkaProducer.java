package com.indocyber.SoalOBS.producer;

import com.indocyber.SoalOBS.dto.GetInventoryDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class InsertInventoryKafkaProducer {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.consumer.group}")
    private String groupId;

    @Value("${kafka.topic.add-inventory-resp}")
    private String topicResponse;

    @Bean
    public ReplyingKafkaTemplate<String, GetInventoryDTO.Request, GetInventoryDTO.Response> insertInventoryRequestReplyKafkaTemplate() {
        return (ReplyingKafkaTemplate<String, GetInventoryDTO.Request, GetInventoryDTO.Response>) new ReplyingKafkaTemplate(producerFactory(), container());
    }

    public ProducerFactory<String, GetInventoryDTO.Request> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    public KafkaTemplate<String, GetInventoryDTO.Request> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    public ConsumerFactory<String, GetInventoryDTO.Response> consumerFactory() {
        JsonDeserializer<GetInventoryDTO.Response> deserializer = new JsonDeserializer<>(GetInventoryDTO.Response.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    public ConcurrentKafkaListenerContainerFactory<String, GetInventoryDTO.Response> containerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GetInventoryDTO.Response> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setReplyTemplate(kafkaTemplate());
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    public ConcurrentMessageListenerContainer<String, GetInventoryDTO.Response> container() {
        ConcurrentMessageListenerContainer<String, GetInventoryDTO.Response>
                container = containerFactory().createContainer(topicResponse);
        container.getContainerProperties().setGroupId(groupId);
        return container;
    }
}
