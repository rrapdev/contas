package br.com.contas.service.criteria;

import br.com.contas.domain.enumeration.Periodicidade;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.contas.domain.ContaRecebimento} entity. This class is used
 * in {@link br.com.contas.web.rest.ContaRecebimentoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conta-recebimentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaRecebimentoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Periodicidade
     */
    public static class PeriodicidadeFilter extends Filter<Periodicidade> {

        public PeriodicidadeFilter() {}

        public PeriodicidadeFilter(PeriodicidadeFilter filter) {
            super(filter);
        }

        @Override
        public PeriodicidadeFilter copy() {
            return new PeriodicidadeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dataVencimento;

    private StringFilter descricao;

    private BigDecimalFilter valor;

    private LocalDateFilter dataRecebimento;

    private BigDecimalFilter valorRecebido;

    private StringFilter observacoes;

    private PeriodicidadeFilter periodicidade;

    private LongFilter pagadorId;

    private LongFilter categoriaRecebimentoId;

    private LongFilter caixaId;

    private Boolean distinct;

    public ContaRecebimentoCriteria() {}

    public ContaRecebimentoCriteria(ContaRecebimentoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dataVencimento = other.dataVencimento == null ? null : other.dataVencimento.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.dataRecebimento = other.dataRecebimento == null ? null : other.dataRecebimento.copy();
        this.valorRecebido = other.valorRecebido == null ? null : other.valorRecebido.copy();
        this.observacoes = other.observacoes == null ? null : other.observacoes.copy();
        this.periodicidade = other.periodicidade == null ? null : other.periodicidade.copy();
        this.pagadorId = other.pagadorId == null ? null : other.pagadorId.copy();
        this.categoriaRecebimentoId = other.categoriaRecebimentoId == null ? null : other.categoriaRecebimentoId.copy();
        this.caixaId = other.caixaId == null ? null : other.caixaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContaRecebimentoCriteria copy() {
        return new ContaRecebimentoCriteria(this);
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

    public LocalDateFilter getDataVencimento() {
        return dataVencimento;
    }

    public LocalDateFilter dataVencimento() {
        if (dataVencimento == null) {
            dataVencimento = new LocalDateFilter();
        }
        return dataVencimento;
    }

    public void setDataVencimento(LocalDateFilter dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public StringFilter descricao() {
        if (descricao == null) {
            descricao = new StringFilter();
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public BigDecimalFilter getValor() {
        return valor;
    }

    public BigDecimalFilter valor() {
        if (valor == null) {
            valor = new BigDecimalFilter();
        }
        return valor;
    }

    public void setValor(BigDecimalFilter valor) {
        this.valor = valor;
    }

    public LocalDateFilter getDataRecebimento() {
        return dataRecebimento;
    }

    public LocalDateFilter dataRecebimento() {
        if (dataRecebimento == null) {
            dataRecebimento = new LocalDateFilter();
        }
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDateFilter dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public BigDecimalFilter getValorRecebido() {
        return valorRecebido;
    }

    public BigDecimalFilter valorRecebido() {
        if (valorRecebido == null) {
            valorRecebido = new BigDecimalFilter();
        }
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimalFilter valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public StringFilter getObservacoes() {
        return observacoes;
    }

    public StringFilter observacoes() {
        if (observacoes == null) {
            observacoes = new StringFilter();
        }
        return observacoes;
    }

    public void setObservacoes(StringFilter observacoes) {
        this.observacoes = observacoes;
    }

    public PeriodicidadeFilter getPeriodicidade() {
        return periodicidade;
    }

    public PeriodicidadeFilter periodicidade() {
        if (periodicidade == null) {
            periodicidade = new PeriodicidadeFilter();
        }
        return periodicidade;
    }

    public void setPeriodicidade(PeriodicidadeFilter periodicidade) {
        this.periodicidade = periodicidade;
    }

    public LongFilter getPagadorId() {
        return pagadorId;
    }

    public LongFilter pagadorId() {
        if (pagadorId == null) {
            pagadorId = new LongFilter();
        }
        return pagadorId;
    }

    public void setPagadorId(LongFilter pagadorId) {
        this.pagadorId = pagadorId;
    }

    public LongFilter getCategoriaRecebimentoId() {
        return categoriaRecebimentoId;
    }

    public LongFilter categoriaRecebimentoId() {
        if (categoriaRecebimentoId == null) {
            categoriaRecebimentoId = new LongFilter();
        }
        return categoriaRecebimentoId;
    }

    public void setCategoriaRecebimentoId(LongFilter categoriaRecebimentoId) {
        this.categoriaRecebimentoId = categoriaRecebimentoId;
    }

    public LongFilter getCaixaId() {
        return caixaId;
    }

    public LongFilter caixaId() {
        if (caixaId == null) {
            caixaId = new LongFilter();
        }
        return caixaId;
    }

    public void setCaixaId(LongFilter caixaId) {
        this.caixaId = caixaId;
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
        final ContaRecebimentoCriteria that = (ContaRecebimentoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataVencimento, that.dataVencimento) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(dataRecebimento, that.dataRecebimento) &&
            Objects.equals(valorRecebido, that.valorRecebido) &&
            Objects.equals(observacoes, that.observacoes) &&
            Objects.equals(periodicidade, that.periodicidade) &&
            Objects.equals(pagadorId, that.pagadorId) &&
            Objects.equals(categoriaRecebimentoId, that.categoriaRecebimentoId) &&
            Objects.equals(caixaId, that.caixaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataVencimento,
            descricao,
            valor,
            dataRecebimento,
            valorRecebido,
            observacoes,
            periodicidade,
            pagadorId,
            categoriaRecebimentoId,
            caixaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaRecebimentoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dataVencimento != null ? "dataVencimento=" + dataVencimento + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (dataRecebimento != null ? "dataRecebimento=" + dataRecebimento + ", " : "") +
            (valorRecebido != null ? "valorRecebido=" + valorRecebido + ", " : "") +
            (observacoes != null ? "observacoes=" + observacoes + ", " : "") +
            (periodicidade != null ? "periodicidade=" + periodicidade + ", " : "") +
            (pagadorId != null ? "pagadorId=" + pagadorId + ", " : "") +
            (categoriaRecebimentoId != null ? "categoriaRecebimentoId=" + categoriaRecebimentoId + ", " : "") +
            (caixaId != null ? "caixaId=" + caixaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
