package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "FIN_SERVICO_VALOR")
@NamedQuery(name = "ServicoValor.pesquisaID", query = "select s from ServicoValor s where s.id=:pid")
public class ServicoValor implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_SERVICO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @Column(name = "NR_IDADE_INI", length = 18, nullable = false)
    private int idadeIni;
    @Column(name = "NR_IDADE_FIM", length = 18, nullable = false)
    private int idadeFim;
    @Column(name = "NR_VALOR", length = 18, nullable = false)
    private float valor;
    @Column(name = "NR_DESCONTO_ATE_VENCIMENTO", length = 18, nullable = false)
    private float descontoAteVenc;
    @Column(name = "NR_TAXA", length = 18, nullable = true)
    private float taxa;

    public ServicoValor() {
        this.id = -1;
        this.servicos = new Servicos();
        this.idadeIni = 0;
        this.idadeFim = 500;
        this.valor = 0;
        this.descontoAteVenc = 0;
        this.taxa = 0;
    }

    public ServicoValor(int id, Servicos servicos, int idadeIni, int idadeFim, float valor, float descontoAteVenc, float taxa) {
        this.id = id;
        this.servicos = servicos;
        this.idadeIni = idadeIni;
        this.idadeFim = idadeFim;
        this.valor = valor;
        this.descontoAteVenc = descontoAteVenc;
        this.taxa = taxa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public int getIdadeIni() {
        return idadeIni;
    }

    public void setIdadeIni(int idadeIni) {
        this.idadeIni = idadeIni;
    }

    public int getIdadeFim() {
        return idadeFim;
    }

    public void setIdadeFim(int idadeFim) {
        this.idadeFim = idadeFim;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getDescontoAteVenc() {
        return descontoAteVenc;
    }

    public void setDescontoAteVenc(float descontoAteVenc) {
        this.descontoAteVenc = descontoAteVenc;
    }

    public float getTaxa() {
        return taxa;
    }

    public void setTaxa(float taxa) {
        this.taxa = taxa;
    }
}
