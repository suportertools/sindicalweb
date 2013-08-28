
package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.pessoa.Juridica;
import javax.persistence.*;

@Entity
@Table(name="ARR_FATURAMENTO_FOLHA_EMPRESA")
@NamedQuery(name="FolhaEmpresa.pesquisaID", query="select c from FolhaEmpresa c where c.id = :pid")
public class FolhaEmpresa implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_JURIDICA", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Juridica juridica;
    @JoinColumn(name="ID_TIPO_SERVICO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private TipoServico tipoServico;
    @Column(name="DS_REFERENCIA", length=7 , nullable=true)
    private String referencia;
    @Column(name="NR_VALOR", nullable=true)
    private float valorMes;
    @Column(name="NR_NUM_FUNCIONARIOS", nullable=true)
    private int numFuncionarios;
    @Column(name="NR_ALTERACOES", nullable=false)
    private int alteracoes;

    public FolhaEmpresa() {
        this.id = -1;
        this.juridica = new Juridica();
        this.tipoServico = new TipoServico();
        this.referencia = "";
        this.valorMes = 0;
        this.numFuncionarios = 0;
        this.alteracoes = 1;
    }
    
    public FolhaEmpresa(int id, Juridica juridica, TipoServico tipoServico, String referencia, float valorMes, int numFuncionarios, int alteracoes) {
        this.id = id;
        this.juridica = juridica;
        this.tipoServico = tipoServico;
        this.referencia = referencia;
        this.valorMes = valorMes;
        this.numFuncionarios = numFuncionarios;
        this.alteracoes = alteracoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public float getValorMes() {
        return valorMes;
    }

    public void setValorMes(float valorMes) {
        this.valorMes = valorMes;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public int getNumFuncionarios() {
        return numFuncionarios;
    }

    public void setNumFuncionarios(int numFuncionarios) {
        this.numFuncionarios = numFuncionarios;
    }

    public int getAlteracoes() {
        return alteracoes;
    }

    public void setAlteracoes(int alteracoes) {
        this.alteracoes = alteracoes;
    }





}