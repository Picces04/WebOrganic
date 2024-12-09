package com.projectweb.reponsitory.home;


import com.projectweb.model.OgnBlog;
import com.projectweb.model.OgnProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogHomeReponsitory extends JpaRepository<OgnBlog, String> , JpaSpecificationExecutor<OgnBlog> {
    OgnBlog findById(Long id);
}
