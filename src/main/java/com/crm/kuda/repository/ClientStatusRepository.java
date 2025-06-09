package com.crm.kuda.repository;

import com.crm.kuda.entity.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientStatusRepository extends JpaRepository<ClientStatus, Long> {
    Optional<ClientStatus> findByCode(String code);
    boolean existsByCode(String code);
}
