package br.com.contas.domain;

import br.com.contas.domain.enumeration.Periodicidade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ContaRecebimento.
 */
@Entity
@Table(name = "conta_recebimento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContaRecebimento implements Serializable {

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

    @Column(name = "data_recebimento")
    private LocalDate dataRecebimento;

    @Column(name = "valor_recebido", precision = 21, scale = 2)
    private BigDecimal valorRecebido;

    @Column(name = "observacoes")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodicidade")
    private Periodicidade periodicidade;

    @ManyToOne
    private Pagador pagador;

    @ManyToOne
    private CategoriaRecebimento categoriaRecebimento;

    @ManyToOne
    private Caixa caixa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContaRecebimento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataVencimento() {
        return this.dataVencimento;
    }

    public ContaRecebimento dataVencimento(LocalDate dataVencimento) {
        this.setDataVencimento(dataVencimento);
        return this;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public ContaRecebimento descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public ContaRecebimento valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataRecebimento() {
        return this.dataRecebimento;
    }

    public ContaRecebimento dataRecebimento(LocalDate dataRecebimento) {
        this.setDataRecebimento(dataRecebimento);
        return this;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public BigDecimal getValorRecebido() {
        return this.valorRecebido;
    }

    public ContaRecebimento valorRecebido(BigDecimal valorRecebido) {
        this.setValorRecebido(valorRecebido);
        return this;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public String getObservacoes() {
        return this.observacoes;
    }

    public ContaRecebimento observacoes(String observacoes) {
        this.setObservacoes(observacoes);
        return this;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Periodicidade getPeriodicidade() {
        return this.periodicidade;
    }

    public ContaRecebimento periodicidade(Periodicidade periodicidade) {
        this.setPeriodicidade(periodicidade);
        return this;
    }

    public void setPeriodicidade(Periodicidade periodicidade) {
        this.periodicidade = periodicidade;
    }

    public Pagador getPagador() {
        return this.pagador;
    }

    public void setPagador(Pagador pagador) {
        this.pagador = pagador;
    }

    public ContaRecebimento pagador(Pagador pagador) {
        this.setPagador(pagador);
        return this;
    }

    public CategoriaRecebimento getCategoriaRecebimento() {
        return this.categoriaRecebimento;
    }

    public void setCategoriaRecebimento(CategoriaRecebimento categoriaRecebimento) {
        this.categoriaRecebimento = categoriaRecebimento;
    }

    public ContaRecebimento categoriaRecebimento(CategoriaRecebimento categoriaRecebimento) {
        this.setCategoriaRecebimento(categoriaRecebimento);
        return this;
    }

    public Caixa getCaixa() {
        return this.caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public ContaRecebimento caixa(Caixa caixa) {
        this.setCaixa(caixa);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContaRecebimento)) {
            return false;
        }
        return id != null && id.equals(((ContaRecebimento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContaRecebimento{" +
            "id=" + getId() +
            ", dataVencimento='" + getDataVencimento() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", valor=" + getValor() +
            ", dataRecebimento='" + getDataRecebimento() + "'" +
            ", valorRecebido=" + getValorRecebido() +
            ", observacoes='" + getObservacoes() + "'" +
            ", periodicidade='" + getPeriodicidade() + "'" +
            "}";
    }
}
