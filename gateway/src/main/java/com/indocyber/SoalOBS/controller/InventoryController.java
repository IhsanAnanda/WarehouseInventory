package com.indocyber.SoalOBS.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indocyber.SoalOBS.dto.GetInventoryDTO;
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
public class InventoryController {

    @Value(("${kafka.topic.get-inventory-req}"))
    private String getInventoryTopicRequest;

    @Value(("${kafka.topic.list-by-type-inventory-req}"))
    private String getListByTypeInventoryTopicRequest;

    @Value(("${kafka.topic.list-inventory-req}"))
    private String getListInventoryTopicRequest;

    @Value(("${kafka.topic.add-inventory-req}"))
    private String addInventoryTopicRequest;

    @Value(("${kafka.topic.delete-inventory-req}"))
    private String deleteInventoryTopicRequest;

    @Value(("${kafka.topic.update-inventory-req}"))
    private String updateInventoryTopicRequest;

    @Value(("${kafka.topic.get-item-stock-req}"))
    private String getItemStockTopicRequest;

    @Autowired
    private ReplyingKafkaTemplate<String, GetInventoryDTO.Request, String> getInventoryByItemIdmRequestReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetInventoryDTO.Request, String> listByTypeInventoryReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetInventoryDTO.Request, String> listInventoryReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetInventoryDTO.Request, String> insertInventoryRequestReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetInventoryDTO.Request, String> deleteInventoryByItemIdmRequestReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetInventoryDTO.Request, String> updateInventoryRequestReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetInventoryDTO.Request, String> getItemStockRequestReplyKafkaTemplate;

    @PostMapping("getInventoryById")
    public ResponseEntity<?> getInventoryById(
            @RequestBody GetInventoryDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetInventoryDTO.Request> pr = new ProducerRecord<>(getInventoryTopicRequest, request);
        RequestReplyFuture<String, GetInventoryDTO.Request, String> future = getInventoryByItemIdmRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("getInventoryByType")
    public ResponseEntity<?> getInventoryByType(
            @RequestBody GetInventoryDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetInventoryDTO.Request> pr = new ProducerRecord<>(getListByTypeInventoryTopicRequest, request);
        RequestReplyFuture<String, GetInventoryDTO.Request, String> future = listByTypeInventoryReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("getInventory")
    public ResponseEntity<?> getInventory(
            @RequestBody GetInventoryDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetInventoryDTO.Request> pr = new ProducerRecord<>(getListInventoryTopicRequest, request);
        RequestReplyFuture<String, GetInventoryDTO.Request, String> future = listInventoryReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("insertInventory")
    public ResponseEntity<?> insertInventory(
            @RequestBody GetInventoryDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetInventoryDTO.Request> pr = new ProducerRecord<>(addInventoryTopicRequest, request);
        RequestReplyFuture<String, GetInventoryDTO.Request, String> future = insertInventoryRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("deleteInventory")
    public ResponseEntity<?> deleteInventory(
            @RequestBody GetInventoryDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetInventoryDTO.Request> pr = new ProducerRecord<>(deleteInventoryTopicRequest, request);
        RequestReplyFuture<String, GetInventoryDTO.Request, String> future = deleteInventoryByItemIdmRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("updateInventory")
    public ResponseEntity<?> updateInventory(
            @RequestBody GetInventoryDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetInventoryDTO.Request> pr = new ProducerRecord<>(updateInventoryTopicRequest, request);
        RequestReplyFuture<String, GetInventoryDTO.Request, String> future = updateInventoryRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("getItemStock")
    public ResponseEntity<?> getItemStock(
            @RequestBody GetInventoryDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetInventoryDTO.Request> pr = new ProducerRecord<>(getItemStockTopicRequest, request);
        RequestReplyFuture<String, GetInventoryDTO.Request, String> future = getItemStockRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }
}
