package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_plano5")
@NamedQuery(name = "Plano5.pesquisaID", query = "select p from Plano5 p where p.id=:pid")
public class Plano5 implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_plano4", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Plano4 plano4;
    @Column(name = "ds_numero", length = 100, nullable = false)
    private String numero;
    @Column(name = "ds_conta", length = 200, nullable = false)
    private String conta;
    @JoinColumn(name = "id_conta_banco", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private ContaBanco contaBanco;
    @JoinColumn(name = "id_plano5_contra_partida", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Plano5 plano5ContraPartida;
    @Column(name = "ds_acesso", length = 10)
    private String acesso;
    @Column(name = "ds_classificador", length = 20)
    private String classificador;

    public Plano5() {
        this.id = -1;
        this.plano4 = new Plano4();
        this.numero = "";
        this.conta = "";
        this.contaBanco = new ContaBanco();
        this.plano5ContraPartida = null;
        this.acesso = "";
        this.classificador = "";
    }

    public Plano5(int id, String numero, String conta, Plano4 plano4, ContaBanco contaBanco, Plano5 plano5ContraPartida, String acesso, String classificador) {
        this.id = id;
        this.plano4 = new Plano4();
        this.numero = numero;
        this.conta = conta;
        this.contaBanco = contaBanco;
        this.plano5ContraPartida = plano5ContraPartida;
        this.acesso = acesso;
        this.classificador = classificador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plano4 getPlano4() {
        return plano4;
    }

    public void setPlano4(Plano4 plano4) {
        this.plano4 = plano4;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public ContaBanco getContaBanco() {
        return contaBanco;
    }

    public void setContaBanco(ContaBanco contaBanco) {
        this.contaBanco = contaBanco;
    }

    public Plano5 getPlano5ContraPartida() {
        return plano5ContraPartida;
    }

    public void setPlano5ContraPartida(Plano5 plano5ContraPartida) {
        this.plano5ContraPartida = plano5ContraPartida;
    }

    public String getAcesso() {
        return acesso;
    }

    public void setAcesso(String acesso) {
        this.acesso = acesso;
    }

    public String getClassificador() {
        return classificador;
    }

    public void setClassificador(String classificador) {
        this.classificador = classificador;
    }
}
