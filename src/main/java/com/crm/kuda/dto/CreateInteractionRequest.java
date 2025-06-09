package com.crm.kuda.dto;

import jakarta.validation.constraints.*;

public class CreateInteractionRequest {
    
    @NotNull(message = "L'ID du client est obligatoire")
    private Long clientId;
    
    @NotNull(message = "Le type d'interaction est obligatoire")
    private Long typeId;
    
    @Size(max = 2000, message = "Le commentaire ne peut pas dépasser 2000 caractères")
    private String commentaire;
    
    // Getters and Setters
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    
    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }
    
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
