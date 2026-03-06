package com.indocyber.SoalOBS.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indocyber.SoalOBS.dto.OrderDTO;
import com.indocyber.SoalOBS.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class GetOrderByIdListener {

    @Autowired
    private OrderService orderService;

    @KafkaListener(
            topics = "${kafka.topic.get-order-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "getOrderByIdKafkaListenerContainerFactory")
    @SendTo
    public OrderDTO.Response getOrderById(String message) throws JsonProcessingException {
        OrderDTO.Request request = null;
        OrderDTO.Response response = new OrderDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, OrderDTO.Request.class);
            response = orderService.getOrderById(request);
        } catch (Throwable ex){
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.list-by-qty-order-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "getOrderByQtyKafkaListenerContainerFactory")
    @SendTo
    public OrderDTO.Response getOrderByQty(String message) throws JsonProcessingException {
        OrderDTO.Request request = null;
        OrderDTO.Response response = new OrderDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, OrderDTO.Request.class);
            response = orderService.getOrderByQty(request);
        } catch (Throwable ex){
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.list-order-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "getOrderKafkaListenerContainerFactory")
    @SendTo
    public OrderDTO.Response getOrder(String message) throws JsonProcessingException {
        OrderDTO.Request request = null;
        OrderDTO.Response response = new OrderDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, OrderDTO.Request.class);
            response = orderService.getOrder(request);
        } catch (Throwable ex){
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.add-order-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "insertOrderKafkaListenerContainerFactory")
    @SendTo
    public OrderDTO.Response insertOrder(String message) throws JsonProcessingException {
        OrderDTO.Request request = null;
        OrderDTO.Response response = new OrderDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, OrderDTO.Request.class);
            response = orderService.insertOrder(request);
        } catch (Throwable ex){
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.update-order-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "updateOrderKafkaListenerContainerFactory")
    @SendTo
    public OrderDTO.Response updateOrder(String message) throws JsonProcessingException {
        OrderDTO.Request request = null;
        OrderDTO.Response response = new OrderDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, OrderDTO.Request.class);
            response = orderService.updateOrder(request);
        } catch (Throwable ex){
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.delete-order-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "deleteOrderKafkaListenerContainerFactory")
    @SendTo
    public OrderDTO.Response deletetOrder(String message) throws JsonProcessingException {
        OrderDTO.Request request = null;
        OrderDTO.Response response = new OrderDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, OrderDTO.Request.class);
            response = orderService.deleteOrder(request);
        } catch (Throwable ex){
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }
}
