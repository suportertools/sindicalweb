package br.com.rtools.associativo;

import br.com.rtools.financeiro.Movimento;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name = "soc_fechamento_repasse")
@Entity
public class FechamentoRepasse implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Rotina rotina;    
    @JoinColumn(name = "id_movimento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Movimento movimento;    
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_inicio")
    private Date dtInicio;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_fechamento")
    private Date dtFechamento;

    public FechamentoRepasse() {
        this.id = -1;
        this.rotina = new Rotina();
        this.movimento = new Movimento();
        this.dtInicio = DataHoje.dataHoje();
        this.dtFechamento = DataHoje.dataHoje();
    }
    
    public FechamentoRepasse(int id, Rotina rotina, Movimento movimento, Date dtInicio, Date dtFechamento) {
        this.id = id;
        this.rotina = rotina;
        this.movimento = movimento;
        this.dtInicio = dtInicio;
        this.dtFechamento = dtFechamento;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public String getDtInicioString() {
        return DataHoje.converteData(dtInicio);
    }

    public void setDtInicioString(String dtInicio) {
        this.dtInicio = DataHoje.converte(dtInicio);
    }

    public Date getDtFechamento() {
        return dtFechamento;
    }

    public void setDtFechamento(Date dtFechamento) {
        this.dtFechamento = dtFechamento;
    }
    
    public String getDtFechamentoString() {
        return DataHoje.converteData(dtFechamento);
    }

    public void setDtFechamentoString(String dtFechamento) {
        this.dtFechamento = DataHoje.converte(dtFechamento);
    }    
    
}
