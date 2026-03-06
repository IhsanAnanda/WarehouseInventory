package com.indocyber.SoalOBS.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ItemTopic {
    @Value("${kafka.topic.get-item-req}")
    private String GetItemReq;

    @Value("${kafka.topic.get-item-resp}")
    private String GetItemResp;

    @Value("${kafka.topic.add-item-req}")
    private String AddItemReq;

    @Value("${kafka.topic.add-item-resp}")
    private String AddItemResp;

    @Value("${kafka.topic.delete-item-req}")
    private String DeleteItemReq;

    @Value("${kafka.topic.delete-item-resp}")
    private String DeleteItemResp;

    @Value("${kafka.topic.update-item-req}")
    private String UpdateItemReq;

    @Value("${kafka.topic.update-item-resp}")
    private String UpdateItemResp;

    @Value("${kafka.topic.list-item-req}")
    private String ListItemReq;

    @Value("${kafka.topic.list-item-resp}")
    private String ListItemResp;

    @Value("${kafka.topic.list-by-price-item-req}")
    private String ListByPriceItemReq;

    @Value("${kafka.topic.list-by-price-item-resp}")
    private String ListByPriceItemResp;

    @Bean
    public NewTopic GetItemReq() {
        return new NewTopic(GetItemReq, 1, (short) 1);
    }

    @Bean
    public NewTopic GetItemResp() {
        return new NewTopic(GetItemResp, 1, (short) 1);
    }

    @Bean
    public NewTopic AddItemReq() {
        return new NewTopic(AddItemReq, 1, (short) 1);
    }

    @Bean
    public NewTopic AddItemResp() {
        return new NewTopic(AddItemResp, 1, (short) 1);
    }

    @Bean
    public NewTopic DeleteItemReq() {
        return new NewTopic(DeleteItemReq, 1, (short) 1);
    }

    @Bean
    public NewTopic DeleteItemResp() {
        return new NewTopic(DeleteItemResp, 1, (short) 1);
    }

    @Bean
    public NewTopic UpdateItemReq() {
        return new NewTopic(UpdateItemReq, 1, (short) 1);
    }

    @Bean
    public NewTopic UpdateItemResp() {
        return new NewTopic(UpdateItemResp, 1, (short) 1);
    }

    @Bean
    public NewTopic ListItemReq() {
        return new NewTopic(ListItemReq, 1, (short) 1);
    }

    @Bean
    public NewTopic ListItemResp() {
        return new NewTopic(ListItemResp, 1, (short) 1);
    }

    @Bean
    public NewTopic ListByPriceItemReq() {
        return new NewTopic(ListByPriceItemReq, 1, (short) 1);
    }

    @Bean
    public NewTopic ListByPriceItemResp() {
        return new NewTopic(ListByPriceItemResp, 1, (short) 1);
    }
}
