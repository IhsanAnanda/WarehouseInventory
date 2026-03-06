package com.indocyber.SoalOBS.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indocyber.SoalOBS.dto.GetInventoryDTO;
import com.indocyber.SoalOBS.entity.Inventory;
import com.indocyber.SoalOBS.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetInventoryByItemIdListener {

    @Autowired
    private InventoryService inventoryService;

    @KafkaListener(
            topics = "${kafka.topic.get-inventory-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "getInventoryByItemIdKafkaListenerContainerFactory")
    @SendTo
    public GetInventoryDTO.Response getInventoryByItemId(String message) throws JsonProcessingException {
        GetInventoryDTO.Request request = null;
        GetInventoryDTO.Response response = new GetInventoryDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, GetInventoryDTO.Request.class);
            response = inventoryService.getInventoryById(request);
        } catch (Throwable ex){
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.list-by-type-inventory-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "getInventoryByTypeKafkaListenerContainerFactory")
    @SendTo
    public GetInventoryDTO.Response getInventoryByType(String message) throws JsonProcessingException {
        GetInventoryDTO.Request request = null;
        GetInventoryDTO.Response response = new GetInventoryDTO.Response();
        log.info("Request Masuk : " + request);
        try {
            request = new ObjectMapper().readValue(message, GetInventoryDTO.Request.class);
            response = inventoryService.getInventoryByType(request);
        } catch (Throwable ex){
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.list-inventory-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "getInventoryKafkaListenerContainerFactory")
    @SendTo
    public GetInventoryDTO.Response getInventory(String message) throws JsonProcessingException {
        GetInventoryDTO.Request request = null;
        GetInventoryDTO.Response response = new GetInventoryDTO.Response();
        log.info("Request Masuk : " + request);
        try {
            request = new ObjectMapper().readValue(message, GetInventoryDTO.Request.class);
            response = inventoryService.getAllData(request);
        } catch (Throwable ex){
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.add-inventory-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "insertInventoryKafkaListenerContainerFactory")
    @SendTo
    public GetInventoryDTO.Response insertInventory(String message) throws JsonProcessingException {
        GetInventoryDTO.Request request = null;
        GetInventoryDTO.Response response = new GetInventoryDTO.Response();
        try {
            request = new ObjectMapper().readValue(message, GetInventoryDTO.Request.class);
            log.info("Request insertInventory Masuk : " + request);
            response = inventoryService.insertInventory(request);
        } catch (Throwable ex){
            ex.printStackTrace();
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }


    @KafkaListener(
            topics = "${kafka.topic.delete-inventory-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "deleteInventoryKafkaListenerContainerFactory")
    @SendTo
    public GetInventoryDTO.Response deleteInventory(String message) throws JsonProcessingException {
        GetInventoryDTO.Request request = null;
        GetInventoryDTO.Response response = new GetInventoryDTO.Response();
        try {
            request = new ObjectMapper().readValue(message, GetInventoryDTO.Request.class);
            log.info("Request deleteInventory Masuk : " + request);
            response = inventoryService.deleteInventory(request);
        } catch (Throwable ex){
            ex.printStackTrace();
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.update-inventory-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "updateInventoryKafkaListenerContainerFactory")
    @SendTo
    public GetInventoryDTO.Response updateInventory(String message) throws JsonProcessingException {
        GetInventoryDTO.Request request = null;
        GetInventoryDTO.Response response = new GetInventoryDTO.Response();
        try {
            request = new ObjectMapper().readValue(message, GetInventoryDTO.Request.class);
            log.info("Request deleteInventory Masuk : " + request);
            response = inventoryService.updateInventory(request);
        } catch (Throwable ex){
            ex.printStackTrace();
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.get-item-stock-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "getItemStockKafkaListenerContainerFactory")
    @SendTo
    public GetInventoryDTO.Response getItemStock(String message) throws JsonProcessingException {
        GetInventoryDTO.Request request = null;
        GetInventoryDTO.Response response = new GetInventoryDTO.Response();
        try {
            request = new ObjectMapper().readValue(message, GetInventoryDTO.Request.class);
            log.info("Request getItemStock Masuk : " + request);
            response = inventoryService.getItemStock(request);
        } catch (Throwable ex){
            ex.printStackTrace();
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }
}
