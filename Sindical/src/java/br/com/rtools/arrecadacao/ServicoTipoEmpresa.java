package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.pessoa.Porte;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "arr_faixa_faturamento")
@NamedQuery(name = "ServicoTipoEmpresa.pesquisaID", query = "select c from ServicoTipoEmpresa c where c.id = :pid")
public class ServicoTipoEmpresa implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servicos", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "id_porte", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Porte porte;
    @Column(name = "nr_valor", nullable = true)
    private float valor;
    @Column(name = "ds_ref_inicial", length = 7, nullable = true)
    private String referenciaInicial;
    @Column(name = "ds_ref_final", length = 7, nullable = true)
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
