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
public class GetItemByIdListener {
    @Autowired
    private ItemService getItemService;


}
