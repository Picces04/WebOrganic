package com.projectweb.reponsitory.admin;

import com.projectweb.model.OgnCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryReponsitory extends JpaRepository<OgnCategory, String> {
    Optional<OgnCategory> findFirstById(Long id);

    @Query("SELECT u FROM OgnCategory u WHERE u.status LIKE %:status%")
    List<OgnCategory> findStatus(@Param("status") String status);
}
