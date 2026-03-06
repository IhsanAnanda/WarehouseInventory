package com.indocyber.SoalOBS.producer;

import com.indocyber.SoalOBS.dto.GetItemDTO;
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
public class GetItemKafkaProducer {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.consumer.group}")
    private String groupId;

    @Value("${kafka.topic.get-item-resp}")
    private String topicResponse;

    @Bean
    public ReplyingKafkaTemplate<String, GetItemDTO.Request, GetItemDTO.Response> getItemByIdRequestReplyKafkaTemplate() {
        return (ReplyingKafkaTemplate<String, GetItemDTO.Request, GetItemDTO.Response>) new ReplyingKafkaTemplate(producerFactory(), container());
    }

    public ProducerFactory<String, GetItemDTO.Request> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    public KafkaTemplate<String, GetItemDTO.Request> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    public ConsumerFactory<String, GetItemDTO.Response> consumerFactory() {
        JsonDeserializer<GetItemDTO.Response> deserializer = new JsonDeserializer<>(GetItemDTO.Response.class);
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

    public ConcurrentKafkaListenerContainerFactory<String, GetItemDTO.Response> containerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GetItemDTO.Response> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setReplyTemplate(kafkaTemplate());
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    public ConcurrentMessageListenerContainer<String, GetItemDTO.Response> container() {
        ConcurrentMessageListenerContainer<String, GetItemDTO.Response>
                container = containerFactory().createContainer(topicResponse);
        container.getContainerProperties().setGroupId(groupId);
        return container;
    }
}
