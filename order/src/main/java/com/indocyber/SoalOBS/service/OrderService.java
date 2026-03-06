package com.indocyber.SoalOBS.service;

import com.indocyber.SoalOBS.dto.OrderDTO;

public interface OrderService {
    public OrderDTO.Response getOrderById(OrderDTO.Request request);

    public OrderDTO.Response getOrderByQty(OrderDTO.Request request);

    public OrderDTO.Response getOrder(OrderDTO.Request request);

    public OrderDTO.Response insertOrder(OrderDTO.Request request);

    public OrderDTO.Response updateOrder(OrderDTO.Request request);

    public OrderDTO.Response deleteOrder(OrderDTO.Request request);
}
