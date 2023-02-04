package br.com.contas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.contas.domain.Pagador} entity. This class is used
 * in {@link br.com.contas.web.rest.PagadorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pagadors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PagadorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomePagador;

    private StringFilter cpfCnpj;

    private Boolean distinct;

    public PagadorCriteria() {}

    public PagadorCriteria(PagadorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomePagador = other.nomePagador == null ? null : other.nomePagador.copy();
        this.cpfCnpj = other.cpfCnpj == null ? null : other.cpfCnpj.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PagadorCriteria copy() {
        return new PagadorCriteria(this);
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

    public StringFilter getNomePagador() {
        return nomePagador;
    }

    public StringFilter nomePagador() {
        if (nomePagador == null) {
            nomePagador = new StringFilter();
        }
        return nomePagador;
    }

    public void setNomePagador(StringFilter nomePagador) {
        this.nomePagador = nomePagador;
    }

    public StringFilter getCpfCnpj() {
        return cpfCnpj;
    }

    public StringFilter cpfCnpj() {
        if (cpfCnpj == null) {
            cpfCnpj = new StringFilter();
        }
        return cpfCnpj;
    }

    public void setCpfCnpj(StringFilter cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
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
        final PagadorCriteria that = (PagadorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomePagador, that.nomePagador) &&
            Objects.equals(cpfCnpj, that.cpfCnpj) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomePagador, cpfCnpj, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagadorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomePagador != null ? "nomePagador=" + nomePagador + ", " : "") +
            (cpfCnpj != null ? "cpfCnpj=" + cpfCnpj + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
