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
@Table(name="SOC_SOCIOS")
@NamedQuery(name="Socios.pesquisaID", query="select s from Socios s where s.id=:pid")
public class Socios implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_MATRICULA_SOCIOS", referencedColumnName="ID",  nullable=true)
    @ManyToOne (fetch=FetchType.EAGER)
    private MatriculaSocios matriculaSocios;
    @JoinColumn(name="ID_SERVICO_PESSOA", referencedColumnName="ID",  nullable=true)
    @ManyToOne (fetch=FetchType.EAGER)
    private ServicoPessoa servicoPessoa;
    @JoinColumn(name="ID_PARENTESCO", referencedColumnName="ID",  nullable=true)
    @ManyToOne (fetch=FetchType.EAGER)
    private Parentesco parentesco;
    @Column(name="NR_VIA_CARTEIRINHA", length=10,nullable=true)
    private int nrViaCarteirinha;
    @Temporal(TemporalType.DATE)
    @Column(name="DT_VALIDADE_CARTEIRINHA")
    private Date dtValidadeCarteirinha;

    public Socios() {
        this.id = -1;
        this.matriculaSocios = new MatriculaSocios();
        this.servicoPessoa = new ServicoPessoa();
        this.parentesco = new Parentesco();
        this.nrViaCarteirinha = 0;
        this.dtValidadeCarteirinha = null;
    }

    public Socios(int id, MatriculaSocios matriculaSocios, ServicoPessoa servicoPessoa, Parentesco parentesco, int nrViaCarteirinha, Date dtValidadeCarteirinha) {
        this.id = id;
        this.matriculaSocios = matriculaSocios;
        this.servicoPessoa = servicoPessoa;
        this.parentesco = parentesco;
        this.nrViaCarteirinha = nrViaCarteirinha;
        this.dtValidadeCarteirinha = dtValidadeCarteirinha;
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

    public Date getDtValidadeCarteirinha() {
        return dtValidadeCarteirinha;
    }

    public void setDtValidadeCarteirinha(Date dtValidadeCarteirinha) {
        this.dtValidadeCarteirinha = dtValidadeCarteirinha;
    }

    public String getValidadeCarteirinha(){
        if (dtValidadeCarteirinha != null)
            return DataHoje.converteData(dtValidadeCarteirinha);
        else
            return "";
    }

    public void setValidadeCarteirinha(String validadeCarteirinha){
        if (!(validadeCarteirinha.isEmpty()))
            this.dtValidadeCarteirinha = DataHoje.converte(validadeCarteirinha);
    }
}