package br.com.rtools.seguranca;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="SEG_LOG")
@NamedQuery(name="Log.pesquisaID", query="select l from Log l where l.id=:pid")
public class Log implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name="DT_DATA")
    private Date dtData;
    @Column(name="DS_HORA", length=50,nullable=false)
    private String hora;
    @JoinColumn(name="ID_USUARIO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name="ID_PERMISSAO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Permissao permissao;

    public Log(){
         this.id = -1;
         setData("");
         this.hora = "";
         this.usuario = new Usuario();
         this.permissao = new Permissao();
    }

    public Log(int id, String data, String hora,Usuario usuario,Permissao permissao){
        this.id = id;
        setData("");
        this.hora = hora;
        this.usuario = usuario;
        this.permissao = permissao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public String getData() {
        if (dtData != null)
            return DataHoje.converteData(dtData);
        else
            return "";
    }

    public void setData(String data) {
        if (!(data.isEmpty()))
        this.dtData = DataHoje.converte(data);
    }
}