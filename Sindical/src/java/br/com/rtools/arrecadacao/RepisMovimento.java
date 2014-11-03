package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_repis_movimento")
@NamedQuery(name = "RepisMovimento.pesquisaID", query = "select m from RepisMovimento m where m.id = :pid")
public class RepisMovimento implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dataEmissao;
    @Column(name = "ds_solicitante", length = 100, nullable = true)
    private String contato;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Pessoa pessoa;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_resposta")
    private Date dataResposta;
    @Column(name = "nr_ano", length = 4, nullable = false)
    private int ano;
    @JoinColumn(name = "id_repis_status", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private RepisStatus repisStatus;
    @JoinColumn(name = "id_patronal", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Patronal patronal;
    @JoinColumn(name = "id_certidao_tipo", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private CertidaoTipo certidaoTipo;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_impressao")
    private Date dataImpressao;
    
    public RepisMovimento() {
        this.id = -1;
        this.dataEmissao = DataHoje.dataHoje();
        this.contato = "";
        this.pessoa = new Pessoa();
        this.dataResposta = new Date();
        this.ano = 0;
        this.repisStatus = new RepisStatus();
        this.patronal = new Patronal();
        this.certidaoTipo = new CertidaoTipo();
        this.dataImpressao = null;
    }

    public RepisMovimento(int id, String dataEmissaoString, String contato, Pessoa pessoa, String dataRespostaString, int ano, RepisStatus repisStatus, Patronal patronal, CertidaoTipo certidaoTipo, String dataImpressaoString) {
        this.id = id;
        setDataEmissaoString(dataEmissaoString);
        this.contato = contato;
        this.pessoa = pessoa;
        setDataRespostaString(dataRespostaString);
        this.ano = ano;
        this.repisStatus = repisStatus;
        this.patronal = patronal;
        this.certidaoTipo = certidaoTipo;
        setDataImpressaoString(dataRespostaString);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDataEmissaoString() {
        return DataHoje.converteData(dataEmissao);
    }

    public void setDataEmissaoString(String dataEmissaoString) {
        this.dataEmissao = DataHoje.converte(dataEmissaoString);
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getDataResposta() {
        return dataResposta;
    }

    public void setDataResposta(Date dataResposta) {
        this.dataResposta = dataResposta;
    }

    public String getDataRespostaString() {
        return DataHoje.converteData(dataResposta);
    }

    public void setDataRespostaString(String dataRespostaString) {
        this.dataResposta = DataHoje.converte(dataRespostaString);
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public RepisStatus getRepisStatus() {
        return repisStatus;
    }

    public void setRepisStatus(RepisStatus repisStatus) {
        this.repisStatus = repisStatus;
    }

    public Patronal getPatronal() {
        return patronal;
    }

    public void setPatronal(Patronal patronal) {
        this.patronal = patronal;
    }

    public CertidaoTipo getCertidaoTipo() {
        return certidaoTipo;
    }

    public void setCertidaoTipo(CertidaoTipo certidaoTipo) {
        this.certidaoTipo = certidaoTipo;
    }

    public Date getDataImpressao() {
        return dataImpressao;
    }

    public void setDataImpressao(Date dataImpressao) {
        this.dataImpressao = dataImpressao;
    }
    
    public String getDataImpressaoString() {
        return DataHoje.converteData(dataImpressao);
    }

    public void setDataImpressaoString(String dataImpressaoString) {
        this.dataImpressao = DataHoje.converte(dataImpressaoString);
    }
}
