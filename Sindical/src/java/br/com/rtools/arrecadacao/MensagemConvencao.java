package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "arr_mensagem_convencao")
@NamedQuery(name = "MensagemConvencao.pesquisaID", query = "select c from MensagemConvencao c where c.id=:pid")
public class MensagemConvencao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_grupo_cidade", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private GrupoCidade grupoCidade;
    @JoinColumn(name = "id_convencao", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Convencao convencao;
    @JoinColumn(name = "id_servicos", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "id_tipo_servico", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private TipoServico tipoServico;
    @Column(name = "ds_mensagem_contribuite", length = 2000, nullable = false)
    private String mensagemContribuinte;
    @Column(name = "ds_mensagem_compensacao", length = 2000, nullable = false)
    private String mensagemCompensacao;
    @Column(name = "ds_referencia", length = 7, nullable = true)
    private String referencia;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_vencimento")
    private Date dtVencimento;

    public MensagemConvencao() {
        this.id = -1;
        this.grupoCidade = new GrupoCidade();
        this.convencao = new Convencao();
        this.servicos = new Servicos();
        this.tipoServico = new TipoServico();
        this.mensagemContribuinte = "";
        this.mensagemCompensacao = "";
        this.referencia = "";
        this.dtVencimento = new Date();
    }

    public MensagemConvencao(int id, GrupoCidade grupoCidade, Convencao convencao, Servicos servicos, TipoServico tipoServico, String mensagemContribuinte, String mensagemCompensacao, String referencia, Date dtVencimento) {
        this.id = id;
        this.grupoCidade = grupoCidade;
        this.convencao = convencao;
        this.servicos = servicos;
        this.tipoServico = tipoServico;
        this.mensagemContribuinte = mensagemContribuinte;
        this.mensagemCompensacao = mensagemCompensacao;
        this.referencia = referencia;
        this.dtVencimento = dtVencimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GrupoCidade getGrupoCidade() {
        return grupoCidade;
    }

    public void setGrupoCidade(GrupoCidade grupoCidade) {
        this.grupoCidade = grupoCidade;
    }

    public Convencao getConvencao() {
        return convencao;
    }

    public void setConvencao(Convencao convencao) {
        this.convencao = convencao;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getMensagemContribuinte() {
        return mensagemContribuinte;
    }

    public void setMensagemContribuinte(String mensagemContribuinte) {
        this.mensagemContribuinte = mensagemContribuinte;
    }

    public String getMensagemCompensacao() {
        return mensagemCompensacao;
    }

    public void setMensagemCompensacao(String mensagemCompensacao) {
        this.mensagemCompensacao = mensagemCompensacao;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public String getVencimento() {
        return DataHoje.converteData(dtVencimento);
    }

    public void setVencimento(String vencimento) {
        this.dtVencimento = DataHoje.converte(vencimento);
    }
}
