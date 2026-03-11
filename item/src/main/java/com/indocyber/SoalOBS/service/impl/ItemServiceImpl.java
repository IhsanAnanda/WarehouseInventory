package com.indocyber.SoalOBS.service.impl;

import com.indocyber.SoalOBS.dto.GetItemDTO;
import com.indocyber.SoalOBS.entity.Item;
import com.indocyber.SoalOBS.repositories.ItemRepository;
import com.indocyber.SoalOBS.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public GetItemDTO.Response getItemById(GetItemDTO.Request request) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for(HashMap.Entry<String, Integer> entry : hashMap.entrySet()){
            System.out.println();
        }
        Optional<Item> dbResult = itemRepository.findById(request.getId());
        if(dbResult.isEmpty()){
            return GetItemDTO.Response.builder()
                    .error(true)
                    .message("No data found").build();
        }
        return GetItemDTO.Response.builder().error(false).result(
                GetItemDTO.DataResp.builder()
                        .id(dbResult.get().getId())
                        .name(dbResult.get().getName())
                        .price(dbResult.get().getPrice()).build()
        ).build();
    }

    @Override
    public GetItemDTO.Response getItemByPrice(GetItemDTO.Request request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getMaxRow());
        Page<Item> dbResult = itemRepository.findByPrice(request.getPrice(), pageable);
        if(dbResult.isEmpty()){
            return GetItemDTO.  Response.builder()
                    .error(true)
                    .message("No data found").build();
        }
        List<GetItemDTO.DataResp> listResult = new ArrayList<>();
        for (Item it: dbResult) {
            GetItemDTO.DataResp temp = new GetItemDTO.DataResp();
            temp.setId(it.getId());
            temp.setName(it.getName());
            temp.setPrice(it.getPrice());
            listResult.add(temp);
        }
        return GetItemDTO.Response.builder().error(false).listResult(listResult).build();
    }

    @Override
    @Cacheable(value = "items", key = "#request.page + '-' + #request.maxRow")
    public GetItemDTO.Response getAllData(GetItemDTO.Request request) {
        Pageable pageable = PageRequest.of(request.getPage() != null ? request.getPage() : 0, request.getMaxRow() != null ? request.getMaxRow() : 10);
        Page<Item> dbResult = itemRepository.getAllData(pageable);
        if(dbResult.isEmpty()){
            return GetItemDTO.  Response.builder()
                    .error(true)
                    .message("No data found").build();
        }
        List<GetItemDTO.DataResp> listResult = new ArrayList<>();
        for (Item it: dbResult) {
            GetItemDTO.DataResp temp = new GetItemDTO.DataResp();
            temp.setId(it.getId());
            temp.setName(it.getName());
            temp.setPrice(it.getPrice());
            listResult.add(temp);
        }
        return GetItemDTO.Response.builder().error(false).listResult(listResult).build();
    }

    // @Override
    // public GetItemDTO.Response getAllData(GetItemDTO.Request request) {
    //     Pageable pageable = PageRequest.of(request.getPage(), request.getMaxRow());
    //     Page<Item> dbResult = itemRepository.getAllData(pageable);
    //     if(dbResult.isEmpty()){
    //         return GetItemDTO.  Response.builder()
    //                 .error(true)
    //                 .message("No data found").build();
    //     }
    //     List<GetItemDTO.DataResp> listResult = new ArrayList<>();
    //     for (Item it: dbResult) {
    //         GetItemDTO.DataResp temp = new GetItemDTO.DataResp();
    //         temp.setId(it.getId());
    //         temp.setName(it.getName());
    //         temp.setPrice(it.getPrice());
    //         listResult.add(temp);
    //     }
    //     return GetItemDTO.Response.builder().error(false).listResult(listResult).build();
    // }

    @Override
    public GetItemDTO.Response insertItem(GetItemDTO.Request request) {
        try{
            itemRepository.save(Item.builder()
                    .name(request.getName())
                    .price(request.getPrice())
                    .build());
            return GetItemDTO.Response.builder()
                    .error(false)
                    .message("Successfully insert new Item")
                    .result(GetItemDTO.DataResp.builder()
                            .price(request.getPrice())
                            .name(request.getName())
                            .build())
                    .build();
        }catch (Exception e){
            log.info(e.getMessage());
            return GetItemDTO.Response.builder()
                    .error(true)
                    .message("Failed to insert new Item").build();
        }
    }

    @Override
    public GetItemDTO.Response deleteItem(GetItemDTO.Request request) {
        try{
            GetItemDTO.Response dbResult = this.getItemById(request);
            itemRepository.deleteById(request.getId());
            return GetItemDTO.Response.builder()
                    .error(false)
                    .message("Successfully delete Item")
                    .result(GetItemDTO.DataResp.builder()
                            .id(request.getId())
                            .price(dbResult.getResult().getPrice())
                            .name(dbResult.getResult().getName())
                            .build())
                    .build();
        }catch (Exception e){
            log.info(e.getMessage());
            return GetItemDTO.Response.builder()
                    .error(true)
                    .message("Failed to delete Item").build();
        }
    }

    @Override
    public GetItemDTO.Response updateItem(GetItemDTO.Request request) {
        try{
            log.info("Request masuk update item : " + request);
            Optional<Item> dbResult = itemRepository.findById(request.getId());
            if(dbResult.isEmpty()){
                return GetItemDTO.Response.builder()
                        .error(true)
                        .message("No data found").build();
            }
            dbResult.get().setName(request.getName());
            dbResult.get().setPrice(request.getPrice());
            itemRepository.save(dbResult.get());
            return GetItemDTO.Response.builder()
                    .error(false)
                    .message("Data updated")
                    .result(GetItemDTO.DataResp.builder()
                            .id(request.getId())
                            .price(request.getPrice())
                            .name(request.getName())
                            .build())
                    .build();
        }catch (Exception e){
            log.info(e.getMessage());
            return GetItemDTO.Response.builder()
                    .error(true)
                    .message("Failed to update Item data").build();
        }
    }

}
