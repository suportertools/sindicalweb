package br.com.rtools.associativo;

import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "soc_socios")
@NamedQuery(name = "Socios.pesquisaID", query = "select s from Socios s where s.id=:pid")
public class Socios implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_matricula_socios", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private MatriculaSocios matriculaSocios;
    @JoinColumn(name = "id_servico_pessoa", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private ServicoPessoa servicoPessoa;
    @JoinColumn(name = "id_parentesco", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Parentesco parentesco;
    @Column(name = "nr_via_carteirinha", length = 10, nullable = true)
    private int nrViaCarteirinha;

    public Socios() {
        this.id = -1;
        this.matriculaSocios = new MatriculaSocios();
        this.servicoPessoa = new ServicoPessoa();
        this.parentesco = new Parentesco();
        this.nrViaCarteirinha = 0;
    }

    public Socios(int id, MatriculaSocios matriculaSocios, ServicoPessoa servicoPessoa, Parentesco parentesco, int nrViaCarteirinha) {
        this.id = id;
        this.matriculaSocios = matriculaSocios;
        this.servicoPessoa = servicoPessoa;
        this.parentesco = parentesco;
        this.nrViaCarteirinha = nrViaCarteirinha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MatriculaSocios getMatriculaSocios() {
        return matriculaSocios;
    }

    public void setMatriculaSocios(MatriculaSocios matriculaSocios) {
        this.matriculaSocios = matriculaSocios;
    }

    public ServicoPessoa getServicoPessoa() {
        return servicoPessoa;
    }

    public void setServicoPessoa(ServicoPessoa servicoPessoa) {
        this.servicoPessoa = servicoPessoa;
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public int getNrViaCarteirinha() {
        return nrViaCarteirinha;
    }

    public void setNrViaCarteirinha(int nrViaCarteirinha) {
        this.nrViaCarteirinha = nrViaCarteirinha;
    }
}