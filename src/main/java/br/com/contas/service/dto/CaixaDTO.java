package br.com.contas.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.contas.domain.Caixa} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CaixaDTO implements Serializable {

    private Long id;

    private String nomeCaixa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCaixa() {
        return nomeCaixa;
    }

    public void setNomeCaixa(String nomeCaixa) {
        this.nomeCaixa = nomeCaixa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CaixaDTO)) {
            return false;
        }

        CaixaDTO caixaDTO = (CaixaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, caixaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaixaDTO{" +
            "id=" + getId() +
            ", nomeCaixa='" + getNomeCaixa() + "'" +
            "}";
    }
}
