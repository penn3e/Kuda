package com.crm.kuda.repository;

import com.crm.kuda.entity.ClientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientCategoryRepository extends JpaRepository<ClientCategory, Long> {
    Optional<ClientCategory> findByCode(String code);
    boolean existsByCode(String code);
    
    @Query("SELECT c FROM ClientCategory c WHERE c.famille = :famille")
    List<ClientCategory> findByFamille(@Param("famille") String famille);
    
    @Query("SELECT DISTINCT c.famille FROM ClientCategory c")
    List<String> findDistinctFamilles();
}
