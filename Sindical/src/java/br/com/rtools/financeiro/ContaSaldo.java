package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "fin_conta_saldo")
@NamedQuery(name = "ContaSaldo.pesquisaID", query = "select cs from ContaSaldo cs where cs.id=:pid")
public class ContaSaldo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date dtData;
    @Column(name = "nr_saldo", length = 10)
    private float saldo;
    @JoinColumn(name = "id_plano5", referencedColumnName = "id")
    @OneToOne
    private Plano5 plano5;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @OneToOne
    private Usuario usuario;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Filial filial;
    @JoinColumn(name = "id_caixa", referencedColumnName = "id")
    @OneToOne
    private Caixa caixa;

    public ContaSaldo() {
        this.id = -1;
        this.setData(null);
        this.saldo = 0;
        this.plano5 = new Plano5();
        this.usuario = new Usuario();
        this.filial = new Filial();
        this.caixa = new Caixa();
    }

    public ContaSaldo(int id, Date dtData, float saldo, Plano5 plano5, Usuario usuario, Filial filial, Caixa caixa) {
        this.id = id;
        this.dtData = dtData;
        this.saldo = saldo;
        this.plano5 = plano5;
        this.usuario = usuario;
        this.filial = filial;
        this.caixa = caixa;
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

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public String getData() {
        return DataHoje.converteData(dtData);
    }

    public void setData(String data) {
        this.dtData = DataHoje.converte(data);
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }
}
