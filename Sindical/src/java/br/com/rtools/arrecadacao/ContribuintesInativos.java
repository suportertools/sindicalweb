package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ARR_CONTRIBUINTES_INATIVOS")
@NamedQuery(name = "ContribuintesInativos.pesquisaID", query = "select ci from ContribuintesInativos ci where ci.id=:pid")
public class ContribuintesInativos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_JURIDICA", referencedColumnName = "ID", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Juridica juridica;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_ATIVACAO")
    private Date dtAtivacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_INATIVACAO")
    private Date dtInativacao;
    @JoinColumn(name = "ID_MOTIVO_INATIVACAO", referencedColumnName = "ID", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private MotivoInativacao motivoInativacao;
    @Column(name = "DS_SOLICITANTE")
    private String solicitante;
    @Column(name = "DS_OBS")
    private String observacao;

    public ContribuintesInativos() {
        this.id = -1;
        this.juridica = new Juridica();
        setAtivacao("");
        setInativacao(DataHoje.data());
        this.motivoInativacao = new MotivoInativacao();
        this.solicitante = "";
        this.observacao = "";
    }

    public ContribuintesInativos(int id, Juridica juridica, String ativacao, String inativacao, MotivoInativacao motivoInativacao, String solicitante, String observacao) {
        this.id = id;
        this.juridica = juridica;
        setAtivacao(ativacao);
        setInativacao(inativacao);
        this.motivoInativacao = motivoInativacao;
        this.solicitante = "";
        this.observacao = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Date getDtAtivacao() {
        return dtAtivacao;
    }

    public void setDtAtivacao(Date dtAtivacao) {
        this.dtAtivacao = dtAtivacao;
    }

    public String getAtivacao() {
        return DataHoje.converteData(dtAtivacao);
    }

    public void setAtivacao(String ativacao) {
        this.dtAtivacao = DataHoje.converte(ativacao);
    }

    public Date getDtInativacao() {
        return dtInativacao;
    }

    public void setDtInativacao(Date dtInativacao) {
        this.dtInativacao = dtInativacao;
    }

    public String getInativacao() {
        String a = DataHoje.converteData(dtInativacao);
        return a;
    }

    public void setInativacao(String inativacao) {
        Date a = DataHoje.converte(inativacao);
        this.dtInativacao = a;
    }

    public MotivoInativacao getMotivoInativacao() {
        return motivoInativacao;
    }

    public void setMotivoInativacao(MotivoInativacao motivoInativacao) {
        this.motivoInativacao = motivoInativacao;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
