package com.indocyber.SoalOBS.function;

import com.indocyber.SoalOBS.dto.GetItemDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class GetItemById {

    @Value("${kafka.topic.get-item-req}")
    private String topicGetItemRequest;

    @Autowired
    private ReplyingKafkaTemplate<String, GetItemDTO.Request, GetItemDTO.Response> getItemByIdRequestReplyKafkaTemplate;

    public GetItemDTO.Response getItemById(Integer id) {
        GetItemDTO.Response response = new GetItemDTO.Response();

        try {
            ProducerRecord<String, GetItemDTO.Request> pr = new ProducerRecord<>(
                    topicGetItemRequest,
                    GetItemDTO.Request.builder()
                            .id(id)
                            .build());
            RequestReplyFuture<String, GetItemDTO.Request, GetItemDTO.Response> future = getItemByIdRequestReplyKafkaTemplate.sendAndReceive(pr);
            ConsumerRecord<String, GetItemDTO.Response> record = future.get(5, TimeUnit.MINUTES);
            response = record.value();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.setError(true);
            response.setMessage(e.getMessage());
            return response;
        }

        return response;
    }
}
