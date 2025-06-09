package com.crm.kuda.repository;

import com.crm.kuda.entity.CrmMission;
import com.crm.kuda.entity.CrmMissionClient;
import com.crm.kuda.entity.KudaClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrmMissionClientRepository extends JpaRepository<CrmMissionClient, Long> {
    
    List<CrmMissionClient> findByMission(CrmMission mission);
    
    List<CrmMissionClient> findByClient(KudaClient client);
    
    Optional<CrmMissionClient> findByMissionAndClient(CrmMission mission, KudaClient client);
    
    @Query("SELECT mc FROM CrmMissionClient mc WHERE mc.mission.id = :missionId")
    List<CrmMissionClient> findByMissionId(@Param("missionId") Long missionId);
    
    @Query("SELECT mc FROM CrmMissionClient mc WHERE mc.client.id = :clientId")
    List<CrmMissionClient> findByClientId(@Param("clientId") Long clientId);
    
    boolean existsByMissionAndClient(CrmMission mission, KudaClient client);
    
    void deleteByMissionAndClient(CrmMission mission, KudaClient client);
}
