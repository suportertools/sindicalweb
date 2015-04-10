package br.com.rtools.financeiro;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "conf_financeiro")
public class ConfiguracaoFinanceiro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "is_transferencia_automatica_caixa", columnDefinition = "boolean default true")
    private boolean transferenciaAutomaticaCaixa;
    @Column(name = "is_caixa_operador", columnDefinition = "boolean default false")
    private boolean caixaOperador;
    @Column(name = "nr_carencia_dias", columnDefinition = "integer default 0")
    private Integer carenciaDias;
    @Column(name = "is_alterar_valor_fechamento", columnDefinition = "boolean default false")
    private boolean alterarValorFechamento;    
    @Column(name = "is_modal_transferencia", columnDefinition = "boolean default false")
    private boolean modalTransferencia;    

    public ConfiguracaoFinanceiro() {
        this.id = -1;
        this.transferenciaAutomaticaCaixa = true;
        this.caixaOperador = false;
        this.carenciaDias = null;
        this.alterarValorFechamento = false;
        this.modalTransferencia = false;
    }
    
    public ConfiguracaoFinanceiro(int id, boolean transferenciaAutomaticaCaixa, boolean caixaOperador, Integer carenciaDias, boolean alterarValorFechamento, boolean modalTransferencia) {
        this.id = id;
        this.transferenciaAutomaticaCaixa = transferenciaAutomaticaCaixa;
        this.caixaOperador = caixaOperador;
        this.carenciaDias = carenciaDias;
        this.alterarValorFechamento = alterarValorFechamento;
        this.modalTransferencia = modalTransferencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTransferenciaAutomaticaCaixa() {
        return transferenciaAutomaticaCaixa;
    }

    public void setTransferenciaAutomaticaCaixa(boolean transferenciaAutomaticaCaixa) {
        this.transferenciaAutomaticaCaixa = transferenciaAutomaticaCaixa;
    }

    public boolean isCaixaOperador() {
        return caixaOperador;
    }

    public void setCaixaOperador(boolean caixaOperador) {
        this.caixaOperador = caixaOperador;
    }
    
    public Integer getCarenciaDias() {
        return carenciaDias;
    }

    public void setCarenciaDias(Integer carenciaDias) {
        if (carenciaDias != null && carenciaDias < 0) {
            carenciaDias = null;
        }
        this.carenciaDias = carenciaDias;
    }
    
    public boolean isAlterarValorFechamento() {
        return alterarValorFechamento;
    }

    public void setAlterarValorFechamento(boolean alterarValorFechamento) {
        this.alterarValorFechamento = alterarValorFechamento;
    }

    public boolean isModalTransferencia() {
        return modalTransferencia;
    }

    public void setModalTransferencia(boolean modalTransferencia) {
        this.modalTransferencia = modalTransferencia;
    }
}
