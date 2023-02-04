package br.com.contas.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.contas.domain.CategoriaPagamento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoriaPagamentoDTO implements Serializable {

    private Long id;

    private String nomeCategoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaPagamentoDTO)) {
            return false;
        }

        CategoriaPagamentoDTO categoriaPagamentoDTO = (CategoriaPagamentoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoriaPagamentoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaPagamentoDTO{" +
            "id=" + getId() +
            ", nomeCategoria='" + getNomeCategoria() + "'" +
            "}";
    }
}
