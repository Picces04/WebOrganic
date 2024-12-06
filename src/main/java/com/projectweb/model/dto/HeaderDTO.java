package com.projectweb.model.dto;

import java.math.BigDecimal;

public class HeaderDTO {
    private long totalProduct;
    private BigDecimal sumPrice;

    public long getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(long totalProduct) {
        this.totalProduct = totalProduct;
    }

    public BigDecimal getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(BigDecimal sumPrice) {
        this.sumPrice = sumPrice;
    }

    public void clear() {
        this.totalProduct = 0;   // Đặt lại totalProduct về 0
        this.sumPrice = BigDecimal.valueOf(0);    // Đặt lại sumPrice về null
    }
}
