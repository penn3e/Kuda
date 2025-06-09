package com.crm.kuda.repository;

import com.crm.kuda.entity.ClientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientTypeRepository extends JpaRepository<ClientType, Long> {
    Optional<ClientType> findByCode(String code);
    boolean existsByCode(String code);
}
