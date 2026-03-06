package com.indocyber.SoalOBS.service;

import com.indocyber.SoalOBS.dto.GetItemDTO;

public interface ItemService {

    public GetItemDTO.Response getItemById(GetItemDTO.Request request);

    public GetItemDTO.Response getItemByPrice(GetItemDTO.Request request);

    public GetItemDTO.Response getAllData(GetItemDTO.Request request);

    public GetItemDTO.Response insertItem(GetItemDTO.Request request);

    public GetItemDTO.Response deleteItem(GetItemDTO.Request request);

    public GetItemDTO.Response updateItem(GetItemDTO.Request request);
}
