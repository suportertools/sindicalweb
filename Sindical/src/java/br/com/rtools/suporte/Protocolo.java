
package br.com.rtools.suporte;

import br.com.rtools.pessoa.Pessoa;
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
@Table(name="PRO_PROTOCOLO")
@NamedQuery(name="Protocolo.pesquisaID", query="select p from Protocolo p where p.id=:pid")

public class Protocolo implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name="DT_DATA")
    private Date data;

    @Column(name="DS_HORA")
    private String hora;

    @Column(name="DS_SOLICITANTE", length=50)
    private String solicitante;

    @JoinColumn(name="ID_PESSOA", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Pessoa empresa;

    public Protocolo(int id, Date data, String hora, String solicitante, Pessoa empresa) {
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.solicitante = solicitante;
        this.empresa = empresa;
    }

    public Protocolo() {
        this.id = -1;
        this.data = new Date();
        this.hora = DataHoje.horaMinuto();
        this.solicitante = "";
        this.empresa = new Pessoa();
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public Pessoa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Pessoa empresa) {
        this.empresa = empresa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDataString(){
        return DataHoje.converteData(data);
    }

    public void setDataString (String data){
        this.data = DataHoje.converte(data);
    }
}
