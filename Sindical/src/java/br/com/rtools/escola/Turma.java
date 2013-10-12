package br.com.rtools.escola;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "ESC_TURMA")
@NamedQueries({
    @NamedQuery(name = "Turma.pesquisaID",    query = "SELECT T FROM Turma AS T WHERE T.id = :pid"),
    @NamedQuery(name = "Turma.findAll",       query = "SELECT T FROM Turma AS T ORDER BY T.cursos.descricao ASC")
})
public class Turma implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_CURSO", referencedColumnName = "ID")
    @ManyToOne
    private Servicos cursos;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_INICIO")
    private Date dtInicio;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_TERMINO")
    private Date dtTermino;
    @Column(name = "TM_INICIO")
    private String horaInicio;
    @Column(name = "TM_TERMINO")
    private String horaTermino;
    @Column(name = "IS_SEGUNDA", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean segunda;
    @Column(name = "IS_TERCA", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean terca;
    @Column(name = "IS_QUARTA", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean quarta;
    @Column(name = "IS_QUINTA", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean quinta;
    @Column(name = "IS_SEXTA", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean sexta;
    @Column(name = "IS_SABADO", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean sabado;
    @Column(name = "IS_DOMINGO", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean domingo;
    @JoinColumn(name = "ID_FILIAL", referencedColumnName = "ID")
    @ManyToOne
    private Filial filial;

    public Turma(int id, Servicos cursos, String dataInicio, String dataTermino, String horaInicio, String horaTermino,
            boolean segunda, boolean terca, boolean quarta, boolean quinta, boolean sexta, boolean sabado, boolean domingo, Filial filial) {
        this.id = id;
        this.cursos = cursos;
        setDataInicio(dataInicio);
        setDataTermino(dataTermino);
        this.horaInicio = horaInicio;
        this.horaTermino = horaTermino;
        this.segunda = segunda;
        this.terca = terca;
        this.quarta = quarta;
        this.quinta = quinta;
        this.sexta = sexta;
        this.sabado = sabado;
        this.domingo = domingo;
        this.filial = filial;
    }

    public Turma() {
        this.id = -1;
        this.cursos = new Servicos();
        setDataInicio("");
        setDataTermino("");
        this.horaInicio = "";
        this.horaTermino = "";
        this.segunda = false;
        this.terca = false;
        this.quarta = false;
        this.quinta = false;
        this.sexta = false;
        this.sabado = false;
        this.domingo = false;
        this.filial = new Filial();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Servicos getCursos() {
        return cursos;
    }

    public void setCursos(Servicos cursos) {
        this.cursos = cursos;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtTermino() {
        return dtTermino;
    }

    public void setDtTermino(Date dtTermino) {
        this.dtTermino = dtTermino;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
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

    public String getDataInicio() {
        if (getDtInicio() != null) {
            return DataHoje.converteData(getDtInicio());
        } else {
            return "";
        }
    }

    public void setDataInicio(String dataInicio) {
        if (!(dataInicio.isEmpty())) {
            this.setDtInicio(DataHoje.converte(dataInicio));
        }
    }

    public String getDataTermino() {
        if (getDtTermino() != null) {
            return DataHoje.converteData(getDtTermino());
        } else {
            return "";
        }
    }

    public void setDataTermino(String dataTermino) {
        if (!(dataTermino.isEmpty())) {
            this.setDtTermino(DataHoje.converte(dataTermino));
        }
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }
}
