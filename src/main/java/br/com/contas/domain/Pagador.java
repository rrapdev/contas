package br.com.contas.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Pagador.
 */
@Entity
@Table(name = "pagador")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pagador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_pagador")
    private String nomePagador;

    @Column(name = "cpf_cnpj")
    private String cpfCnpj;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pagador id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomePagador() {
        return this.nomePagador;
    }

    public Pagador nomePagador(String nomePagador) {
        this.setNomePagador(nomePagador);
        return this;
    }

    public void setNomePagador(String nomePagador) {
        this.nomePagador = nomePagador;
    }

    public String getCpfCnpj() {
        return this.cpfCnpj;
    }

    public Pagador cpfCnpj(String cpfCnpj) {
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
        if (!(o instanceof Pagador)) {
            return false;
        }
        return id != null && id.equals(((Pagador) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pagador{" +
            "id=" + getId() +
            ", nomePagador='" + getNomePagador() + "'" +
            ", cpfCnpj='" + getCpfCnpj() + "'" +
            "}";
    }
}
