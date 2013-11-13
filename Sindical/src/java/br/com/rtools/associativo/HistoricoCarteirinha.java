package br.com.rtools.associativo;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SOC_HISTORICO_CARTEIRINHA")
@NamedQuery(name = "HistoricoCarteirinha.pesquisaID", query = "select hc from HistoricoCarteirinha hc where hc.id=:pid")
public class HistoricoCarteirinha implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_SOCIO", referencedColumnName = "ID")
    @ManyToOne
    private Socios socios;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_EMISSAO")
    private Date dtEmissao;
    @Column(name = "DS_HORA")
    private String hora;
    @Column(name = "DS_DESCRICAO")
    private String descricao;

    public HistoricoCarteirinha() {
        this.id = -1;
        this.socios = new Socios();
        this.setEmissao(DataHoje.data());
        this.hora = DataHoje.horaMinuto();
        this.descricao = "";
    }
    
    public HistoricoCarteirinha(int id, Socios socios, String emissao, String hora, String descricao) {
        this.id = id;
        this.socios = socios;
        this.setEmissao(emissao);
        this.hora = hora;
        this.descricao = descricao;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socios getSocios() {
        return socios;
    }

    public void setSocios(Socios socios) {
        this.socios = socios;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }
    
    public String getEmissao() {
        if (dtEmissao != null) {
            return DataHoje.converteData(dtEmissao);
        } else {
            return "";
        }
    }

    public void setEmissao(String emissao) {
        if (!(emissao.isEmpty())) {
            this.dtEmissao = DataHoje.converte(emissao);
        }
    }    

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
