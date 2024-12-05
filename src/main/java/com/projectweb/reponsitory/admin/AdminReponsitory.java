package com.projectweb.reponsitory.admin;

import com.projectweb.model.OgnAdmin;
import com.projectweb.model.OgnUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AdminReponsitory extends JpaRepository<OgnAdmin, String> , JpaSpecificationExecutor<OgnAdmin> {
    Optional<OgnAdmin> findFirstById(Long id);

    // Thêm phương thức tìm tất cả các Admin theo userid
    List<OgnAdmin> findByUserid(OgnUser userid);

}
