package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_desconto_servico_empresa")
@NamedQuery(name = "DescontoServicoEmpresa.pesquisaID", query = "SELECT DSEM FROM DescontoServicoEmpresa AS DSEM WHERE DSEM.id=:pid")
public class DescontoServicoEmpresa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_juridica", referencedColumnName = "id")
    @ManyToOne
    private Juridica juridica;
    @JoinColumn(name = "id_servico", referencedColumnName = "id")
    @ManyToOne
    private Servicos servicos;
    @Column(name = "nr_desconto")
    private float desconto;

    public DescontoServicoEmpresa() {
        this.id = -1;
        this.juridica = new Juridica();
        this.servicos = new Servicos();
        this.desconto = 0;
    }

    public DescontoServicoEmpresa(int id, Juridica juridica, Servicos servicos, float desconto) {
        this.id = id;
        this.juridica = juridica;
        this.servicos = servicos;
        this.desconto = desconto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    public String getDescontoString() {
        return Moeda.converteR$Float(desconto);
    }

    public void setDescontoString(String desconto) {
        this.desconto = Float.parseFloat(Moeda.substituiVirgula(desconto));
    }
}
