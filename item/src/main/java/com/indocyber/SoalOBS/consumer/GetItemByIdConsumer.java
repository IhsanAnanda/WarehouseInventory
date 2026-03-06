package com.indocyber.SoalOBS.consumer;

import com.indocyber.SoalOBS.dto.GetItemDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@EnableKafka
@Configuration
public class GetItemByIdConsumer extends BaseKafkaConsumer<GetItemDTO.Response>{
//    @Value(value = "${kafka.bootstrapAddress}")
//    private String bootstrapAddress;
//
//    @Value(value = "${kafka.consumer.group}")
//    private String groupId;
//
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, GetItemDTO.Request>> getItemByIdKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, GetItemDTO.Request> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        // NOTE - set up of reply template
//        factory.setReplyTemplate(kafkaTemplate());
//        return factory;
//    }
//
//    public ConsumerFactory<String, GetItemDTO.Request> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    public KafkaTemplate<String, GetItemDTO.Response> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    public ProducerFactory<String, GetItemDTO.Response> producerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.consumer.group}")
    private String groupId;

    @Override
    protected String getBootstrapAddr() {
        return bootstrapAddress;
    }

    @Override
    protected String getGroupId() {
        return groupId;
    }

    @Bean
    public KafkaListenerContainerFactory< ConcurrentMessageListenerContainer<String, String> >
    getItemByIdKafkaListenerContainerFactory(){
        return this.getKafkaListenerContainerFactory();
    }


}
