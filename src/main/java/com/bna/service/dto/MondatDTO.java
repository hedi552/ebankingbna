package com.bna.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bna.domain.Mondat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MondatDTO implements Serializable {

    private Long id;

    @NotNull
    private String compteSrc;

    @NotNull
    private String compteBenef;

    @NotNull
    private Double montant;

    private String code;

    private CompteDTO compte;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompteSrc() {
        return compteSrc;
    }

    public void setCompteSrc(String compteSrc) {
        this.compteSrc = compteSrc;
    }

    public String getCompteBenef() {
        return compteBenef;
    }

    public void setCompteBenef(String compteBenef) {
        this.compteBenef = compteBenef;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        if (!(o instanceof MondatDTO)) {
            return false;
        }

        MondatDTO mondatDTO = (MondatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mondatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MondatDTO{" +
            "id=" + getId() +
            ", compteSrc='" + getCompteSrc() + "'" +
            ", compteBenef='" + getCompteBenef() + "'" +
            ", montant=" + getMontant() +
            ", code='" + getCode() + "'" +
            ", compte=" + getCompte() +
            "}";
    }
}
