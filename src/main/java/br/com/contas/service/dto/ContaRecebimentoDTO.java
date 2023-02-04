package br.com.contas.service.dto;

import br.com.contas.domain.enumeration.Periodicidade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.contas.domain.ContaRecebimento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaRecebimentoDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dataVencimento;

    private String descricao;

    private BigDecimal valor;

    private LocalDate dataRecebimento;

    private BigDecimal valorRecebido;

    private String observacoes;

    private Periodicidade periodicidade;

    private PagadorDTO pagador;

    private CategoriaRecebimentoDTO categoriaRecebimento;

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

    public LocalDate getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
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

    public PagadorDTO getPagador() {
        return pagador;
    }

    public void setPagador(PagadorDTO pagador) {
        this.pagador = pagador;
    }

    public CategoriaRecebimentoDTO getCategoriaRecebimento() {
        return categoriaRecebimento;
    }

    public void setCategoriaRecebimento(CategoriaRecebimentoDTO categoriaRecebimento) {
        this.categoriaRecebimento = categoriaRecebimento;
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
        if (!(o instanceof ContaRecebimentoDTO)) {
            return false;
        }

        ContaRecebimentoDTO contaRecebimentoDTO = (ContaRecebimentoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contaRecebimentoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaRecebimentoDTO{" +
            "id=" + getId() +
            ", dataVencimento='" + getDataVencimento() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", dataRecebimento='" + getDataRecebimento() + "'" +
            ", valorRecebido=" + getValorRecebido() +
            ", observacoes='" + getObservacoes() + "'" +
            ", periodicidade='" + getPeriodicidade() + "'" +
            ", pagador=" + getPagador() +
            ", categoriaRecebimento=" + getCategoriaRecebimento() +
            ", caixa=" + getCaixa() +
            "}";
    }
}
