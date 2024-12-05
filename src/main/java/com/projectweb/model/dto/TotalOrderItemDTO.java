package com.projectweb.model.dto;

import java.math.BigDecimal;

public class TotalOrderItemDTO {

    private long id;
    private Long quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
