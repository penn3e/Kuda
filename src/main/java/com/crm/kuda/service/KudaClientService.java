package com.crm.kuda.service;

import com.crm.kuda.entity.KudaClient;
import com.crm.kuda.entity.User;
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
public class KudaClientService {

    @Autowired
    private KudaClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<KudaClient> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Optional<KudaClient> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public KudaClient saveClient(KudaClient client) {
        if (client.getId() == null) {
            client.setDateCreation(new Date());
        }
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public Page<KudaClient> searchClients(String searchTerm, Pageable pageable) {
        return clientRepository.findBySearchTerm(searchTerm, pageable);
    }

    public List<KudaClient> getClientsByType(ClientTypeEnum type) {
        return clientRepository.findByType(type);
    }

    public List<KudaClient> getClientsByStatus(ClientStatusEnum status) {
        return clientRepository.findByStatut(status);
    }

    public List<KudaClient> getClientsByWilaya(String wilaya) {
        return clientRepository.findByWilaya(wilaya);
    }

    public List<KudaClient> getClientsByTypeAndStatus(ClientTypeEnum type, ClientStatusEnum status) {
        return clientRepository.findByTypeAndStatut(type, status);
    }

    public List<KudaClient> getClientsByCreatedUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(clientRepository::findByCreatedBy).orElse(List.of());
    }

    public Optional<KudaClient> updateClientStatus(Long id, ClientStatusEnum status) {
        Optional<KudaClient> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            KudaClient client = clientOpt.get();
            client.setStatut(status);
            return Optional.of(clientRepository.save(client));
        }
        return Optional.empty();
    }

    public Long getClientCountByType(ClientTypeEnum type) {
        return clientRepository.countByType(type);
    }

    public Long getClientCountByStatus(ClientStatusEnum status) {
        return clientRepository.countByStatut(status);
    }

    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    public boolean existsByNumeroRc(String numeroRc) {
        return clientRepository.existsByNumeroRc(numeroRc);
    }

    public Optional<KudaClient> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Optional<KudaClient> findByNumeroRc(String numeroRc) {
        return clientRepository.findByNumeroRc(numeroRc);
    }
}
