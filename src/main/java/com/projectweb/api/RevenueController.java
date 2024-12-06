package com.projectweb.api;

import com.projectweb.model.dto.RevenueDto;
import com.projectweb.service.admin.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Biểu đồ
@RestController
@RequestMapping("/api/revenue")
public class RevenueController {

    @Autowired
    private BillService billService;

    @GetMapping("/monthly")
    public List<RevenueDto> getMonthlyRevenue() {
        return billService.getMonthlyRevenue();
    }
}
