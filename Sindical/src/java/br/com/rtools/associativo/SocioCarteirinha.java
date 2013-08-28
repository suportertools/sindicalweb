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
@Table(name="SOC_CARTEIRINHA")
@NamedQuery(name="SocioCarteirinha.pesquisaID", query="select sc from SocioCarteirinha sc where sc.id=:pid")
public class SocioCarteirinha implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_SOCIO", referencedColumnName="ID", nullable=true)
    @ManyToOne
    private Socios socios;
    @Temporal(TemporalType.DATE)
    @Column(name="DT_EMISSAO")
    private Date dtEmissao;

    public SocioCarteirinha(int id, Socios socios, Date dtEmissao) {
        this.id = id;
        this.socios = socios;
        this.dtEmissao = dtEmissao;
    }

    public SocioCarteirinha() {
        this.id = -1;
        this.socios = new Socios();
        this.dtEmissao = null;
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

    public String getEmissao(){
        if (dtEmissao != null)
            return DataHoje.converteData(dtEmissao);
        else
            return "";
    }

    public void setEmissao(String emissao){
        if (!(emissao.isEmpty()))
            this.dtEmissao = DataHoje.converte(emissao);
    }
}
