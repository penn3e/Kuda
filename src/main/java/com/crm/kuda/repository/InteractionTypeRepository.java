package com.crm.kuda.repository;

import com.crm.kuda.entity.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InteractionTypeRepository extends JpaRepository<InteractionType, Long> {
    Optional<InteractionType> findByCode(String code);
    boolean existsByCode(String code);
}
