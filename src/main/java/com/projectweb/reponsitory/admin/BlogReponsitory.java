package com.projectweb.reponsitory.admin;


import com.projectweb.model.OgnBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BlogReponsitory extends JpaRepository<OgnBlog, String> , JpaSpecificationExecutor<OgnBlog> {
    Optional<OgnBlog> findFirstById(Long id);
}
