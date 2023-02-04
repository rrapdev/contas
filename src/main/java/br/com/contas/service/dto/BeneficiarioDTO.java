package br.com.contas.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.contas.domain.Beneficiario} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BeneficiarioDTO implements Serializable {

    private Long id;

    private String nomeBeneficiario;

    private String cpfCnpj;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeBeneficiario() {
        return nomeBeneficiario;
    }

    public void setNomeBeneficiario(String nomeBeneficiario) {
        this.nomeBeneficiario = nomeBeneficiario;
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
        if (!(o instanceof BeneficiarioDTO)) {
            return false;
        }

        BeneficiarioDTO beneficiarioDTO = (BeneficiarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, beneficiarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BeneficiarioDTO{" +
            "id=" + getId() +
            ", nomeBeneficiario='" + getNomeBeneficiario() + "'" +
            ", cpfCnpj='" + getCpfCnpj() + "'" +
            "}";
    }
}
