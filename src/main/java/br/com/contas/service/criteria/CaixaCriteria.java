package br.com.contas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.contas.domain.Caixa} entity. This class is used
 * in {@link br.com.contas.web.rest.CaixaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /caixas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CaixaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomeCaixa;

    private Boolean distinct;

    public CaixaCriteria() {}

    public CaixaCriteria(CaixaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomeCaixa = other.nomeCaixa == null ? null : other.nomeCaixa.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CaixaCriteria copy() {
        return new CaixaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNomeCaixa() {
        return nomeCaixa;
    }

    public StringFilter nomeCaixa() {
        if (nomeCaixa == null) {
            nomeCaixa = new StringFilter();
        }
        return nomeCaixa;
    }

    public void setNomeCaixa(StringFilter nomeCaixa) {
        this.nomeCaixa = nomeCaixa;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CaixaCriteria that = (CaixaCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(nomeCaixa, that.nomeCaixa) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomeCaixa, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaixaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomeCaixa != null ? "nomeCaixa=" + nomeCaixa + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
