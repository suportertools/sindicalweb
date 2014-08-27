package br.com.rtools.homologacao;

import br.com.rtools.atendimento.AteMovimento;
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
@Table(name = "hom_senha")
@NamedQuery(name = "Senha.pesquisaID", query = "select s from Senha s where s.id = :pid")
public class Senha implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_agendamento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Agendamento agendamento;
    @Column(name = "ds_hora", length = 5)
    private String hora;
    @Column(name = "ds_hora_chamada", length = 5)
    private String horaChamada;
    @Column(name = "nr_mesa")
    private int mesa;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date dtData;
    @Column(name = "nr_senha")
    private int senha;
    @JoinColumn(name = "id_filial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Filial filial;
    @JoinColumn(name = "id_atendimento", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private AteMovimento ateMovimento;

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
        this.ateMovimento = null;
    }

    public Senha(int id, Agendamento agendamento, String hora, String horaChamada, int mesa, Usuario usuario, String data, int senha, Filial filial, AteMovimento ateMovimento) {
        this.id = id;
        this.agendamento = agendamento;
        this.hora = hora;
        this.horaChamada = horaChamada;
        this.mesa = mesa;
        this.usuario = usuario;
        this.setData(data);
        this.senha = senha;
        this.filial = filial;
        this.ateMovimento = ateMovimento;
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
        if (dtData != null) {
            return DataHoje.converteData(dtData);
        } else {
            return "";
        }
    }

    public void setData(String data) {
        if (!(data.isEmpty())) {
            this.dtData = DataHoje.converte(data);
        }
    }

    public AteMovimento getAteMovimento() {
        return ateMovimento;
    }

    public void setAteMovimento(AteMovimento ateMovimento) {
        this.ateMovimento = ateMovimento;
    }
}
