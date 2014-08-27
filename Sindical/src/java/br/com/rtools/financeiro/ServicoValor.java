package br.com.rtools.financeiro;

import br.com.rtools.utilitarios.Moeda;
import javax.persistence.*;

@Entity
@Table(name = "fin_servico_valor",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id_servico", "nr_idade_ini"}),
            @UniqueConstraint(columnNames = {"id_servico", "nr_idade_fim"})
        }
)
@NamedQuery(name = "ServicoValor.pesquisaID", query = "select s from ServicoValor s where s.id=:pid")
public class ServicoValor implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servico", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @Column(name = "nr_idade_ini", length = 18, nullable = false, columnDefinition = "integer default 0")
    private int idadeIni;
    @Column(name = "nr_idade_fim", length = 18, nullable = false, columnDefinition = "integer default 500")
    private int idadeFim;
    @Column(name = "nr_valor", length = 18, nullable = false, columnDefinition = "double precision default 0")
    private float valor;
    @Column(name = "nr_desconto_ate_vencimento", length = 18, nullable = false, columnDefinition = "double precision default 0")
    private float descontoAteVenc;
    @Column(name = "nr_taxa", length = 18, nullable = true, columnDefinition = "double precision default 0")
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

    public String getValorString() {
        return Moeda.converteR$Float(valor);
    }

    public void setValorString(String valor) {
        this.valor = Moeda.converteUS$(valor);
    }
}
