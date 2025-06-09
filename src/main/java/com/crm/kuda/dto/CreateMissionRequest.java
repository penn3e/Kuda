package com.crm.kuda.dto;

import jakarta.validation.constraints.*;
import java.util.Date;

public class CreateMissionRequest {
    
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255, message = "Le titre ne peut pas dépasser 255 caractères")
    private String titre;
    
    @Size(max = 2000, message = "La description ne peut pas dépasser 2000 caractères")
    private String description;
    
    @NotNull(message = "Le type d'interaction est obligatoire")
    private Long typeInteractionId;
    
    @NotNull(message = "La date de début est obligatoire")
    private Date dateDebut;
    
    @NotNull(message = "La date de fin est obligatoire")
    private Date dateFin;
    
    @NotNull(message = "L'utilisateur assigné est obligatoire")
    private Long assignedToId;
    
    // Getters and Setters
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Long getTypeInteractionId() { return typeInteractionId; }
    public void setTypeInteractionId(Long typeInteractionId) { this.typeInteractionId = typeInteractionId; }
    
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    
    public Long getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Long assignedToId) { this.assignedToId = assignedToId; }
}
