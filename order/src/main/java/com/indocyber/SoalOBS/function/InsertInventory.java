package com.indocyber.SoalOBS.function;

import com.indocyber.SoalOBS.dto.GetInventoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class InsertInventory {
    @Value("${kafka.topic.add-inventory-req}")
    private String topicAddInventoryRequest;

    @Autowired
    private ReplyingKafkaTemplate<String, GetInventoryDTO.Request, GetInventoryDTO.Response> insertInventoryRequestReplyKafkaTemplate;

    public GetInventoryDTO.Response insertInventory(Integer itemId, Integer qty, String type) {
        GetInventoryDTO.Response response = new GetInventoryDTO.Response();

        try {
            ProducerRecord<String, GetInventoryDTO.Request> pr = new ProducerRecord<>(
                    topicAddInventoryRequest,
                    GetInventoryDTO.Request.builder()
                            .itemId(itemId)
                            .qty(qty)
                            .type(type)
                            .build());
            RequestReplyFuture<String, GetInventoryDTO.Request, GetInventoryDTO.Response> future = insertInventoryRequestReplyKafkaTemplate.sendAndReceive(pr);
            ConsumerRecord<String, GetInventoryDTO.Response> record = future.get(5, TimeUnit.MINUTES);
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
