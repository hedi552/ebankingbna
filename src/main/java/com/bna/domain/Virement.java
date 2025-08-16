package com.bna.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Virement.
 */
@Entity
@Table(name = "virement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Virement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "compte_beneficiaire", nullable = false)
    private String compteBeneficiaire;

    @NotNull
    @Column(name = "motif", nullable = false)
    private String motif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Compte compte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Compte beneficiaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Virement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompteBeneficiaire() {
        return this.compteBeneficiaire;
    }

    public Virement compteBeneficiaire(String compteBeneficiaire) {
        this.setCompteBeneficiaire(compteBeneficiaire);
        return this;
    }

    public void setCompteBeneficiaire(String compteBeneficiaire) {
        this.compteBeneficiaire = compteBeneficiaire;
    }

    public String getMotif() {
        return this.motif;
    }

    public Virement motif(String motif) {
        this.setMotif(motif);
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Compte getCompte() {
        return this.compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Virement compte(Compte compte) {
        this.setCompte(compte);
        return this;
    }

    public Compte getBeneficiaire() {
        return this.beneficiaire;
    }

    public void setBeneficiaire(Compte compte) {
        this.beneficiaire = compte;
    }

    public Virement beneficiaire(Compte compte) {
        this.setBeneficiaire(compte);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Virement)) {
            return false;
        }
        return getId() != null && getId().equals(((Virement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Virement{" +
            "id=" + getId() +
            ", compteBeneficiaire='" + getCompteBeneficiaire() + "'" +
            ", motif='" + getMotif() + "'" +
            "}";
    }
}
