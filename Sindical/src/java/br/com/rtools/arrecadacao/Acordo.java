package br.com.rtools.arrecadacao;

import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_acordo")
@NamedQuery(name = "Acordo.pesquisaID", query = "select c from Acordo c where c.id = :pid")
public class Acordo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @Column(name = "ds_contato", length = 200)
    private String contato;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date dtData;
    @Column(name = "ds_email", length = 500)
    private String email;

    public Acordo() {
        this.id = -1;
        this.usuario = new Usuario();
        this.contato = "";
        this.dtData = DataHoje.dataHoje();
        this.email = "";
    }

    public Acordo(int id, Usuario usuario, String contato, String data, String email) {
        this.id = id;
        this.usuario = usuario;
        this.contato = contato;
        setData(data);
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
