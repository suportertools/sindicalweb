package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
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
import java.io.Serializable;

@Entity
@Table(name = "ARR_OPOSICAO")
@NamedQuery(name = "Oposicao.pesquisaID", query = "select o from Oposicao o where o.id=:pid")
public class Oposicao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_EMISSAO")
    private Date dtEmissao;
    @JoinColumn(name = "ID_JURIDICA", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Juridica juridica;
    @JoinColumn(name = "ID_OPOSICAO_PESSOA", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private OposicaoPessoa oposicaoPessoa;
    @JoinColumn(name = "ID_CONVENCAO_PERIODO", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private ConvencaoPeriodo convencaoPeriodo;
    @Column(name = "DS_OBS", length = 500)
    private String observacao;

    public Oposicao(int id, String emissao, OposicaoPessoa oposicaoPessoa, Juridica juridica, ConvencaoPeriodo convencaoPeriodo, String observacao) {
        this.id = id;
        this.setEmissao(emissao);
        this.oposicaoPessoa = oposicaoPessoa;
        this.juridica = juridica;
        this.convencaoPeriodo = convencaoPeriodo;
        this.observacao = observacao;
    }

    public Oposicao() {
        this.id = -1;
        this.setEmissao(DataHoje.data());
        this.oposicaoPessoa = new OposicaoPessoa();
        this.juridica = new Juridica();
        this.convencaoPeriodo = new ConvencaoPeriodo();
        this.observacao = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getEmissao() {
        return DataHoje.converteData(dtEmissao);
    }

    public void setEmissao(String emissao) {
        this.dtEmissao = DataHoje.converte(emissao);
    }

    public OposicaoPessoa getOposicaoPessoa() {
        return oposicaoPessoa;
    }

    public void setOposicaoPessoa(OposicaoPessoa oposicaoPessoa) {
        this.oposicaoPessoa = oposicaoPessoa;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public ConvencaoPeriodo getConvencaoPeriodo() {
        return convencaoPeriodo;
    }

    public void setConvencaoPeriodo(ConvencaoPeriodo convencaoPeriodo) {
        this.convencaoPeriodo = convencaoPeriodo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
