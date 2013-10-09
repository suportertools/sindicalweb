package br.com.rtools.arrecadacao;

import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "ARR_ACORDO")
@NamedQuery(name = "Acordo.pesquisaID", query = "select c from Acordo c where c.id = :pid")
public class Acordo implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @Column(name = "DS_CONTATO", length = 200, nullable = true)
    private String contato;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_DATA")
    private Date dtData;

    public Acordo() {
        this.id = -1;
        this.usuario = new Usuario();
        this.contato = "";
        this.dtData = DataHoje.dataHoje();
    }

    public Acordo(int id, Usuario usuario, String contato, String data) {
        this.id = id;
        this.usuario = usuario;
        this.contato = contato;
        setData(data);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public Date getDtData() {
        return dtData;
    }

    public void setDtData(Date dtData) {
        this.dtData = dtData;
    }

    public String getData() {
        return DataHoje.converteData(dtData);
    }

    public void setData(String data) {
        setDtData(DataHoje.converte(data));
    }
}