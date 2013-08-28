package br.com.rtools.homologacao;

import br.com.rtools.pessoa.Filial;
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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="HOM_CANCELAR_HORARIO")
@NamedQuery(name="CancelarHorario.pesquisaID", query="select ch from CancelarHorario ch where ch.id = :pid")
public class CancelarHorario implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_HORARIOS", referencedColumnName="ID")
    @ManyToOne (fetch=FetchType.EAGER)
    private Horarios horarios;
    @JoinColumn(name="ID_FILIAL", referencedColumnName="ID")
    @ManyToOne (fetch=FetchType.EAGER)
    private Filial filial;
    @Temporal(TemporalType.DATE)
    @Column(name="DT_DATA")
    private Date dtData;
    @Column(name="NR_QUANTIDADE")
    private int quantidade;

    public CancelarHorario() {
        this.id = -1;
        this.horarios = new Horarios();
        this.filial = new Filial();
        this.setData("");
        this.quantidade = 0;
   }

    public CancelarHorario(int idI, Horarios horarios1, Filial filial1, String dataString, int quantidadeI) {
        this.id = idI;
        this.horarios = horarios1;
        this.filial = filial1;
        this.setData(dataString);
        this.quantidade = quantidadeI;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Horarios getHorarios() {
        return horarios;
    }

    public void setHorarios(Horarios horarios1) {
        this.horarios = horarios1;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Date getDtData() {
        return dtData;
    }

    public void setDtData(Date dtData) {
        this.dtData = dtData;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getData() {
        if (dtData != null)
            return DataHoje.converteData(dtData);
        else
            return "";
    }

    public void setData(String data) {
        if (!(data.isEmpty()))
            this.dtData = DataHoje.converte(data);
    }
}