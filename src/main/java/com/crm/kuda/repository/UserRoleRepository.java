package com.crm.kuda.repository;

import com.crm.kuda.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByCode(String code);
    boolean existsByCode(String code);
}
