package br.com.rtools.escola;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "ESC_TURMA")
@NamedQueries({
    @NamedQuery(name = "Turma.pesquisaID",      query = "SELECT T FROM Turma AS T WHERE T.id = :pid"),
    @NamedQuery(name = "Turma.findAll",         query = "SELECT T FROM Turma AS T ORDER BY T.cursos.descricao ASC, T.dtInicio DESC, T.horaInicio ASC "),
    @NamedQuery(name = "Turma.findName",        query = "SELECT T FROM Turma AS T WHERE UPPER(T.cursos.descricao) LIKE :pdescricao ORDER BY T.cursos.descricao ASC, T.dtInicio DESC, T.horaInicio ASC ")
})
public class Turma implements Serializable {

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
    @Column(name = "NR_QUANTIDADE")
    private int quantidade;
    @Column(name = "NR_SALA", length = 2)
    private String sala;
    @Column(name = "DS_DESCRICAO", length = 255)
    private String descricao;
    @Column(name = "NR_IDADE_INI")
    private int idadeInicial;
    @Column(name = "NR_IDADE_FIM")    
    private int idadeFim;

    public Turma(int id, Servicos cursos, String dataInicio, String dataTermino, String horaInicio, String horaTermino,
            boolean segunda, boolean terca, boolean quarta, boolean quinta, boolean sexta, boolean sabado, boolean domingo, Filial filial,
            int quantidade, String sala, String descricao, int idadeInicial, int idadeFim) {
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
        this.quantidade = quantidade;
        this.sala = sala;
        this.descricao = descricao;
        this.idadeInicial = idadeInicial;
        this.idadeFim = idadeFim;
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
        this.quantidade = 0;
        this.sala = "";
        this.descricao = "";
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
    
    public void selecionaDataInicio(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy"); 
        this.dtInicio = DataHoje.converte(format.format(event.getObject()));
    }
    
    public void selecionaDataTermino(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy"); 
        this.dtTermino = DataHoje.converte(format.format(event.getObject()));
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdadeInicial() {
        return idadeInicial;
    }

    public void setIdadeInicial(int idadeInicial) {
        this.idadeInicial = idadeInicial;
    }

    public int getIdadeFim() {
        return idadeFim;
    }

    public void setIdadeFim(int idadeFim) {
        this.idadeFim = idadeFim;
    }
}
