package br.com.contas.domain;

import br.com.contas.domain.enumeration.Periodicidade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ContaPagamento.
 */
@Entity
@Table(name = "conta_pagamento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaPagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor", precision = 21, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "valor_pago", precision = 21, scale = 2)
    private BigDecimal valorPago;

    @Column(name = "observacoes")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodicidade")
    private Periodicidade periodicidade;

    @ManyToOne
    private Beneficiario beneficiario;

    @ManyToOne
    private CategoriaPagamento categoriaPagamento;

    @ManyToOne
    private Caixa caixa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContaPagamento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataVencimento() {
        return this.dataVencimento;
    }

    public ContaPagamento dataVencimento(LocalDate dataVencimento) {
        this.setDataVencimento(dataVencimento);
        return this;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public ContaPagamento descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public ContaPagamento valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataPagamento() {
        return this.dataPagamento;
    }

    public ContaPagamento dataPagamento(LocalDate dataPagamento) {
        this.setDataPagamento(dataPagamento);
        return this;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public BigDecimal getValorPago() {
        return this.valorPago;
    }

    public ContaPagamento valorPago(BigDecimal valorPago) {
        this.setValorPago(valorPago);
        return this;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public String getObservacoes() {
        return this.observacoes;
    }

    public ContaPagamento observacoes(String observacoes) {
        this.setObservacoes(observacoes);
        return this;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Periodicidade getPeriodicidade() {
        return this.periodicidade;
    }

    public ContaPagamento periodicidade(Periodicidade periodicidade) {
        this.setPeriodicidade(periodicidade);
        return this;
    }

    public void setPeriodicidade(Periodicidade periodicidade) {
        this.periodicidade = periodicidade;
    }

    public Beneficiario getBeneficiario() {
        return this.beneficiario;
    }

    public void setBeneficiario(Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }

    public ContaPagamento beneficiario(Beneficiario beneficiario) {
        this.setBeneficiario(beneficiario);
        return this;
    }

    public CategoriaPagamento getCategoriaPagamento() {
        return this.categoriaPagamento;
    }

    public void setCategoriaPagamento(CategoriaPagamento categoriaPagamento) {
        this.categoriaPagamento = categoriaPagamento;
    }

    public ContaPagamento categoriaPagamento(CategoriaPagamento categoriaPagamento) {
        this.setCategoriaPagamento(categoriaPagamento);
        return this;
    }

    public Caixa getCaixa() {
        return this.caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public ContaPagamento caixa(Caixa caixa) {
        this.setCaixa(caixa);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaPagamento)) {
            return false;
        }
        return id != null && id.equals(((ContaPagamento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaPagamento{" +
            "id=" + getId() +
            ", dataVencimento='" + getDataVencimento() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", dataPagamento='" + getDataPagamento() + "'" +
            ", valorPago=" + getValorPago() +
            ", observacoes='" + getObservacoes() + "'" +
            ", periodicidade='" + getPeriodicidade() + "'" +
            "}";
    }
}
