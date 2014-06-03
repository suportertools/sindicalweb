package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ARR_REPIS_MOVIMENTO")
@NamedQuery(name = "RepisMovimento.pesquisaID", query = "select m from RepisMovimento m where m.id = :pid")
public class RepisMovimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_EMISSAO")
    private Date dataEmissao;
    @Column(name = "DS_SOLICITANTE", length = 100, nullable = true)
    private String contato;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Pessoa pessoa;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_RESPOSTA")
    private Date dataResposta;
    @Column(name = "NR_ANO", length = 4, nullable = false)
    private int ano;
    @JoinColumn(name = "ID_REPIS_STATUS", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private RepisStatus repisStatus;
    @JoinColumn(name = "ID_PATRONAL", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Patronal patronal;

    public RepisMovimento() {
        this.id = -1;
        this.dataEmissao = DataHoje.dataHoje();
        this.contato = "";
        this.pessoa = new Pessoa();
        this.dataResposta = new Date();
        this.ano = 0;
        this.repisStatus = new RepisStatus();
        this.patronal = new Patronal();
    }

    public RepisMovimento(int id, String dataEmissaoString, String contato, Pessoa pessoa, String dataRespostaString, int ano, RepisStatus repisStatus, Patronal patronal) {
        this.id = id;
        setDataEmissaoString(dataEmissaoString);
        this.contato = contato;
        this.pessoa = pessoa;
        setDataRespostaString(dataRespostaString);
        this.ano = ano;
        this.repisStatus = repisStatus;
        this.patronal = patronal;
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
}
