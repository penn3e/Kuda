package com.crm.kuda.configsecurity;

import com.crm.kuda.entity.*;
import com.crm.kuda.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ClientTypeRepository clientTypeRepository;
    
    @Autowired
    private ClientStatusRepository clientStatusRepository;
    
    @Autowired
    private ClientCategoryRepository clientCategoryRepository;
    
    @Autowired
    private InteractionTypeRepository interactionTypeRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Initialisation des données de référence...");
        
        initializeUserRoles();
        initializeClientTypes();
        initializeClientStatus();
        initializeClientCategories();
        initializeInteractionTypes();
        initializeDefaultUsers();
        
        System.out.println("✅ Initialisation terminée avec succès !");
    }

    private void initializeUserRoles() {
        System.out.println("👥 Initialisation des rôles utilisateurs...");
        
        String[][] userRoles = {
            {"ROLE_OPERATIONAL", "👨‍💼 Opérationnel"},
            {"ROLE_SUPERVISOR", "👨‍💻 Superviseur"},
            {"ROLE_ADMIN", "👑 Administrateur"}
        };

        for (String[] roleData : userRoles) {
            if (!userRoleRepository.existsByCode(roleData[0])) {
                UserRole userRole = new UserRole(roleData[0], roleData[1]);
                userRoleRepository.save(userRole);
                System.out.println("✅ Rôle utilisateur créé : " + roleData[0]);
            }
        }
    }

    private void initializeClientTypes() {
        System.out.println("🏷️ Initialisation des types de clients...");
        
        String[][] clientTypes = {
            {"CLIENT", "🧑‍⚕️ CLIENT"},
            {"PROSPECT", "📞 PROSPECT"},
            {"FOURNISSEUR", "🏭 FOURNISSEUR"},
            {"PARTENAIRE", "🤝 PARTENAIRE"},
            {"TESTEUR", "🧪 TESTEUR"},
            {"DISTRIBUTEUR", "🏬 DISTRIBUTEUR"},
            {"REVENDEUR", "🌐 REVENDEUR"},
            {"SOUS-TRAITANT", "🧾 SOUS-TRAITANT"},
            {"ARCHIVÉ", "🚫 ARCHIVÉ"}
        };

        for (String[] typeData : clientTypes) {
            if (!clientTypeRepository.existsByCode(typeData[0])) {
                ClientType clientType = new ClientType(typeData[0], typeData[1]);
                clientTypeRepository.save(clientType);
                System.out.println("✅ Type client créé : " + typeData[0]);
            }
        }
    }

    private void initializeClientStatus() {
        System.out.println("📊 Initialisation des statuts clients...");
        
        String[][] clientStatuses = {
            {"ACTIF", "✅ ACTIF"},
            {"EN_NÉGOCIATION", "⏳ EN_NÉGOCIATION"},
            {"EN_TEST", "🧪 EN_TEST"},
            {"À_RELANCER", "📞 À_RELANCER"},
            {"PERDU", "❌ PERDU"},
            {"INACTIF", "🚫 INACTIF"},
            {"NOUVEAU", "🆕 NOUVEAU"},
            {"EN_ATTENTE_DOCS", "📝 EN_ATTENTE_DOCS"},
            {"BLOQUÉ", "🛑 BLOQUÉ"},
            {"VALIDÉ", "✅ VALIDÉ"}
        };

        for (String[] statusData : clientStatuses) {
            if (!clientStatusRepository.existsByCode(statusData[0])) {
                ClientStatus clientStatus = new ClientStatus(statusData[0], statusData[1]);
                clientStatusRepository.save(clientStatus);
                System.out.println("✅ Statut client créé : " + statusData[0]);
            }
        }
    }

    private void initializeClientCategories() {
        System.out.println("📂 Initialisation des catégories clients...");
        
        String[][] clientCategories = {
            // Points de vente
            {"PHARMACIE", "🏪 Points de vente", "Pharmacie"},
            {"PARAPHARMACIE", "🏪 Points de vente", "Parapharmacie"},
            {"MAGASIN_MEDICAL", "🏪 Points de vente", "Magasin matériel médical"},
            
            // Établissements de santé
            {"HOPITAL_PUBLIC", "🏥 Établissements de santé", "Hôpital public"},
            {"CLINIQUE_PRIVÉE", "🏥 Établissements de santé", "Clinique privée"},
            {"POLYCLINIQUE", "🏥 Établissements de santé", "Polyclinique"},
            {"CENTRE_SANTÉ", "🏥 Établissements de santé", "Centre de santé / Dispensaire"},
            {"EHPAD", "🏥 Établissements de santé", "EHPAD / Maison de retraite"},
            
            // Intermédiaires / Distributeurs
            {"GROSSISTE", "🏭 Intermédiaires / Distributeurs", "Grossiste répartiteur"},
            {"CENTRALE_ACHAT", "🏭 Intermédiaires / Distributeurs", "Centrale d'achat"},
            {"DEPOSITAIRE", "🏭 Intermédiaires / Distributeurs", "Dépositaire"},
            {"IMPORT_EXPORT", "🏭 Intermédiaires / Distributeurs", "Importateur / Exportateur"},
            
            // Laboratoires / Recherche
            {"LABO_ANALYSE", "⚗️ Laboratoires / Recherche", "Laboratoire d'analyse médicale"},
            {"LABO_RECHERCHE", "⚗️ Laboratoires / Recherche", "Laboratoire de recherche / universitaire"},
            {"LABO_PARTENAIRE", "⚗️ Laboratoires / Recherche", "Laboratoire partenaire"},
            
            // Professionnels de santé
            {"MEDECIN", "👩‍⚕️ Professionnels de santé", "Médecin / Cabinet médical"},
            {"DENTISTE", "👩‍⚕️ Professionnels de santé", "Dentiste"},
            {"KINESITHERAPEUTE", "👩‍⚕️ Professionnels de santé", "Kinésithérapeute"},
            {"INFIRMIER", "👩‍⚕️ Professionnels de santé", "Infirmier libéral"},
            {"VETERINAIRE", "👩‍⚕️ Professionnels de santé", "Vétérinaire"},
            
            // Institutionnels / Publics
            {"MINISTERE_SANTE", "🏛️ Institutionnels / Publics", "Ministère de la santé"},
            {"PHARMACIE_HOSPITALIERE", "🏛️ Institutionnels / Publics", "Pharmacie hospitalière"},
            {"AGENCE_REGULATION", "🏛️ Institutionnels / Publics", "Agence de régulation / contrôle"},
            
            // Autres
            {"ONG", "🌍 Autres", "ONG médicale / Humanitaire"},
            {"ENTREPRISE_INFIRMERIE", "🌍 Autres", "Entreprise avec infirmerie"},
            {"ECOLE_FACULTE", "🌍 Autres", "École / Faculté médicale"}
        };

        for (String[] categoryData : clientCategories) {
            if (!clientCategoryRepository.existsByCode(categoryData[0])) {
                ClientCategory clientCategory = new ClientCategory(categoryData[0], categoryData[1], categoryData[2]);
                clientCategoryRepository.save(clientCategory);
                System.out.println("✅ Catégorie client créée : " + categoryData[0]);
            }
        }
    }

    private void initializeInteractionTypes() {
        System.out.println("💬 Initialisation des types d'interactions...");
        
        String[][] interactionTypes = {
            {"APPEL", "📞 Appel téléphonique"},
            {"EMAIL", "📧 Email"},
            {"RDV_PHYSIQUE", "🤝 Rendez-vous physique"},
            {"VISIO", "💻 Visio / Réunion en ligne"},
            {"LIVRAISON", "📦 Livraison / Réception"},
            {"SIGNATURE_CONTRAT", "📝 Signature de contrat"},
            {"DEMANDE_INFO", "📄 Demande d'information"},
            {"RELANCE", "🔁 Relance"},
            {"TEST_PRODUIT", "🧪 Test produit"},
            {"FACTURATION", "💸 Règlement / Facturation"},
            {"RECLAMATION", "🧾 Réclamation"},
            {"SUIVI_POST_VENTE", "🗓️ Suivi post-vente"},
            {"COURRIER", "✉️ Courrier postal"},
            {"PRESENTATION", "📊 Présentation produit"},
            {"INCIDENT", "⚠️ Incident / Blocage"},
            {"PREMIER_CONTACT", "🆕 Premier contact"}
        };

        for (String[] typeData : interactionTypes) {
            if (!interactionTypeRepository.existsByCode(typeData[0])) {
                InteractionType interactionType = new InteractionType(typeData[0], typeData[1]);
                interactionTypeRepository.save(interactionType);
                System.out.println("✅ Type d'interaction créé : " + typeData[0]);
            }
        }
    }

    private void initializeDefaultUsers() {
        System.out.println("👥 Initialisation des utilisateurs par défaut...");
        
        // Créer un utilisateur admin par défaut
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@kuda.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrateur Principal");

            Set<UserRole> adminRoles = new HashSet<>();
            UserRole adminRole = userRoleRepository.findByCode("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found."));
            adminRoles.add(adminRole);
            admin.setRoles(adminRoles);

            userRepository.save(admin);
            System.out.println("✅ Utilisateur admin créé : username=admin, password=admin123");
        }

        // Créer un superviseur de test
        if (!userRepository.existsByUsername("supervisor")) {
            User supervisor = new User();
            supervisor.setUsername("supervisor");
            supervisor.setEmail("supervisor@kuda.com");
            supervisor.setPassword(passwordEncoder.encode("supervisor123"));
            supervisor.setFullName("Superviseur Test");

            Set<UserRole> supervisorRoles = new HashSet<>();
            UserRole supervisorRole = userRoleRepository.findByCode("ROLE_SUPERVISOR")
                    .orElseThrow(() -> new RuntimeException("Error: Role SUPERVISOR not found."));
            supervisorRoles.add(supervisorRole);
            supervisor.setRoles(supervisorRoles);

            userRepository.save(supervisor);
            System.out.println("✅ Utilisateur supervisor créé : username=supervisor, password=supervisor123");
        }

        // Créer un utilisateur opérationnel de test
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setEmail("user@kuda.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFullName("Utilisateur Opérationnel");

            Set<UserRole> userRoles = new HashSet<>();
            UserRole userRole = userRoleRepository.findByCode("ROLE_OPERATIONAL")
                    .orElseThrow(() -> new RuntimeException("Error: Role OPERATIONAL not found."));
            userRoles.add(userRole);
            user.setRoles(userRoles);

            userRepository.save(user);
            System.out.println("✅ Utilisateur opérationnel créé : username=user, password=user123");
        }
    }
}
