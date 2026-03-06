package com.indocyber.SoalOBS.producer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class BaseReplyKafkaProducer<V, R> {
    protected abstract String getTopicResponse();
    protected abstract Class<?> getKafkaTemplateValueSerializerClass();
    protected abstract Class<?> getKafkaContainerValueDeserializerClass();

    protected abstract String getBootstrapAddress();
    protected abstract String getGroupId();

    protected ReplyingKafkaTemplate<String, V, R>
    registerReplyingKafkaTemplate( ConcurrentKafkaListenerContainerFactory<String, R> kafkaListenerContainerFactory) {
        kafkaListenerContainerFactory.setReplyTemplate(registerKafkaTemplate(registerProducerFactory()));
        ConcurrentMessageListenerContainer<String, R> container = registerReplyContainer(kafkaListenerContainerFactory);
        return (ReplyingKafkaTemplate<String, V, R>) new ReplyingKafkaTemplate(registerProducerFactory(), container);
    }

    protected KafkaTemplate<String, V> registerKafkaTemplate(ProducerFactory<String, V> pf) {
        return new KafkaTemplate<>(pf);
    }

    protected ProducerFactory<String, V> registerProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapAddress());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, getKafkaTemplateValueSerializerClass());
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    protected ConcurrentMessageListenerContainer<String, R>
    registerReplyContainer(ConcurrentKafkaListenerContainerFactory<String, R> containerFactory) {
        ConcurrentMessageListenerContainer<String, R>
                container = containerFactory.createContainer(getTopicResponse());
        Properties configProps = new Properties();
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, getKafkaContainerValueDeserializerClass());
        if(JsonDeserializer.class.equals(getKafkaContainerValueDeserializerClass()))
            configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        container.getContainerProperties().setGroupId(getGroupId());
        container.getContainerProperties().setKafkaConsumerProperties(configProps);
        return container;
    }
}
