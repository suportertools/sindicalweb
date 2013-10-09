package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.pessoa.Porte;
import javax.persistence.*;

@Entity
@Table(name = "ARR_FAIXA_FATURAMENTO")
@NamedQuery(name = "ServicoTipoEmpresa.pesquisaID", query = "select c from ServicoTipoEmpresa c where c.id = :pid")
public class ServicoTipoEmpresa implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_SERVICOS", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "ID_PORTE", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Porte porte;
    @Column(name = "NR_VALOR", nullable = true)
    private float valor;
    @Column(name = "DS_REF_INICIAL", length = 7, nullable = true)
    private String referenciaInicial;
    @Column(name = "DS_REF_FINAL", length = 7, nullable = true)
    private String referenciaFinal;

    public ServicoTipoEmpresa() {
        this.id = -1;
        this.servicos = new Servicos();
        this.porte = new Porte();
        this.valor = 0;
        this.referenciaInicial = "";
        this.referenciaFinal = "";
    }

    public ServicoTipoEmpresa(int id, Servicos servicos, Porte porte, float valor, String referenciaInicial, String referenciaFinal) {
        this.id = id;
        this.servicos = servicos;
        this.porte = porte;
        this.valor = valor;
        this.referenciaInicial = referenciaInicial;
        this.referenciaFinal = referenciaFinal;
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

    public Porte getPorte() {
        return porte;
    }

    public void setPorte(Porte porte) {
        this.porte = porte;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getReferenciaInicial() {
        return referenciaInicial;
    }

    public void setReferenciaInicial(String referenciaInicial) {
        this.referenciaInicial = referenciaInicial;
    }

    public String getReferenciaFinal() {
        return referenciaFinal;
    }

    public void setReferenciaFinal(String referenciaFinal) {
        this.referenciaFinal = referenciaFinal;
    }
}