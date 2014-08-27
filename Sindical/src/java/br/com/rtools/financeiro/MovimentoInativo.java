package br.com.rtools.financeiro;

import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "fin_movimento_inativo")
@NamedQuery(name = "MovimentoInativo.pesquisaID", query = "select mi from MovimentoInativo mi where mi.id=:pid")
public class MovimentoInativo implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Movimento movimento;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date dtData;
    @Column(name = "ds_historico", length = 8000)
    private String historico;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        this.dtData = DataHoje.converte(data);
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
}
