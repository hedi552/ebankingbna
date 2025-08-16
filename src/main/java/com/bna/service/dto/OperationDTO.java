package com.bna.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bna.domain.Operation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperationDTO implements Serializable {

    private Long id;

    @NotNull
    private Long montant;

    @NotNull
    private String libelle;

    private CompteDTO compte;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public CompteDTO getCompte() {
        return compte;
    }

    public void setCompte(CompteDTO compte) {
        this.compte = compte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperationDTO)) {
            return false;
        }

        OperationDTO operationDTO = (OperationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, operationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperationDTO{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", libelle='" + getLibelle() + "'" +
            ", compte=" + getCompte() +
            "}";
    }
}
