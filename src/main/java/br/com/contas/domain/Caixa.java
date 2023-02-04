package br.com.contas.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Caixa.
 */
@Entity
@Table(name = "caixa")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Caixa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_caixa")
    private String nomeCaixa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Caixa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCaixa() {
        return this.nomeCaixa;
    }

    public Caixa nomeCaixa(String nomeCaixa) {
        this.setNomeCaixa(nomeCaixa);
        return this;
    }

    public void setNomeCaixa(String nomeCaixa) {
        this.nomeCaixa = nomeCaixa;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Caixa)) {
            return false;
        }
        return id != null && id.equals(((Caixa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Caixa{" +
            "id=" + getId() +
            ", nomeCaixa='" + getNomeCaixa() + "'" +
            "}";
    }
}
