package br.com.rtools.associativo;

import br.com.rtools.financeiro.Movimento;
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
@Table(name = "soc_historico_carteirinha")
@NamedQuery(name = "HistoricoCarteirinha.pesquisaID", query = "select hc from HistoricoCarteirinha hc where hc.id=:pid")
public class HistoricoCarteirinha implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dtEmissao;
    @Column(name = "ds_hora")
    private String hora;
    @Column(name = "ds_descricao")
    private String descricao;
    @JoinColumn(name = "id_carteirinha", referencedColumnName = "id")
    @ManyToOne
    private SocioCarteirinha carteirinha;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id")
    @ManyToOne
    private Movimento movimento;
    
    public HistoricoCarteirinha() {
        this.id = -1;
        this.setEmissao(DataHoje.data());
        this.hora = DataHoje.horaMinuto();
        this.descricao = "";
        this.carteirinha = new SocioCarteirinha();
        this.movimento = null;
    }
    
    public HistoricoCarteirinha(int id, String emissao, String hora, String descricao, SocioCarteirinha carteirinha, Movimento movimento) {
        this.id = id;
        this.setEmissao(emissao);
        this.hora = hora;
        this.descricao = descricao;
        this.carteirinha = carteirinha;
        this.movimento = movimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }
    
    public String getEmissao() {
        if (dtEmissao != null) {
            return DataHoje.converteData(dtEmissao);
        } else {
            return "";
        }
    }

    public void setEmissao(String emissao) {
        if (!(emissao.isEmpty())) {
            this.dtEmissao = DataHoje.converte(emissao);
        }
    }    

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public SocioCarteirinha getCarteirinha() {
        return carteirinha;
    }

    public void setCarteirinha(SocioCarteirinha carteirinha) {
        this.carteirinha = carteirinha;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }
}
