package com.projectweb.service.admin;

import com.projectweb.model.OgnAdmin;
import org.springframework.data.domain.Page;

import java.util.List;


public interface AdminService {
    Page<OgnAdmin> findAll(String search , int pageNumber);

    OgnAdmin findById(long id);

    List<OgnAdmin> findFirstByUserid(String userid);

    //CRUD Admin
    OgnAdmin insertAdmin(OgnAdmin admin);
    OgnAdmin updateAdmin(OgnAdmin admin);
    boolean deleteAdmin(long id);
    //CRUD Admin

    boolean validatePermission(String... permission);
}
