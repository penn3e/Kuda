package com.crm.kuda.repository;

import com.crm.kuda.entity.CrmInteraction;
import com.crm.kuda.entity.InteractionType;
import com.crm.kuda.entity.KudaClient;
import com.crm.kuda.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CrmInteractionRepository extends JpaRepository<CrmInteraction, Long> {
    
    List<CrmInteraction> findByClient(KudaClient client);
    
    List<CrmInteraction> findByClientOrderByDateInteractionDesc(KudaClient client);
    
    List<CrmInteraction> findByType(InteractionType type);
    
    List<CrmInteraction> findByCreatedBy(User createdBy);
    
    @Query("SELECT i FROM CrmInteraction i WHERE i.client.id = :clientId AND i.dateInteraction >= :dateDebut AND i.dateInteraction <= :dateFin")
    List<CrmInteraction> findByClientAndDateRange(@Param("clientId") Long clientId, @Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);
    
    @Query("SELECT i FROM CrmInteraction i WHERE i.dateInteraction >= :dateDebut AND i.dateInteraction <= :dateFin")
    Page<CrmInteraction> findByDateRange(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin, Pageable pageable);
    
    @Query("SELECT i FROM CrmInteraction i WHERE i.client.id = :clientId ORDER BY i.dateInteraction DESC")
    Page<CrmInteraction> findByClientIdOrderByDateDesc(@Param("clientId") Long clientId, Pageable pageable);
    
    long countByClient(KudaClient client);
    
    long countByType(InteractionType type);
}
