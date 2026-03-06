package com.indocyber.SoalOBS.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OrderTopic {

    @Value("${kafka.topic.get-order-req}")
    private String GetOrderReq;

    @Value("${kafka.topic.get-order-resp}")
    private String GetOrderResp;

    @Value("${kafka.topic.add-order-req}")
    private String AddOrderReq;

    @Value("${kafka.topic.add-order-resp}")
    private String AddOrderResp;

    @Value("${kafka.topic.delete-order-req}")
    private String DeleteOrderReq;

    @Value("${kafka.topic.delete-order-resp}")
    private String DeleteOrderResp;

    @Value("${kafka.topic.update-order-req}")
    private String UpdateOrderReq;

    @Value("${kafka.topic.update-order-resp}")
    private String UpdateOrderResp;

    @Value("${kafka.topic.list-order-req}")
    private String ListOrderReq;

    @Value("${kafka.topic.list-order-resp}")
    private String ListOrderResp;

    @Value("${kafka.topic.list-by-qty-order-req}")
    private String ListByQtyOrderReq;

    @Value("${kafka.topic.list-by-qty-order-resp}")
    private String ListByQtyOrderResp;

    @Bean
    public NewTopic GetorderReq() {
        return new NewTopic(GetOrderReq, 1, (short) 1);
    }

    @Bean
    public NewTopic GetorderResp() {
        return new NewTopic(GetOrderResp, 1, (short) 1);
    }

    @Bean
    public NewTopic AddOrderReq() {
        return new NewTopic(AddOrderReq, 1, (short) 1);
    }

    @Bean
    public NewTopic AddOrderResp() {
        return new NewTopic(AddOrderResp, 1, (short) 1);
    }

    @Bean
    public NewTopic DeleteOrderReq() {
        return new NewTopic(DeleteOrderReq, 1, (short) 1);
    }

    @Bean
    public NewTopic DeleteOrderResp() {
        return new NewTopic(DeleteOrderResp, 1, (short) 1);
    }

    @Bean
    public NewTopic UpdateOrderReq() {
        return new NewTopic(UpdateOrderReq, 1, (short) 1);
    }

    @Bean
    public NewTopic UpdateOrderResp() {
        return new NewTopic(UpdateOrderResp, 1, (short) 1);
    }

    @Bean
    public NewTopic ListOrderReq() {
        return new NewTopic(ListOrderReq, 1, (short) 1);
    }

    @Bean
    public NewTopic ListOrderResp() {
        return new NewTopic(ListOrderResp, 1, (short) 1);
    }

    @Bean
    public NewTopic ListByQtyOrderReq() {
        return new NewTopic(ListByQtyOrderReq, 1, (short) 1);
    }

    @Bean
    public NewTopic ListByQtyOrderResp() {
        return new NewTopic(ListByQtyOrderResp, 1, (short) 1);
    }
}
