package br.com.contas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.contas.domain.CategoriaRecebimento} entity. This class is used
 * in {@link br.com.contas.web.rest.CategoriaRecebimentoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categoria-recebimentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoriaRecebimentoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomeCategoria;

    private Boolean distinct;

    public CategoriaRecebimentoCriteria() {}

    public CategoriaRecebimentoCriteria(CategoriaRecebimentoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomeCategoria = other.nomeCategoria == null ? null : other.nomeCategoria.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CategoriaRecebimentoCriteria copy() {
        return new CategoriaRecebimentoCriteria(this);
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

    public StringFilter getNomeCategoria() {
        return nomeCategoria;
    }

    public StringFilter nomeCategoria() {
        if (nomeCategoria == null) {
            nomeCategoria = new StringFilter();
        }
        return nomeCategoria;
    }

    public void setNomeCategoria(StringFilter nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
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
        final CategoriaRecebimentoCriteria that = (CategoriaRecebimentoCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(nomeCategoria, that.nomeCategoria) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomeCategoria, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaRecebimentoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomeCategoria != null ? "nomeCategoria=" + nomeCategoria + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
