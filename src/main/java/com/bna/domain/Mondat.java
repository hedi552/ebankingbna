package com.bna.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Mondat.
 */
@Entity
@Table(name = "mondat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mondat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "compte_src", nullable = false)
    private String compteSrc;

    @NotNull
    @Column(name = "compte_benef", nullable = false)
    private String compteBenef;

    @NotNull
    @Column(name = "montant", nullable = false)
    private Double montant;

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Compte compte;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mondat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompteSrc() {
        return this.compteSrc;
    }

    public Mondat compteSrc(String compteSrc) {
        this.setCompteSrc(compteSrc);
        return this;
    }

    public void setCompteSrc(String compteSrc) {
        this.compteSrc = compteSrc;
    }

    public String getCompteBenef() {
        return this.compteBenef;
    }

    public Mondat compteBenef(String compteBenef) {
        this.setCompteBenef(compteBenef);
        return this;
    }

    public void setCompteBenef(String compteBenef) {
        this.compteBenef = compteBenef;
    }

    public Double getMontant() {
        return this.montant;
    }

    public Mondat montant(Double montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getCode() {
        return this.code;
    }

    public Mondat code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Compte getCompte() {
        return this.compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Mondat compte(Compte compte) {
        this.setCompte(compte);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mondat)) {
            return false;
        }
        return getId() != null && getId().equals(((Mondat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mondat{" +
            "id=" + getId() +
            ", compteSrc='" + getCompteSrc() + "'" +
            ", compteBenef='" + getCompteBenef() + "'" +
            ", montant=" + getMontant() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
