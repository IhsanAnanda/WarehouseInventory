package com.indocyber.SoalOBS.service;

import com.indocyber.SoalOBS.dto.GetInventoryDTO;

public interface InventoryService {
    public GetInventoryDTO.Response getInventoryById(GetInventoryDTO.Request request);
    public GetInventoryDTO.Response getInventoryByType(GetInventoryDTO.Request request);
    public GetInventoryDTO.Response getAllData(GetInventoryDTO.Request request);
    public GetInventoryDTO.Response insertInventory(GetInventoryDTO.Request request);
    public GetInventoryDTO.Response deleteInventory(GetInventoryDTO.Request request);
    public GetInventoryDTO.Response updateInventory(GetInventoryDTO.Request request);
    public GetInventoryDTO.Response getItemStock(GetInventoryDTO.Request request);
}
