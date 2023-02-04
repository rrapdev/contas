package br.com.contas.service.criteria;

import br.com.contas.domain.enumeration.Periodicidade;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.com.contas.domain.ContaPagamento} entity. This class is used
 * in {@link br.com.contas.web.rest.ContaPagamentoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conta-pagamentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaPagamentoCriteria implements Serializable, Criteria {

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

    private LocalDateFilter dataPagamento;

    private BigDecimalFilter valorPago;

    private StringFilter observacoes;

    private PeriodicidadeFilter periodicidade;

    private LongFilter beneficiarioId;

    private LongFilter categoriaPagamentoId;

    private LongFilter caixaId;

    private Boolean distinct;

    public ContaPagamentoCriteria() {}

    public ContaPagamentoCriteria(ContaPagamentoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dataVencimento = other.dataVencimento == null ? null : other.dataVencimento.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.dataPagamento = other.dataPagamento == null ? null : other.dataPagamento.copy();
        this.valorPago = other.valorPago == null ? null : other.valorPago.copy();
        this.observacoes = other.observacoes == null ? null : other.observacoes.copy();
        this.periodicidade = other.periodicidade == null ? null : other.periodicidade.copy();
        this.beneficiarioId = other.beneficiarioId == null ? null : other.beneficiarioId.copy();
        this.categoriaPagamentoId = other.categoriaPagamentoId == null ? null : other.categoriaPagamentoId.copy();
        this.caixaId = other.caixaId == null ? null : other.caixaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContaPagamentoCriteria copy() {
        return new ContaPagamentoCriteria(this);
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

    public LocalDateFilter getDataPagamento() {
        return dataPagamento;
    }

    public LocalDateFilter dataPagamento() {
        if (dataPagamento == null) {
            dataPagamento = new LocalDateFilter();
        }
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateFilter dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public BigDecimalFilter getValorPago() {
        return valorPago;
    }

    public BigDecimalFilter valorPago() {
        if (valorPago == null) {
            valorPago = new BigDecimalFilter();
        }
        return valorPago;
    }

    public void setValorPago(BigDecimalFilter valorPago) {
        this.valorPago = valorPago;
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

    public LongFilter getBeneficiarioId() {
        return beneficiarioId;
    }

    public LongFilter beneficiarioId() {
        if (beneficiarioId == null) {
            beneficiarioId = new LongFilter();
        }
        return beneficiarioId;
    }

    public void setBeneficiarioId(LongFilter beneficiarioId) {
        this.beneficiarioId = beneficiarioId;
    }

    public LongFilter getCategoriaPagamentoId() {
        return categoriaPagamentoId;
    }

    public LongFilter categoriaPagamentoId() {
        if (categoriaPagamentoId == null) {
            categoriaPagamentoId = new LongFilter();
        }
        return categoriaPagamentoId;
    }

    public void setCategoriaPagamentoId(LongFilter categoriaPagamentoId) {
        this.categoriaPagamentoId = categoriaPagamentoId;
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
        final ContaPagamentoCriteria that = (ContaPagamentoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataVencimento, that.dataVencimento) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(dataPagamento, that.dataPagamento) &&
            Objects.equals(valorPago, that.valorPago) &&
            Objects.equals(observacoes, that.observacoes) &&
            Objects.equals(periodicidade, that.periodicidade) &&
            Objects.equals(beneficiarioId, that.beneficiarioId) &&
            Objects.equals(categoriaPagamentoId, that.categoriaPagamentoId) &&
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
            dataPagamento,
            valorPago,
            observacoes,
            periodicidade,
            beneficiarioId,
            categoriaPagamentoId,
            caixaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaPagamentoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dataVencimento != null ? "dataVencimento=" + dataVencimento + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (dataPagamento != null ? "dataPagamento=" + dataPagamento + ", " : "") +
            (valorPago != null ? "valorPago=" + valorPago + ", " : "") +
            (observacoes != null ? "observacoes=" + observacoes + ", " : "") +
            (periodicidade != null ? "periodicidade=" + periodicidade + ", " : "") +
            (beneficiarioId != null ? "beneficiarioId=" + beneficiarioId + ", " : "") +
            (categoriaPagamentoId != null ? "categoriaPagamentoId=" + categoriaPagamentoId + ", " : "") +
            (caixaId != null ? "caixaId=" + caixaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
