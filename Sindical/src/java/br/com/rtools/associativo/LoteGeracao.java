package br.com.rtools.associativo;

import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "soc_lote_geracao")
@NamedQuery(name = "LoteGeracao.pesquisaID", query = "select lg from LoteGeracao lg where lg.id = :pid")
public class LoteGeracao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_vencimento", length = 7)
    private String referenciaVencimento;
    @JoinColumn(name = "id_servico_pessoa", referencedColumnName = "id")
    @ManyToOne
    private ServicoPessoa servicoPessoa;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento")
    private Date dtLancamento;

    public LoteGeracao() {
        this.id = -1;
        this.referenciaVencimento = "";
        this.servicoPessoa = new ServicoPessoa();
        this.dtLancamento = DataHoje.dataHoje();
    }
    
    public LoteGeracao(int id, String referenciaVencimento, ServicoPessoa servicoPessoa, String lancamento) {
        this.id = id;
        this.referenciaVencimento = referenciaVencimento;
        this.servicoPessoa = servicoPessoa;
        this.setLancamento(lancamento);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferenciaVencimento() {
        return referenciaVencimento;
    }

    public void setReferenciaVencimento(String referenciaVencimento) {
        this.referenciaVencimento = referenciaVencimento;
    }

    public ServicoPessoa getServicoPessoa() {
        return servicoPessoa;
    }

    public void setServicoPessoa(ServicoPessoa servicoPessoa) {
        this.servicoPessoa = servicoPessoa;
    }

    public Date getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(Date dtLancamento) {
        this.dtLancamento = dtLancamento;
    }
    
    public String getLancamento() {
        return DataHoje.converteData(dtLancamento);
    }

    public void setLancamento(String lancamento) {
        this.dtLancamento = DataHoje.converte(lancamento);
    }
}
