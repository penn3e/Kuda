package com.crm.kuda.dto;

import jakarta.validation.constraints.*;

public class CreateClientRequest {
    
    @NotNull(message = "Le type de client est obligatoire")
    private Long typeId;
    
    private Long categorieId;
    
    @NotBlank(message = "La raison sociale est obligatoire")
    @Size(max = 255, message = "La raison sociale ne peut pas dépasser 255 caractères")
    private String raisonSociale;
    
    @Size(max = 100, message = "Le nom du contact ne peut pas dépasser 100 caractères")
    private String nomContact;
    
    @Size(max = 100, message = "La fonction du contact ne peut pas dépasser 100 caractères")
    private String fonctionContact;
    
    @Email(message = "Format d'email invalide")
    private String email;
    
    @Size(max = 20, message = "Le téléphone ne peut pas dépasser 20 caractères")
    private String telephone;
    
    private String adresse;
    private String commune;
    private String wilaya;
    private String pays;
    private String codePostal;
    private String numeroRc;
    private String nif;
    
    private Long statutId;
    private String notes;

    // Getters and Setters
    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }
    
    public Long getCategorieId() { return categorieId; }
    public void setCategorieId(Long categorieId) { this.categorieId = categorieId; }
    
    public String getRaisonSociale() { return raisonSociale; }
    public void setRaisonSociale(String raisonSociale) { this.raisonSociale = raisonSociale; }
    
    public String getNomContact() { return nomContact; }
    public void setNomContact(String nomContact) { this.nomContact = nomContact; }
    
    public String getFonctionContact() { return fonctionContact; }
    public void setFonctionContact(String fonctionContact) { this.fonctionContact = fonctionContact; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getCommune() { return commune; }
    public void setCommune(String commune) { this.commune = commune; }
    
    public String getWilaya() { return wilaya; }
    public void setWilaya(String wilaya) { this.wilaya = wilaya; }
    
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    
    public String getNumeroRc() { return numeroRc; }
    public void setNumeroRc(String numeroRc) { this.numeroRc = numeroRc; }
    
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }
    
    public Long getStatutId() { return statutId; }
    public void setStatutId(Long statutId) { this.statutId = statutId; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
