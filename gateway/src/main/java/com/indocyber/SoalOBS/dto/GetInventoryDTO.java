package com.indocyber.SoalOBS.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class GetInventoryDTO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        private Integer id;
        @JsonProperty("item_id")
        private Integer itemId;
        private Integer qty;
        private String type;
        private Integer page;
        @JsonProperty("max_row")
        private Integer maxRow;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response{
        private Boolean error;
        private String message;
        private DataResp result;
        private List<DataResp> listResult;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataResp{
        private Integer id;
        @JsonProperty("item_id")
        private Integer itemId;
        private Integer qty;
        private String type;
        private Integer stock;
    }
}
