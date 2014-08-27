package br.com.rtools.escola;

import br.com.rtools.financeiro.Servicos;
import javax.persistence.*;

@Entity
@Table(name = "matr_contrato_servico")
@NamedQuery(name = "MatriculaContratoServico.pesquisaID", query = "select mcs from MatriculaContratoServico mcs where mcs.id=:pid")
public class MatriculaContratoServico implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id")
    @ManyToOne
    private MatriculaContrato contrato;
    @JoinColumn(name = "id_servico", referencedColumnName = "id")
    @ManyToOne
    private Servicos servicos;

    public MatriculaContratoServico() {
        this.id = -1;
        this.contrato = new MatriculaContrato();
        this.servicos = new Servicos();
    }

    public MatriculaContratoServico(int id, MatriculaContrato contrato, Servicos servicos) {
        this.id = id;
        this.contrato = contrato;
        this.servicos = servicos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MatriculaContrato getContrato() {
        return contrato;
    }

    public void setContrato(MatriculaContrato contrato) {
        this.contrato = contrato;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServico(Servicos servicos) {
        this.servicos = servicos;
    }
}
