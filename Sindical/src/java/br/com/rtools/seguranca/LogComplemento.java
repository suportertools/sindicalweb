package br.com.rtools.seguranca;

import javax.persistence.*;

@Entity
@Table(name = "SEG_LOG_COMPLEMENTO")
@NamedQuery(name = "LogComplemento.pesquisaID", query = "select lc from LogComplemento lc where lc.id=:pid")
public class LogComplemento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_LOG", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Log log;
    @Column(name = "DS_CAMPO", length = 50, nullable = false)
    private String campo;
    @Column(name = "DS_CONTEUDO_ORIGINAL", length = 100, nullable = true)
    private String conteudoOriginal;
    @Column(name = "DS_CONTEUDO_ALTERADO", length = 100, nullable = true)
    private String conteudoAlterado;

    public LogComplemento() {
        this.id = -1;
        this.log = new Log();
        this.campo = "";
        this.conteudoOriginal = "";
        this.conteudoAlterado = "";
    }

    public LogComplemento(int id, Log log, String campo, String conteudoOriginal, String conteudoAlterado) {
        this.id = id;
        this.log = log;
        this.campo = campo;
        this.conteudoOriginal = conteudoOriginal;
        this.conteudoAlterado = conteudoAlterado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getConteudoOriginal() {
        return conteudoOriginal;
    }

    public void setConteudoOriginal(String conteudoOriginal) {
        this.conteudoOriginal = conteudoOriginal;
    }

    public String getConteudoAlterado() {
        return conteudoAlterado;
    }

    public void setConteudoAlterado(String conteudoAlterado) {
        this.conteudoAlterado = conteudoAlterado;
    }
}