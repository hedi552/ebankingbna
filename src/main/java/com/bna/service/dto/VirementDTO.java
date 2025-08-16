package com.bna.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bna.domain.Virement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VirementDTO implements Serializable {

    private Long id;

    @NotNull
    private String compteBeneficiaire;

    @NotNull
    private String motif;

    private CompteDTO compte;

    private CompteDTO beneficiaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompteBeneficiaire() {
        return compteBeneficiaire;
    }

    public void setCompteBeneficiaire(String compteBeneficiaire) {
        this.compteBeneficiaire = compteBeneficiaire;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public CompteDTO getCompte() {
        return compte;
    }

    public void setCompte(CompteDTO compte) {
        this.compte = compte;
    }

    public CompteDTO getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(CompteDTO beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VirementDTO)) {
            return false;
        }

        VirementDTO virementDTO = (VirementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, virementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VirementDTO{" +
            "id=" + getId() +
            ", compteBeneficiaire='" + getCompteBeneficiaire() + "'" +
            ", motif='" + getMotif() + "'" +
            ", compte=" + getCompte() +
            ", beneficiaire=" + getBeneficiaire() +
            "}";
    }
}
