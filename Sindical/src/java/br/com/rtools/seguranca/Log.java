package br.com.rtools.seguranca;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "SEG_LOG")
public class Log implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_DATA")
    private Date dtData;
    @Column(name = "DS_HORA", length = 50, nullable = false)
    private String hora;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "ID_ROTINA", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private Rotina rotina;
    @Column(name = "DS_CONTEUDO_ORIGINAL", length = 1024, nullable = true)
    private String conteudoOriginal;
    @Column(name = "DS_CONTEUDO_ALTERADO", length = 1024, nullable = true)
    private String conteudoAlterado;
    @JoinColumn(name = "ID_EVENTO", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private Evento evento;

    public Log() {
        this.id = -1;
        this.dtData = new Date();
        this.hora = DataHoje.livre(new Date(), "HH:mm");
        this.usuario = new Usuario();
        this.rotina = new Rotina();
        this.conteudoOriginal = "";
        this.conteudoAlterado = "";
        this.evento = new Evento();
    }
    
    public Log(Integer id, Date dtData, String hora, Usuario usuario, Rotina rotina, String conteudoOriginal, String conteudoAlterado, Evento evento) {
        this.id = id;
        this.dtData = dtData;
        this.hora = hora;
        this.usuario = usuario;
        this.rotina = rotina;
        this.conteudoOriginal = conteudoOriginal;
        this.conteudoAlterado = conteudoAlterado;
        this.evento = evento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDtData() {
        return dtData;
    }

    public void setDtData(Date dtData) {
        this.dtData = dtData;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
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

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
