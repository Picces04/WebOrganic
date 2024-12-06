package com.projectweb.service.admin;

import com.projectweb.model.OgnUser;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {
    OgnUser findByEmail(String email);
    OgnUser findById(String userid) throws UsernameNotFoundException;
    Page<OgnUser> findAll(String search ,int pageNumber);
    List<OgnUser> findAll();

    Integer TotalUsers();

    //CRUD User
    OgnUser insertUser(OgnUser user);
    OgnUser updateUser(OgnUser user);
    //OgnUser deleteUser(OgnUser user);
    boolean deleteUser(String id);

    boolean existsByEmail(String email);
    //CRUD User



}
