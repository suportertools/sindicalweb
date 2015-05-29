package br.com.rtools.escola;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.utilitarios.DataHoje;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "esc_matr_individual")
@NamedQuery(name = "MatriculaIndividual.pesquisaID", query = "select m from MatriculaIndividual m where m.id=:pid")
public class MatriculaIndividual implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_matr_escola", referencedColumnName = "id")
    @ManyToOne
    private MatriculaEscola matriculaEscola;
    @JoinColumn(name = "id_curso", referencedColumnName = "id")
    @ManyToOne
    private Servicos curso;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_inicio")
    private Date dataInicio;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_termino")
    private Date dataTermino;
    @Column(name = "tm_inicio", length = 5)
    private String inicio;
    @Column(name = "tm_termino", length = 5)
    private String termino;
    @JoinColumn(name = "id_professor", referencedColumnName = "id")
    @ManyToOne
    private Professor professor;
    @Column(name = "is_seg", columnDefinition = "boolean default false")
    private boolean segunda;
    @Column(name = "is_ter", columnDefinition = "boolean default false")
    private boolean terca;
    @Column(name = "is_qua", columnDefinition = "boolean default false")
    private boolean quarta;
    @Column(name = "is_qui", columnDefinition = "boolean default false")
    private boolean quinta;
    @Column(name = "is_sex", columnDefinition = "boolean default false")
    private boolean sexta;
    @Column(name = "is_sab", columnDefinition = "boolean default false")
    private boolean sabado;
    @Column(name = "is_dom", columnDefinition = "boolean default false")
    private boolean domingo;

    public MatriculaIndividual() {
        id = -1;
        matriculaEscola = new MatriculaEscola();
        curso = new Servicos();
        dataInicio = DataHoje.dataHoje();
        dataTermino = null;
        inicio = "";
        termino = "";
        professor = new Professor();
        segunda = false;
        terca = false;
        quarta = false;
        quinta = false;
        sexta = false;
        sabado = false;
        domingo = false;
    }

    public MatriculaIndividual(int id, MatriculaEscola matriculaEscola, Servicos curso, Date dataInicio, Date dataTermino, String inicio, String termino, Professor professor, boolean segunda, boolean terca, boolean quarta, boolean quinta, boolean sexta, boolean sabado, boolean domingo) {
        this.id = id;
        this.matriculaEscola = matriculaEscola;
        this.curso = curso;
        this.dataInicio = dataInicio;
        this.dataTermino = dataTermino;
        this.inicio = inicio;
        this.termino = termino;
        this.professor = professor;
        this.segunda = segunda;
        this.terca = terca;
        this.quarta = quarta;
        this.quinta = quinta;
        this.sexta = sexta;
        this.sabado = sabado;
        this.domingo = domingo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MatriculaEscola getMatriculaEscola() {
        return matriculaEscola;
    }

    public void setMatriculaEscola(MatriculaEscola matriculaEscola) {
        this.matriculaEscola = matriculaEscola;
    }

    public Servicos getCurso() {
        return curso;
    }

    public void setCurso(Servicos curso) {
        this.curso = curso;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public boolean isSegunda() {
        return segunda;
    }

    public void setSegunda(boolean segunda) {
        this.segunda = segunda;
    }

    public boolean isTerca() {
        return terca;
    }

    public void setTerca(boolean terca) {
        this.terca = terca;
    }

    public boolean isQuarta() {
        return quarta;
    }

    public void setQuarta(boolean quarta) {
        this.quarta = quarta;
    }

    public boolean isQuinta() {
        return quinta;
    }

    public void setQuinta(boolean quinta) {
        this.quinta = quinta;
    }

    public boolean isSexta() {
        return sexta;
    }

    public void setSexta(boolean sexta) {
        this.sexta = sexta;
    }

    public boolean isSabado() {
        return sabado;
    }

    public void setSabado(boolean sabado) {
        this.sabado = sabado;
    }

    public boolean isDomingo() {
        return domingo;
    }

    public void setDomingo(boolean domingo) {
        this.domingo = domingo;
    }

    public void setDataInicioString(String dataInicio) {
        this.dataInicio = DataHoje.converte(dataInicio);
    }

    public String getDataInicioString() {
        return DataHoje.converteData(dataInicio);
    }

    public void setDataTerminoString(String dataTermino) {
        this.dataTermino = DataHoje.converte(dataTermino);
    }

    public String getDataTerminoString() {
        return DataHoje.converteData(dataTermino);
    }

    public void listenerInicio(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataInicio = DataHoje.converte(format.format(event.getObject()));
    }

    public void listenerTermino(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataTermino = DataHoje.converte(format.format(event.getObject()));
    }
}
