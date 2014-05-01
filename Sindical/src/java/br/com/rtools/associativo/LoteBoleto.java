package br.com.rtools.associativo;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SOC_LOTE_BOLETO")
@NamedQuery(name = "LoteBoleto.pesquisaID", query = "select lb from LoteBoleto lb where lb.id = :pid")
public class LoteBoleto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_PROCESSAMENTO")
    private Date dtProcessamento;

    public LoteBoleto() {
        this.id = -1;
        this.dtProcessamento = DataHoje.dataHoje();
    }
    
    public LoteBoleto(int id, Date dtProcessamento) {
        this.id = id;
        this.dtProcessamento = dtProcessamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtProcessamento() {
        return dtProcessamento;
    }

    public void setDtProcessamento(Date dtProcessamento) {
        this.dtProcessamento = dtProcessamento;
    }
        
    public String getProcessamento() {
        return DataHoje.converteData(dtProcessamento);
    }

    public void setProcessamento(String processamento) {
        this.dtProcessamento = DataHoje.converte(processamento);
    }
}
