package com.indocyber.SoalOBS.service.impl;

import com.indocyber.SoalOBS.dto.GetItemDTO;
import com.indocyber.SoalOBS.dto.GetInventoryDTO;
import com.indocyber.SoalOBS.dto.OrderDTO;
import com.indocyber.SoalOBS.entity.Item;
import com.indocyber.SoalOBS.entity.Order;
import com.indocyber.SoalOBS.function.GetItemById;
import com.indocyber.SoalOBS.function.GetItemStock;
import com.indocyber.SoalOBS.function.InsertInventory;
import com.indocyber.SoalOBS.repositories.OrderRespository;
import com.indocyber.SoalOBS.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRespository orderRespository;

    @Autowired
    GetItemStock getItemStock;

    @Autowired
    GetItemById getItemById;

    @Autowired
    InsertInventory insertInventory;

    @Override
    public OrderDTO.Response getOrderById(OrderDTO.Request request) {
        Optional<Order> dbResult = orderRespository.findById(request.getOrderNo());
        if(dbResult.isEmpty()){
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("No data found")
                    .build();
        }
        return OrderDTO.Response.builder()
                .error(false)
                .result(OrderDTO.DataResp.builder()
                        .orderNo(dbResult.get().getOrderNo())
                        .itemId(dbResult.get().getItem().getId())
                        .price(dbResult.get().getPrice())
                        .qty(dbResult.get().getQty()).build())
                .build();
    }

    @Override
    public OrderDTO.Response getOrderByQty(OrderDTO.Request request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getMaxRow());
        log.info("Request masuk : " + request);
        Page<Order> dbResult = orderRespository.findByQty(request.getQty(), pageable);
        log.info("DB Result : " + dbResult);
        if(dbResult.isEmpty()){
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("No data found")
                    .build();
        }
        List<OrderDTO.DataResp> listResult = new ArrayList<>();
        for (Order or: dbResult) {
            OrderDTO.DataResp temp = new OrderDTO.DataResp();
            temp.setOrderNo(or.getOrderNo());
            temp.setQty(or.getQty());
            temp.setPrice(or.getPrice());
            temp.setItemId(or.getItem().getId());
            listResult.add(temp);
        }
        return OrderDTO.Response.builder().error(false).listResult(listResult).build();
    }

    @Override
    public OrderDTO.Response getOrder(OrderDTO.Request request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getMaxRow());
        log.info("Request masuk ke getOrder : " + request);
        Page<Order> dbResult = orderRespository.getAllData(pageable);
        log.info("DB Result : " + dbResult);
        if(dbResult.isEmpty()){
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("No data found")
                    .build();
        }
        List<OrderDTO.DataResp> listResult = new ArrayList<>();
        for (Order or: dbResult) {
            OrderDTO.DataResp temp = new OrderDTO.DataResp();
            temp.setOrderNo(or.getOrderNo());
            temp.setQty(or.getQty());
            temp.setPrice(or.getPrice());
            temp.setItemId(or.getItem().getId());
            listResult.add(temp);
        }
        return OrderDTO.Response.builder().error(false).listResult(listResult).build();
    }

    @Override
    public OrderDTO.Response insertOrder(OrderDTO.Request request) {
        try {
            GetInventoryDTO.Response getItemStockResp = getItemStock.getItemById(request.getItemId());
            log.info(getItemStockResp.toString());
            if(getItemStockResp.getError()){
                return OrderDTO.Response.builder()
                        .error(true)
                        .message("Invalid Item")
                        .build();
            }
            if(getItemStockResp.getResult().getStock() < 1){
                return OrderDTO.Response.builder()
                        .error(true)
                        .message("Item stock is 0").build();
            }
            if((getItemStockResp.getResult().getStock() - request.getQty()) < 1){
                return OrderDTO.Response.builder()
                        .error(true)
                        .message("Item stock is not enough").build();
            }
            GetItemDTO.Response getItemByIdResp = getItemById.getItemById(request.getItemId());
            log.info(getItemByIdResp.toString());
            String lastOrderNo = orderRespository.getlastData();
            Integer num = Integer.valueOf(lastOrderNo.substring(1));
            String newOrderNo = "O" + String.valueOf(num+1);
            GetInventoryDTO.Response insertInventoryResp = insertInventory.insertInventory(request.getItemId(), request.getQty(), "W");
            log.info("Insert Inventory Resp : " + insertInventoryResp);
            if(insertInventoryResp.getError()){
                return OrderDTO.Response.builder()
                        .error(true)
                        .message("Failed to insert new Inventory").build();
            }
            orderRespository.save(Order.builder()
                    .orderNo(newOrderNo)
                    .item(Item.builder()
                            .id(request.getItemId())
                            .name(getItemByIdResp.getResult().getName())
                            .price(getItemByIdResp.getResult().getPrice())
                            .build())
                    .price(getItemByIdResp.getResult().getPrice())
                    .qty(request.getQty())
                    .build());
            return OrderDTO.Response.builder()
                    .error(false)
                    .message("Successfully insert new Order")
                    .result(OrderDTO.DataResp.builder()
                            .qty(request.getQty())
                            .price(getItemByIdResp.getResult().getPrice())
                            .itemId(request.getItemId())
                            .orderNo(newOrderNo)
                            .build())
                    .build();
        }catch (Exception e){
            log.info(e.getMessage());
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("Failed to insert new Order").build();
        }
    }

    @Override
    public OrderDTO.Response updateOrder(OrderDTO.Request request) {
        log.info("Masuk ke service update impl : " + request);
        try {
            Optional<Order> dbResult = orderRespository.findById(request.getOrderNo());
            if(dbResult.isEmpty()){
                return OrderDTO.Response.builder()
                        .error(true)
                        .message("No data found")
                        .build();
            }
            // todo return previous stock item (inside if statement)
            if(!request.getItemId().equals(dbResult.get().getItem().getId())){
                GetInventoryDTO.Response insertInventoryResp = insertInventory.insertInventory(dbResult.get().getItem().getId(), request.getQty(), "T");
                if(insertInventoryResp.getError()){
                    return OrderDTO.Response.builder()
                            .error(true)
                            .message("Failed to update previous Inventory").build();
                }
            }
            // todo update item stock if its same item
            GetInventoryDTO.Response getItemStockResp = getItemStock.getItemById(request.getItemId());
            log.info(getItemStockResp.toString());
            if(getItemStockResp.getError()){
                return OrderDTO.Response.builder()
                        .error(true)
                        .message("Invalid Item")
                        .build();
            }
            if(getItemStockResp.getResult().getStock() < 1){
                return OrderDTO.Response.builder()
                        .error(true)
                        .message("Item stock is 0").build();
            }
            if((getItemStockResp.getResult().getStock() - request.getQty()) < 1){
                return OrderDTO.Response.builder()
                        .error(true)
                        .message("Item stock is not enough").build();
            }
            GetItemDTO.Response getItemByIdResp = getItemById.getItemById(request.getItemId());
            log.info(getItemByIdResp.toString());
            GetInventoryDTO.Response insertInventoryResp = insertInventory.insertInventory(request.getItemId(), request.getQty(), "W");
            log.info("Insert Inventory Resp : " + insertInventoryResp);
            if(insertInventoryResp.getError()){
                return OrderDTO.Response.builder()
                        .error(true)
                        .message("Failed to insert new Inventory").build();
            }
            orderRespository.save(Order.builder()
                    .orderNo(request.getOrderNo())
                    .item(Item.builder()
                            .id(request.getItemId())
                            .name(getItemByIdResp.getResult().getName())
                            .price(getItemByIdResp.getResult().getPrice())
                            .build())
                    .price(getItemByIdResp.getResult().getPrice())
                    .qty(request.getQty())
                    .build());
            return OrderDTO.Response.builder()
                    .error(false)
                    .message("Data Updated")
                    .result(OrderDTO.DataResp.builder()
                            .qty(request.getQty())
                            .price(getItemByIdResp.getResult().getPrice())
                            .itemId(request.getItemId())
                            .orderNo(request.getOrderNo())
                            .build())
                    .build();
        }catch (Exception e){
            log.info(e.getMessage());
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("Failed to update Order").build();
        }
    }

    @Override
    public OrderDTO.Response deleteOrder(OrderDTO.Request request) {
        log.info("Masuk ke service delete impl : " + request);
        try{
            Optional<Order> dbResult = orderRespository.findById(request.getOrderNo());
            if(dbResult.isEmpty()){
                return OrderDTO.Response.builder()
                        .error(true)
                        .message("No data found")
                        .build();
            }
            orderRespository.deleteById(request.getOrderNo());
            return OrderDTO.Response.builder()
                    .error(false)
                    .message("Successfully delete Order")
                    .result(OrderDTO.DataResp.builder()
                            .orderNo(request.getOrderNo())
                            .itemId(dbResult.get().getItem().getId())
                            .qty(dbResult.get().getQty())
                            .price(dbResult.get().getPrice())
                            .build())
                    .build();
        }catch (Exception e){
            log.info(e.getMessage());
            return OrderDTO.Response.builder()
                    .error(true)
                    .message("Failed to delete Order").build();
        }
    }
}
