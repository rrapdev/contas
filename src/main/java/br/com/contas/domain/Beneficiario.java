package br.com.contas.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Beneficiario.
 */
@Entity
@Table(name = "beneficiario")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Beneficiario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_beneficiario")
    private String nomeBeneficiario;

    @Column(name = "cpf_cnpj")
    private String cpfCnpj;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Beneficiario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeBeneficiario() {
        return this.nomeBeneficiario;
    }

    public Beneficiario nomeBeneficiario(String nomeBeneficiario) {
        this.setNomeBeneficiario(nomeBeneficiario);
        return this;
    }

    public void setNomeBeneficiario(String nomeBeneficiario) {
        this.nomeBeneficiario = nomeBeneficiario;
    }

    public String getCpfCnpj() {
        return this.cpfCnpj;
    }

    public Beneficiario cpfCnpj(String cpfCnpj) {
        this.setCpfCnpj(cpfCnpj);
        return this;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Beneficiario)) {
            return false;
        }
        return id != null && id.equals(((Beneficiario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Beneficiario{" +
            "id=" + getId() +
            ", nomeBeneficiario='" + getNomeBeneficiario() + "'" +
            ", cpfCnpj='" + getCpfCnpj() + "'" +
            "}";
    }
}
