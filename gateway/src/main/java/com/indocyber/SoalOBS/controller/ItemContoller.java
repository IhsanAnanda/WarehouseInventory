package com.indocyber.SoalOBS.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indocyber.SoalOBS.dto.GetItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping
public class ItemContoller {

    @Value(("${kafka.topic.get-item-req}"))
    private String getItemTopicRequest;

    @Value(("${kafka.topic.list-by-price-item-req}"))
    private String listItemByPriceTopicRequest;

    @Value(("${kafka.topic.list-item-req}"))
    private String listItemTopicRequest;

    @Value(("${kafka.topic.add-item-req}"))
    private String addItemTopicRequest;

    @Value(("${kafka.topic.delete-item-req}"))
    private String deleteItemTopicRequest;

    @Value(("${kafka.topic.update-item-req}"))
    private String updateItemTopicRequest;

    @Autowired
    private ReplyingKafkaTemplate<String, GetItemDTO.Request, String> getItemByIdRequestReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetItemDTO.Request, String> listItemByPriceRequestReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetItemDTO.Request, String> listItemReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetItemDTO.Request, String> insertItemRequestReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetItemDTO.Request, String> deleteItemByIdRequestReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetItemDTO.Request, String> updateItemRequestReplyKafkaTemplate;

    @PostMapping("getItemById")
    public ResponseEntity<?> getItemById(
        @RequestBody GetItemDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException{
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetItemDTO.Request> pr = new ProducerRecord<>(getItemTopicRequest, request);
        RequestReplyFuture<String, GetItemDTO.Request, String> future = getItemByIdRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("getItemByPrice")
    public ResponseEntity<?> getItemByPrice(
            @RequestBody GetItemDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException{
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetItemDTO.Request> pr = new ProducerRecord<>(listItemByPriceTopicRequest, request);
        RequestReplyFuture<String, GetItemDTO.Request, String> future = listItemByPriceRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("getItem")
    public ResponseEntity<?> getItem(
            @RequestBody GetItemDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException{
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetItemDTO.Request> pr = new ProducerRecord<>(listItemTopicRequest, request);
        RequestReplyFuture<String, GetItemDTO.Request, String> future = listItemReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("insertItem")
    public ResponseEntity<?> insertItem(
            @RequestBody GetItemDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException{
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetItemDTO.Request> pr = new ProducerRecord<>(addItemTopicRequest, request);
        RequestReplyFuture<String, GetItemDTO.Request, String> future = insertItemRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("deleteItem")
    public ResponseEntity<?> deleteItem(
            @RequestBody GetItemDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException{
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetItemDTO.Request> pr = new ProducerRecord<>(deleteItemTopicRequest, request);
        RequestReplyFuture<String, GetItemDTO.Request, String> future = deleteItemByIdRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("updateItem")
    public ResponseEntity<?> updateItem(
            @RequestBody GetItemDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException{
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetItemDTO.Request> pr = new ProducerRecord<>(updateItemTopicRequest, request);
        RequestReplyFuture<String, GetItemDTO.Request, String> future = updateItemRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }
}
