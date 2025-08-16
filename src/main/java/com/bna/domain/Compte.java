package com.bna.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Compte.
 */
@Entity
@Table(name = "compte")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Compte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numcompte", nullable = false)
    private String numcompte;

    @NotNull
    @Column(name = "agence", nullable = false)
    private Integer agence;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Compte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumcompte() {
        return this.numcompte;
    }

    public Compte numcompte(String numcompte) {
        this.setNumcompte(numcompte);
        return this;
    }

    public void setNumcompte(String numcompte) {
        this.numcompte = numcompte;
    }

    public Integer getAgence() {
        return this.agence;
    }

    public Compte agence(Integer agence) {
        this.setAgence(agence);
        return this;
    }

    public void setAgence(Integer agence) {
        this.agence = agence;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Compte user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compte)) {
            return false;
        }
        return getId() != null && getId().equals(((Compte) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compte{" +
            "id=" + getId() +
            ", numcompte='" + getNumcompte() + "'" +
            ", agence=" + getAgence() +
            "}";
    }
}
