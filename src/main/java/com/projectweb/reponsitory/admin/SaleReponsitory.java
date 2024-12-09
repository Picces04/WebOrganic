package com.projectweb.reponsitory.admin;

import com.projectweb.model.OgnSaleprice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SaleReponsitory extends JpaRepository<OgnSaleprice, String>, JpaSpecificationExecutor<OgnSaleprice> {
    Optional<OgnSaleprice> findFirstById(Long id);

    // Phương thức truy vấn tất cả các sale có ngày hết hạn là ngày mai
    @Query("SELECT s FROM OgnSaleprice s WHERE s.enddate = :tomorrow")
    List<OgnSaleprice> findSaleTheDay(LocalDate tomorrow);

}
