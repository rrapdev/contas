package br.com.contas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.contas.domain.Beneficiario} entity. This class is used
 * in {@link br.com.contas.web.rest.BeneficiarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /beneficiarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BeneficiarioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomeBeneficiario;

    private StringFilter cpfCnpj;

    private Boolean distinct;

    public BeneficiarioCriteria() {}

    public BeneficiarioCriteria(BeneficiarioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomeBeneficiario = other.nomeBeneficiario == null ? null : other.nomeBeneficiario.copy();
        this.cpfCnpj = other.cpfCnpj == null ? null : other.cpfCnpj.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BeneficiarioCriteria copy() {
        return new BeneficiarioCriteria(this);
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

    public StringFilter getNomeBeneficiario() {
        return nomeBeneficiario;
    }

    public StringFilter nomeBeneficiario() {
        if (nomeBeneficiario == null) {
            nomeBeneficiario = new StringFilter();
        }
        return nomeBeneficiario;
    }

    public void setNomeBeneficiario(StringFilter nomeBeneficiario) {
        this.nomeBeneficiario = nomeBeneficiario;
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
        final BeneficiarioCriteria that = (BeneficiarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomeBeneficiario, that.nomeBeneficiario) &&
            Objects.equals(cpfCnpj, that.cpfCnpj) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomeBeneficiario, cpfCnpj, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BeneficiarioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomeBeneficiario != null ? "nomeBeneficiario=" + nomeBeneficiario + ", " : "") +
            (cpfCnpj != null ? "cpfCnpj=" + cpfCnpj + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
