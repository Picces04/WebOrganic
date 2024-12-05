package com.projectweb.reponsitory.admin;


import com.projectweb.model.OgnUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserReponsitory extends JpaRepository<OgnUser, String> , JpaSpecificationExecutor<OgnUser> {
    OgnUser findFirstByEmail(String email);
    Optional<OgnUser> findByEmail(String email);
    Optional<OgnUser> findFirstByUserid(String userid);


}
