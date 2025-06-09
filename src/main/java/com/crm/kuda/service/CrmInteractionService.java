package com.crm.kuda.service;

import com.crm.kuda.entity.CrmInteraction;
import com.crm.kuda.entity.KudaClient;
import com.crm.kuda.entity.User;
import com.crm.kuda.repository.CrmInteractionRepository;
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
public class CrmInteractionService {

    @Autowired
    private CrmInteractionRepository interactionRepository;

    @Autowired
    private KudaClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<CrmInteraction> getAllInteractions(Pageable pageable) {
        return interactionRepository.findAll(pageable);
    }

    public Optional<CrmInteraction> getInteractionById(Long id) {
        return interactionRepository.findById(id);
    }

    public CrmInteraction saveInteraction(CrmInteraction interaction) {
        if (interaction.getId() == null) {
            interaction.setDateInteraction(new Date());
        }
        
        // Mettre à jour la date de dernière interaction du client
        if (interaction.getClient() != null) {
            KudaClient client = interaction.getClient();
            client.setDateDerniereInteraction(interaction.getDateInteraction());
            clientRepository.save(client);
        }
        
        return interactionRepository.save(interaction);
    }

    public void deleteInteraction(Long id) {
        interactionRepository.deleteById(id);
    }

    public List<CrmInteraction> getInteractionsByClientId(Long clientId) {
        Optional<KudaClient> client = clientRepository.findById(clientId);
        return client.map(interactionRepository::findByClientOrderByDateInteractionDesc)
                    .orElse(List.of());
    }

    public Page<CrmInteraction> getInteractionsByClientId(Long clientId, Pageable pageable) {
        return interactionRepository.findByClientIdOrderByDateDesc(clientId, pageable);
    }

    public List<CrmInteraction> getInteractionsByType(InteractionTypeEnum type) {
        return interactionRepository.findByType(type);
    }

    public List<CrmInteraction> getInteractionsByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(interactionRepository::findByCreatedBy).orElse(List.of());
    }

    public List<CrmInteraction> getInteractionsByDateRange(Date dateDebut, Date dateFin) {
        return interactionRepository.findByDateRange(dateDebut, dateFin, Pageable.unpaged()).getContent();
    }

    public List<CrmInteraction> getInteractionsByClientAndDateRange(Long clientId, Date dateDebut, Date dateFin) {
        return interactionRepository.findByClientAndDateRange(clientId, dateDebut, dateFin);
    }

    public Long getInteractionCountByClientId(Long clientId) {
        Optional<KudaClient> client = clientRepository.findById(clientId);
        return client.map(interactionRepository::countByClient).orElse(0L);
    }

    public Long getInteractionCountByType(InteractionTypeEnum type) {
        return interactionRepository.countByType(type);
    }
}
