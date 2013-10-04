package br.com.rtools.financeiro;

import br.com.rtools.endereco.Cidade;
import br.com.rtools.pessoa.Filial;
import javax.persistence.*;

@Entity
@Table(name = "FIN_CONTA_BANCO")
@NamedQuery(name = "ContaBanco.pesquisaID", query = "select o from ContaBanco o where o.id=:pid")
public class ContaBanco implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_BANCO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Banco banco;
    @Column(name = "DS_AGENCIA", length = 100, nullable = false)
    private String agencia;
    @Column(name = "DS_CONTA", length = 100, nullable = false)
    private String conta;
    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Cidade cidade;
    @Column(name = "NR_ULTIMO_CHEQUE", length = 38, nullable = false)
    private int uCheque;
    @JoinColumn(name = "ID_FILIAL", referencedColumnName = "ID", nullable = true)
    @OneToOne
    private Filial filial;

    public ContaBanco() {
        this.id = -1;
        this.banco = new Banco();
        this.agencia = "";
        this.conta = "";
        this.cidade = new Cidade();
        this.uCheque = 0;
        this.filial = new Filial();
    }

    public ContaBanco(int id, Banco banco, String agencia, String conta, Cidade cidade, int uCheque, Filial filial) {
        this.id = id;
        this.banco = banco;
        this.agencia = agencia;
        this.conta = conta;
        this.cidade = cidade;
        this.uCheque = uCheque;
        this.filial = filial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public int getUCheque() {
        return uCheque;
    }

    public void setUCheque(int uCheque) {
        this.uCheque = uCheque;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }
}