package com.crm.kuda.repository;

import com.crm.kuda.entity.ERole;
import com.crm.kuda.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
    Boolean existsByName(ERole name);
}
