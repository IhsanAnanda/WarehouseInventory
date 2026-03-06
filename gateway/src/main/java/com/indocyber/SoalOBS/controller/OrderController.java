package com.indocyber.SoalOBS.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indocyber.SoalOBS.dto.GetOrderDTO;
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
public class OrderController {

    @Value(("${kafka.topic.get-order-req}"))
    private String getOrderTopicRequest;

    @Value(("${kafka.topic.list-by-qty-order-req}"))
    private String getListByQtyOrderTopicRequest;

    @Value(("${kafka.topic.list-order-req}"))
    private String getListOrderTopicRequest;

    @Value(("${kafka.topic.add-order-req}"))
    private String addOrderTopicRequest;

    @Value(("${kafka.topic.update-order-req}"))
    private String updateOrderTopicRequest;

    @Value(("${kafka.topic.delete-order-req}"))
    private String deleteOrderTopicRequest;

    @Autowired
    private ReplyingKafkaTemplate<String, GetOrderDTO.Request, String> getOrderByIdRequestReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetOrderDTO.Request, String> listByQtyOrderReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetOrderDTO.Request, String> listOrderReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetOrderDTO.Request, String> insertOrderRequestReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetOrderDTO.Request, String> updateOrderRequestReplyKafkaTemplate;

    @Autowired
    private ReplyingKafkaTemplate<String, GetOrderDTO.Request, String> deleteOrderRequestReplyKafkaTemplate;

    @PostMapping("getOrderById")
    public ResponseEntity<?> getItemById(
            @RequestBody GetOrderDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetOrderDTO.Request> pr = new ProducerRecord<>(getOrderTopicRequest, request);
        RequestReplyFuture<String, GetOrderDTO.Request, String> future = getOrderByIdRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("getOrderByQty")
    public ResponseEntity<?> getOrderByQty(
            @RequestBody GetOrderDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetOrderDTO.Request> pr = new ProducerRecord<>(getListByQtyOrderTopicRequest, request);
        RequestReplyFuture<String, GetOrderDTO.Request, String> future = listByQtyOrderReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("getOrder")
    public ResponseEntity<?> getOrder(
            @RequestBody GetOrderDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetOrderDTO.Request> pr = new ProducerRecord<>(getListOrderTopicRequest, request);
        RequestReplyFuture<String, GetOrderDTO.Request, String> future = listOrderReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("insertOrder")
    public ResponseEntity<?> insertOrder(
            @RequestBody GetOrderDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetOrderDTO.Request> pr = new ProducerRecord<>(addOrderTopicRequest, request);
        RequestReplyFuture<String, GetOrderDTO.Request, String> future = insertOrderRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("updateOrder")
    public ResponseEntity<?> updateOrder(
            @RequestBody GetOrderDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetOrderDTO.Request> pr = new ProducerRecord<>(updateOrderTopicRequest, request);
        RequestReplyFuture<String, GetOrderDTO.Request, String> future = updateOrderRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }

    @PostMapping("deleteOrder")
    public ResponseEntity<?> deleteOrder(
            @RequestBody GetOrderDTO.Request request
    ) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Request Masuk : "  + request.toString());
//        return ResponseEntity.ok().body(ItemDTO.Response.builder().error(false).message("OK Dulu").build());
        ProducerRecord<String, GetOrderDTO.Request> pr = new ProducerRecord<>(deleteOrderTopicRequest, request);
        RequestReplyFuture<String, GetOrderDTO.Request, String> future = deleteOrderRequestReplyKafkaTemplate.sendAndReceive(pr);
        ConsumerRecord<String, String> record = future.get(5, TimeUnit.MINUTES);
        return ok().body(new ObjectMapper().readValue(record.value(), Map.class));
    }


}
