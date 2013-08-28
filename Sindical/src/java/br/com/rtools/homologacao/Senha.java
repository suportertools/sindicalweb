package br.com.rtools.homologacao;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Usuario;
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
@Table(name="HOM_SENHA")
@NamedQuery(name="Senha.pesquisaID", query="select s from Senha s where s.id = :pid")
public class Senha implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_AGENDAMENTO", referencedColumnName="ID")
    @ManyToOne (fetch=FetchType.EAGER)
    private Agendamento agendamento;
    @Column(name="DS_HORA", length=5)
    private String hora;    
    @Column(name="DS_HORA_CHAMADA", length=5)
    private String horaChamada;    
    @Column(name="NR_MESA")
    private int mesa;    
    @JoinColumn(name="ID_USUARIO", referencedColumnName="ID")
    @ManyToOne (fetch=FetchType.EAGER)
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    @Column(name="DT_DATA")
    private Date dtData;
    @Column(name="NR_SENHA")
    private int senha;    
    @JoinColumn(name="ID_FILIAL", referencedColumnName="ID")
    @ManyToOne (fetch=FetchType.EAGER)
    private Filial filial;

    public Senha() {
        this.id = -1;
        this.agendamento = new Agendamento();
        this.hora = "";
        this.horaChamada = "";
        this.mesa = 0;
        this.usuario = new Usuario();
        this.setData("");
        this.senha = 0;
        this.filial = new Filial();
    }
    
    public Senha(int id, Agendamento agendamento, String hora, String horaChamada, int mesa, Usuario usuario, String data, int senha, Filial filial) {
        this.id = id;
        this.agendamento = agendamento;
        this.hora = hora;
        this.horaChamada = horaChamada;
        this.mesa = mesa;
        this.usuario = usuario;
        this.setData(data);
        this.senha = senha;
        this.filial = filial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getHoraChamada() {
        return horaChamada;
    }

    public void setHoraChamada(String horaChamada) {
        this.horaChamada = horaChamada;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDtData() {
        return dtData;
    }

    public void setDtData(Date dtData) {
        this.dtData = dtData;
    }

    public int getSenha() {
        return senha;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
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
