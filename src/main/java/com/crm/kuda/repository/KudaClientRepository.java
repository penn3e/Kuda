package com.crm.kuda.repository;

import com.crm.kuda.entity.ClientStatus;
import com.crm.kuda.entity.ClientType;
import com.crm.kuda.entity.KudaClient;
import com.crm.kuda.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KudaClientRepository extends JpaRepository<KudaClient, Long> {
    
    List<KudaClient> findByType(ClientType type);
    
    List<KudaClient> findByStatut(ClientStatus statut);
    
    List<KudaClient> findByCreatedBy(User createdBy);
    
    Optional<KudaClient> findByEmail(String email);
    
    Optional<KudaClient> findByNumeroRc(String numeroRc);
    
    @Query("SELECT c FROM KudaClient c WHERE c.raisonSociale LIKE %:search% OR c.nomContact LIKE %:search% OR c.email LIKE %:search%")
    Page<KudaClient> findBySearchTerm(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT c FROM KudaClient c WHERE c.type = :type AND c.statut = :statut")
    List<KudaClient> findByTypeAndStatut(@Param("type") ClientType type, @Param("statut") ClientStatus statut);
    
    @Query("SELECT c FROM KudaClient c WHERE c.wilaya = :wilaya")
    List<KudaClient> findByWilaya(@Param("wilaya") String wilaya);
    
    long countByType(ClientType type);
    
    long countByStatut(ClientStatus statut);
    
    boolean existsByEmail(String email);
    
    boolean existsByNumeroRc(String numeroRc);
}
