package br.com.rtools.associativo;

import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "soc_geracao")
@NamedQuery(name = "Geracao.pesquisaID", query = "select g from Geracao g where g.id=:pid")
public class Geracao implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servico_pessoa", referencedColumnName = "id")
    @ManyToOne
    private ServicoPessoa servicoPessoa;
    @Column(name = "ds_vencimento", length = 7)
    private String vencimento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento")
    private Date dtLancamento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ServicoPessoa getServicoPessoa() {
        return servicoPessoa;
    }

    public void setServicoPessoa(ServicoPessoa servicoPessoa) {
        this.servicoPessoa = servicoPessoa;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public Date getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(Date dtLancamento) {
        this.dtLancamento = dtLancamento;
    }
    
    public String getLancamento() {
        if (dtLancamento != null) {
            return DataHoje.converteData(dtLancamento);
        } else {
            return "";
        }
    }

    public void setLancamento(String lancamento) {
        if (!(lancamento.isEmpty())) {
            this.dtLancamento = DataHoje.converte(lancamento);
        }
    }
    
}
