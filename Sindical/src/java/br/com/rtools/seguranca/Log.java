package br.com.rtools.seguranca;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "seg_log")
public class Log implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date dtData;
    @Column(name = "ds_hora", length = 50, nullable = false)
    private String hora;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Rotina rotina;
    @Column(name = "ds_conteudo_original", length = 1024, nullable = true)
    private String conteudoOriginal;
    @Column(name = "ds_conteudo_alterado", length = 1024, nullable = true)
    private String conteudoAlterado;
    @JoinColumn(name = "id_evento", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Evento evento;
    @Column(name = "ds_tabela", length = 150, nullable = true)
    private String tabela;
    @Column(name = "nr_codigo", nullable = true)
    private Integer codigo;

    public Log() {
        this.id = -1;
        this.dtData = new Date();
        this.hora = DataHoje.livre(new Date(), "HH:mm");
        this.usuario = new Usuario();
        this.rotina = new Rotina();
        this.conteudoOriginal = "";
        this.conteudoAlterado = "";
        this.evento = new Evento();
        this.tabela = null;
        this.codigo = null;
    }

    /**
     *
     * @param id
     * @param dtData
     * @param hora
     * @param usuario
     * @param rotina
     * @param conteudoOriginal
     * @param conteudoAlterado
     * @param evento
     * @param tabela (Nome da tabela principal do log gerado
     * @param codigo (Id da tabela citada)
     */
    public Log(Integer id, Date dtData, String hora, Usuario usuario, Rotina rotina, String conteudoOriginal, String conteudoAlterado, Evento evento, String tabela, Integer codigo) {
        this.id = id;
        this.dtData = dtData;
        this.hora = hora;
        this.usuario = usuario;
        this.rotina = rotina;
        this.conteudoOriginal = conteudoOriginal;
        this.conteudoAlterado = conteudoAlterado;
        this.evento = evento;
        this.tabela = tabela;
        this.codigo = codigo;
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

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
