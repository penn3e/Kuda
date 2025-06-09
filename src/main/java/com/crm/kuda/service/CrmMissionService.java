package com.crm.kuda.service;

import com.crm.kuda.entity.*;
import com.crm.kuda.repository.CrmMissionClientRepository;
import com.crm.kuda.repository.CrmMissionRepository;
import com.crm.kuda.repository.KudaClientRepository;
import com.crm.kuda.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CrmMissionService {

    @Autowired
    private CrmMissionRepository missionRepository;

    @Autowired
    private CrmMissionClientRepository missionClientRepository;

    @Autowired
    private KudaClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<CrmMission> getAllMissions(Pageable pageable) {
        return missionRepository.findAll(pageable);
    }

    public Optional<CrmMission> getMissionById(Long id) {
        return missionRepository.findById(id);
    }

    public CrmMission saveMission(CrmMission mission) {
        if (mission.getId() == null) {
            mission.setCreatedAt(new Date());
        }
        return missionRepository.save(mission);
    }

    public void deleteMission(Long id) {
        missionRepository.deleteById(id);
    }

    public List<CrmMission> getMissionsByStatus(MissionStatusEnum status) {
        return missionRepository.findByStatut(status);
    }

    public List<CrmMission> getMissionsByAssignedUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(missionRepository::findByAssignedTo).orElse(List.of());
    }

    public List<CrmMission> getMissionsByCreatedUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(missionRepository::findByCreatedBy).orElse(List.of());
    }

    public List<CrmMission> getMissionsByClientId(Long clientId) {
        return missionRepository.findByClientId(clientId);
    }

    public List<CrmMission> getMissionsByDateRange(Date dateDebut, Date dateFin) {
        return missionRepository.findByDateRange(dateDebut, dateFin);
    }

    public Optional<CrmMission> updateMissionStatus(Long id, MissionStatusEnum status) {
        Optional<CrmMission> missionOpt = missionRepository.findById(id);
        if (missionOpt.isPresent()) {
            CrmMission mission = missionOpt.get();
            mission.setStatut(status);
            return Optional.of(missionRepository.save(mission));
        }
        return Optional.empty();
    }

    public boolean addClientToMission(Long missionId, Long clientId) {
        Optional<CrmMission> missionOpt = missionRepository.findById(missionId);
        Optional<KudaClient> clientOpt = clientRepository.findById(clientId);

        if (missionOpt.isPresent() && clientOpt.isPresent()) {
            CrmMission mission = missionOpt.get();
            KudaClient client = clientOpt.get();

            // Vérifier si la relation existe déjà
            if (!missionClientRepository.existsByMissionAndClient(mission, client)) {
                CrmMissionClient missionClient = new CrmMissionClient();
                missionClient.setMission(mission);
                missionClient.setClient(client);
                missionClientRepository.save(missionClient);
                return true;
            }
        }
        return false;
    }

    public boolean removeClientFromMission(Long missionId, Long clientId) {
        Optional<CrmMission> missionOpt = missionRepository.findById(missionId);
        Optional<KudaClient> clientOpt = clientRepository.findById(clientId);

        if (missionOpt.isPresent() && clientOpt.isPresent()) {
            CrmMission mission = missionOpt.get();
            KudaClient client = clientOpt.get();

            if (missionClientRepository.existsByMissionAndClient(mission, client)) {
                missionClientRepository.deleteByMissionAndClient(mission, client);
                return true;
            }
        }
        return false;
    }

    public long getMissionCountByStatus(MissionStatusEnum status) {
        return missionRepository.countByStatut(status);
    }

    public long getMissionCountByAssignedUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(u -> missionRepository.countByAssignedToAndStatut(u, MissionStatusEnum.EN_COURS))
                  .orElse(0L);
    }
}
