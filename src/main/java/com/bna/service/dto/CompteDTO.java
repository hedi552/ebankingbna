package com.bna.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bna.domain.Compte} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteDTO implements Serializable {

    private Long id;

    @NotNull
    private String numcompte;

    @NotNull
    private Integer agence;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumcompte() {
        return numcompte;
    }

    public void setNumcompte(String numcompte) {
        this.numcompte = numcompte;
    }

    public Integer getAgence() {
        return agence;
    }

    public void setAgence(Integer agence) {
        this.agence = agence;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteDTO)) {
            return false;
        }

        CompteDTO compteDTO = (CompteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteDTO{" +
            "id=" + getId() +
            ", numcompte='" + getNumcompte() + "'" +
            ", agence=" + getAgence() +
            ", user=" + getUser() +
            "}";
    }
}
