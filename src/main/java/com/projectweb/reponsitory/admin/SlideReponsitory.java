package com.projectweb.reponsitory.admin;


import com.projectweb.model.OgnSlide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SlideReponsitory extends JpaRepository<OgnSlide, String> {
    Optional<OgnSlide> findFirstById(Long id);

    @Query("SELECT u FROM OgnSlide u WHERE u.status LIKE %:status%")
    List<OgnSlide> findStatus(@Param("status") String status);
}
