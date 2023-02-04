package br.com.contas.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A CategoriaPagamento.
 */
@Entity
@Table(name = "categoria_pagamento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoriaPagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_categoria")
    private String nomeCategoria;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CategoriaPagamento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCategoria() {
        return this.nomeCategoria;
    }

    public CategoriaPagamento nomeCategoria(String nomeCategoria) {
        this.setNomeCategoria(nomeCategoria);
        return this;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaPagamento)) {
            return false;
        }
        return id != null && id.equals(((CategoriaPagamento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaPagamento{" +
            "id=" + getId() +
            ", nomeCategoria='" + getNomeCategoria() + "'" +
            "}";
    }
}
