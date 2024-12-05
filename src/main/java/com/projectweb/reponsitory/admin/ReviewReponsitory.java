package com.projectweb.reponsitory.admin;

import com.projectweb.model.OgnProduct;
import com.projectweb.model.OgnStarrating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ReviewReponsitory extends JpaRepository<OgnStarrating, String> , JpaSpecificationExecutor<OgnStarrating> {
    Optional<OgnStarrating> findFirstById(Long id);

    @Query("SELECT u FROM OgnStarrating u WHERE u.status LIKE %:status%")
    List<OgnStarrating> findStatus(@Param("status") String status);

    List<OgnStarrating> findByProductid(OgnProduct productid);
}
