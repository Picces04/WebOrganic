package com.projectweb.reponsitory.token;


import com.projectweb.model.OgnUser;
import com.projectweb.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TokenRepo extends JpaRepository<PasswordResetToken, Long> , JpaSpecificationExecutor<PasswordResetToken> {
    Optional<PasswordResetToken> findByToken(String token);
}
