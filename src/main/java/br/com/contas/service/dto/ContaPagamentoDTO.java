package br.com.contas.service.dto;

import br.com.contas.domain.enumeration.Periodicidade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.contas.domain.ContaPagamento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaPagamentoDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dataVencimento;

    private String descricao;

    private BigDecimal valor;

    private LocalDate dataPagamento;

    private BigDecimal valorPago;

    private String observacoes;

    private Periodicidade periodicidade;

    private BeneficiarioDTO beneficiario;

    private CategoriaPagamentoDTO categoriaPagamento;

    private CaixaDTO caixa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Periodicidade getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(Periodicidade periodicidade) {
        this.periodicidade = periodicidade;
    }

    public BeneficiarioDTO getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(BeneficiarioDTO beneficiario) {
        this.beneficiario = beneficiario;
    }

    public CategoriaPagamentoDTO getCategoriaPagamento() {
        return categoriaPagamento;
    }

    public void setCategoriaPagamento(CategoriaPagamentoDTO categoriaPagamento) {
        this.categoriaPagamento = categoriaPagamento;
    }

    public CaixaDTO getCaixa() {
        return caixa;
    }

    public void setCaixa(CaixaDTO caixa) {
        this.caixa = caixa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaPagamentoDTO)) {
            return false;
        }

        ContaPagamentoDTO contaPagamentoDTO = (ContaPagamentoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contaPagamentoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaPagamentoDTO{" +
            "id=" + getId() +
            ", dataVencimento='" + getDataVencimento() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", dataPagamento='" + getDataPagamento() + "'" +
            ", valorPago=" + getValorPago() +
            ", observacoes='" + getObservacoes() + "'" +
            ", periodicidade='" + getPeriodicidade() + "'" +
            ", beneficiario=" + getBeneficiario() +
            ", categoriaPagamento=" + getCategoriaPagamento() +
            ", caixa=" + getCaixa() +
            "}";
    }
}
