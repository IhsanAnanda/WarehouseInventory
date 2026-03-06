package com.indocyber.SoalOBS.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InventoryTopic {

    @Value("${kafka.topic.get-inventory-req}")
    private String GetInventoryReq;

    @Value("${kafka.topic.get-inventory-resp}")
    private String GetInventoryResp;

    @Value("${kafka.topic.add-inventory-req}")
    private String AddInventoryReq;

    @Value("${kafka.topic.add-inventory-resp}")
    private String AddInventoryResp;

    @Value("${kafka.topic.delete-inventory-req}")
    private String DeleteInventoryReq;

    @Value("${kafka.topic.delete-inventory-resp}")
    private String DeleteInventoryResp;

    @Value("${kafka.topic.update-inventory-req}")
    private String UpdateInventoryReq;

    @Value("${kafka.topic.update-inventory-resp}")
    private String UpdateInventoryResp;

    @Value("${kafka.topic.list-inventory-req}")
    private String ListInventoryReq;

    @Value("${kafka.topic.list-inventory-resp}")
    private String ListInventoryResp;

    @Value("${kafka.topic.list-by-type-inventory-req}")
    private String ListByTypeInventoryReq;

    @Value("${kafka.topic.list-by-type-inventory-resp}")
    private String ListByTypeInventoryResp;

    @Value("${kafka.topic.get-item-stock-req}")
    private String getItemStockReq;

    @Value("${kafka.topic.get-item-stock-resp}")
    private String getItemStockResp;

    @Bean
    public NewTopic GetInventoryReq() {
        return new NewTopic(GetInventoryReq, 1, (short) 1);
    }

    @Bean
    public NewTopic GetInventoryResp() {
        return new NewTopic(GetInventoryResp, 1, (short) 1);
    }

    @Bean
    public NewTopic AddInventoryReq() {
        return new NewTopic(AddInventoryReq, 1, (short) 1);
    }

    @Bean
    public NewTopic AddInventoryResp() {
        return new NewTopic(AddInventoryResp, 1, (short) 1);
    }

    @Bean
    public NewTopic DeleteInventoryReq() {
        return new NewTopic(DeleteInventoryReq, 1, (short) 1);
    }

    @Bean
    public NewTopic DeleteInventoryResp() {
        return new NewTopic(DeleteInventoryResp, 1, (short) 1);
    }

    @Bean
    public NewTopic UpdateInventoryReq() {
        return new NewTopic(UpdateInventoryReq, 1, (short) 1);
    }

    @Bean
    public NewTopic UpdateInventoryResp() {
        return new NewTopic(UpdateInventoryResp, 1, (short) 1);
    }

    @Bean
    public NewTopic ListInventoryReq() {
        return new NewTopic(ListInventoryReq, 1, (short) 1);
    }

    @Bean
    public NewTopic ListInventoryResp() {
        return new NewTopic(ListInventoryResp, 1, (short) 1);
    }

    @Bean
    public NewTopic ListByTypeInventoryReq() {
        return new NewTopic(ListByTypeInventoryReq, 1, (short) 1);
    }

    @Bean
    public NewTopic ListByTypeInventoryResp() {
        return new NewTopic(ListByTypeInventoryResp, 1, (short) 1);
    }

    @Bean
    public NewTopic getItemStockReq() {
        return new NewTopic(getItemStockReq, 1, (short) 1);
    }

    @Bean
    public NewTopic getItemStockResp() {
        return new NewTopic(getItemStockResp, 1, (short) 1);
    }
}
