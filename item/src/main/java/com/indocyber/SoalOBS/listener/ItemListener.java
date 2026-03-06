package com.indocyber.SoalOBS.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indocyber.SoalOBS.dto.GetItemDTO;
import com.indocyber.SoalOBS.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class ItemListener {

    @Autowired
    private ItemService getItemService;

    @KafkaListener(
            topics = "${kafka.topic.list-item-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "getItemKafkaListenerContainerFactory")
    @SendTo
    public GetItemDTO.Response getItem(String message) throws JsonProcessingException {
        GetItemDTO.Request request = null;
        GetItemDTO.Response response = new GetItemDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, GetItemDTO.Request.class);
            response = getItemService.getAllData(request);
        } catch (Throwable ex){
            return GetItemDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }
        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.list-by-price-item-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "getItemByPriceKafkaListenerContainerFactory")
    @SendTo
    public GetItemDTO.Response getItemByPrice(String message) throws JsonProcessingException {
        GetItemDTO.Request request = null;
        GetItemDTO.Response response = new GetItemDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, GetItemDTO.Request.class);
            response = getItemService.getItemByPrice(request);
        } catch (Throwable ex){
            return GetItemDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.get-item-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "getItemByIdKafkaListenerContainerFactory")
    @SendTo
    public GetItemDTO.Response getItemById(String message) throws JsonProcessingException {
        GetItemDTO.Request request = null;
        GetItemDTO.Response response = new GetItemDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, GetItemDTO.Request.class);
            response = getItemService.getItemById(request);
        } catch (Throwable ex){
            return GetItemDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.add-item-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "insertItemKafkaListenerContainerFactory")
    @SendTo
    public GetItemDTO.Response insertItem(String message) throws JsonProcessingException {
        GetItemDTO.Request request = null;
        GetItemDTO.Response response = new GetItemDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, GetItemDTO.Request.class);
            response = getItemService.insertItem(request);
        } catch (Throwable ex){
            return GetItemDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.delete-item-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "deleteItemByIdKafkaListenerContainerFactory")
    @SendTo
    public GetItemDTO.Response deleteItemById(String message) throws JsonProcessingException {
        GetItemDTO.Request request = null;
        GetItemDTO.Response response = new GetItemDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, GetItemDTO.Request.class);
            response = getItemService.deleteItem(request);
        } catch (Throwable ex){
            return GetItemDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }

    @KafkaListener(
            topics = "${kafka.topic.update-item-req}",
            groupId = "${kafka.consumer.group}",
            containerFactory = "updateItemKafkaListenerContainerFactory")
    @SendTo
    public GetItemDTO.Response updateItem(String message) throws JsonProcessingException {
        GetItemDTO.Request request = null;
        GetItemDTO.Response response = new GetItemDTO.Response();

        try {
            request = new ObjectMapper().readValue(message, GetItemDTO.Request.class);
            response = getItemService.updateItem(request);
        } catch (Throwable ex){
            return GetItemDTO.Response.builder()
                    .error(true)
                    .message("Parsing error").build();
        }

        return response;
    }
}
