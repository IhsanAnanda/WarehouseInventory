package com.indocyber.SoalOBS.service.impl;

import com.indocyber.SoalOBS.dto.GetInventoryDTO;
import com.indocyber.SoalOBS.dto.GetItemDTO;
import com.indocyber.SoalOBS.entity.Inventory;
import com.indocyber.SoalOBS.entity.Item;
import com.indocyber.SoalOBS.function.GetItemById;
import com.indocyber.SoalOBS.repositories.InventoryRepository;
import com.indocyber.SoalOBS.service.InventoryService;
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
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    GetItemById getItemById;

    @Override
    public GetInventoryDTO.Response getInventoryById(GetInventoryDTO.Request request) {
        Optional<Inventory> dbResult =inventoryRepository.findById(request.getId());
        if(dbResult.isEmpty()){
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("No data found").build();
        }
        return GetInventoryDTO.Response.builder().error(false).result(
                GetInventoryDTO.DataResp.builder()
                        .id(dbResult.get().getId())
                        .itemId(dbResult.get().getItem().getId())
                        .type(dbResult.get().getType())
                        .qty(dbResult.get().getQty())
                        .build()
        ).build();
    }

    @Override
    public GetInventoryDTO.Response getInventoryByType(GetInventoryDTO.Request request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getMaxRow());
        log.info("Request masuk : " + request);
        Page<Inventory> dbResult = inventoryRepository.findByType(request.getType(), pageable);
        log.info("DB Result : " + dbResult);
        if(dbResult.isEmpty()){
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("No data found")
                    .build();
        }
        List<GetInventoryDTO.DataResp> listResult = new ArrayList<>();
        for (Inventory in: dbResult) {
            GetInventoryDTO.DataResp temp = new GetInventoryDTO.DataResp();
            temp.setId(in.getId());
            temp.setQty(in.getQty());
            temp.setItemId(in.getItem().getId());
            temp.setType(in.getType());
            listResult.add(temp);
        }
        return GetInventoryDTO.Response.builder().error(false).listResult(listResult).build();
    }

    @Override
    public GetInventoryDTO.Response getAllData(GetInventoryDTO.Request request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getMaxRow());
        log.info("Request masuk : " + request);
        Page<Inventory> dbResult = inventoryRepository.getAllData(pageable);
        log.info("DB Result : " + dbResult);
        if(dbResult.isEmpty()){
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("No data found")
                    .build();
        }
        List<GetInventoryDTO.DataResp> listResult = new ArrayList<>();
        for (Inventory in: dbResult) {
            GetInventoryDTO.DataResp temp = new GetInventoryDTO.DataResp();
            temp.setId(in.getId());
            temp.setQty(in.getQty());
            temp.setItemId(in.getItem().getId());
            temp.setType(in.getType());
            listResult.add(temp);
        }
        return GetInventoryDTO.Response.builder().error(false).listResult(listResult).build();
    }

    @Override
    public GetInventoryDTO.Response insertInventory(GetInventoryDTO.Request request) {
        log.info("Masuk ke service impl : " + request);
        try{
            if(!request.getType().equals("T") && !request.getType().equals("W")){
                return GetInventoryDTO.Response.builder()
                        .error(true)
                        .message("Type is not T or W")
                        .build();
            }
            GetItemDTO.Response getItemResp = getItemById.getItemById(request.getItemId());
            inventoryRepository.save(Inventory.builder()
                    .item(Item.builder()
                            .id(request.getItemId())
                            .price(getItemResp.getResult().getPrice())
                            .name(getItemResp.getResult().getName())
                            .build())
                    .qty(request.getQty())
                    .type(request.getType())
                    .build());
            log.info("Berhasil insert inventory");
            return GetInventoryDTO.Response.builder()
                    .error(false)
                    .message("Successfully insert new Inventory")
                    .result(GetInventoryDTO.DataResp.builder()
                            .qty(request.getQty())
                            .type(request.getType())
                            .itemId(request.getItemId())
                            .build())
                    .build();
        }catch (Exception e){
            log.info(e.getMessage());
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("Failed to insert new Inventory").build();
        }
    }

    @Override
    public GetInventoryDTO.Response deleteInventory(GetInventoryDTO.Request request) {
        log.info("Masuk ke service impl : " + request);
        try{
            Optional<Inventory> dbResult = inventoryRepository.findById(request.getId());
            if(dbResult.isEmpty()){
                return GetInventoryDTO.Response.builder()
                        .error(true)
                        .message("No Inventory with ID " + request.getId() + " found").build();
            }
            inventoryRepository.deleteById(request.getId());
            return GetInventoryDTO.Response.builder()
                    .error(false)
                    .message("Successfully delete Inventory")
                    .result(GetInventoryDTO.DataResp.builder()
                            .qty(dbResult.get().getQty())
                            .type(dbResult.get().getType())
                            .itemId(dbResult.get().getItem().getId())
                            .build())
                    .build();
        }catch (Exception e){
            log.info(e.getMessage());
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("Failed to delete Inventory").build();
        }
    }

    @Override
    public GetInventoryDTO.Response updateInventory(GetInventoryDTO.Request request) {
        log.info("Masuk ke service impl : " + request);
        try{
            if(!request.getType().equals("T") && !request.equals("W")){
                return GetInventoryDTO.Response.builder()
                        .error(true)
                        .message("Type is not T or W")
                        .build();
            }
            GetItemDTO.Response getItemResp = getItemById.getItemById(request.getItemId());
            if(getItemResp.getError()){
                return GetInventoryDTO.Response.builder()
                        .error(true)
                        .message("No Item found with item_id " + request.getItemId())
                        .build();
            }
            Optional<Inventory> dbResult = inventoryRepository.findById(request.getId());
            if(dbResult.isEmpty()){
                return GetInventoryDTO.Response.builder()
                        .error(true)
                        .message("No Inventory found with id " + request.getId())
                        .build();
            }
            dbResult.get().setQty(request.getQty());
            dbResult.get().setType(request.getType());
            dbResult.get().setItem(Item.builder()
                    .id(getItemResp.getResult().getId())
                    .name(getItemResp.getResult().getName())
                    .price(getItemResp.getResult().getPrice())
                    .build());
            inventoryRepository.save(dbResult.get());
            return GetInventoryDTO.Response.builder()
                    .error(false)
                    .message("Data updated")
                    .result(GetInventoryDTO.DataResp.builder()
                            .qty(request.getQty())
                            .type(request.getType())
                            .itemId(request.getItemId())
                            .build())
                    .build();
        }catch (Exception e){
            log.info(e.getMessage());
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("Failed to update Inventory").build();
        }
    }

    @Override
    public GetInventoryDTO.Response getItemStock(GetInventoryDTO.Request request) {
        GetItemDTO.Response getItemResp = getItemById.getItemById(request.getItemId());
        log.info("getItemStock : " + getItemResp.toString());
        if(getItemResp.getError()){
            return GetInventoryDTO.Response.builder()
                    .error(true)
                    .message("No Item found with item_id " + request.getItemId())
                    .build();
        }
        Integer stock = inventoryRepository.getItemStock(request.getItemId());
        return GetInventoryDTO.Response.builder()
                .error(false)
                .message("Item found")
                .result(GetInventoryDTO.DataResp.builder()
                        .itemId(request.getItemId())
                        .stock(stock)
                        .build())
                .build();
    }
}
