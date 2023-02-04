package br.com.contas.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.contas.domain.Pagador} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PagadorDTO implements Serializable {

    private Long id;

    private String nomePagador;

    private String cpfCnpj;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomePagador() {
        return nomePagador;
    }

    public void setNomePagador(String nomePagador) {
        this.nomePagador = nomePagador;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PagadorDTO)) {
            return false;
        }

        PagadorDTO pagadorDTO = (PagadorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pagadorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagadorDTO{" +
            "id=" + getId() +
            ", nomePagador='" + getNomePagador() + "'" +
            ", cpfCnpj='" + getCpfCnpj() + "'" +
            "}";
    }
}
