package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="PES_ENVIO_EMAILS")
@NamedQuery(name="EnvioEmails.pesquisaID", query="select ee from EnvioEmails ee where ee.id=:pid")
public class EnvioEmails implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name="DT_DATA_ENVIO")
    private Date dtEnvio;
    @JoinColumn(name="ID_PESSOA", referencedColumnName="ID", nullable=false)
    @OneToOne
    private Pessoa pessoa;
    @Column(name="DS_EMAIL", length=50)
    private String email;
    @Column(name="DS_HISTORICO", length=100)
    private String historico;
    @Column(name="DS_OPERACAO", length=20)
    private String operacao;


    public EnvioEmails() {
        this.id = -1;
        this.dtEnvio = DataHoje.dataHoje();
        this.pessoa = new Pessoa();
        this.email = "";
        this.historico = "";
        this.operacao = "";
    }
    
    public EnvioEmails(int id, Pessoa pessoa, String email, String historico, String operacao) {
        this.id = id;
        this.pessoa = pessoa;
        this.email = email;
        this.historico = historico;
        this.operacao = operacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtEnvio() {
        return dtEnvio;
    }

    public void setDtEnvio(Date dtEnvio) {
        this.dtEnvio = dtEnvio;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getEnvio() {
        if (getDtEnvio() != null)
            return DataHoje.converteData(getDtEnvio());
        else
            return "";
    }

    public void setEnvio(String envio) {
        if (!(envio.isEmpty()))
            this.setDtEnvio(DataHoje.converte(envio));
    }
}
