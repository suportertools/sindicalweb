package br.com.rtools.escola;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

/**
 * <p>
 * <b>Turma</b></p>
 * <p>
 * Turmas para escola e predefinições sobre o curso no ato da matrícula. As
 * turmas devem obrigatoriamente pertecerem a uma filial, não será possível
 * realizar o cadastramento sem a filial.</p>
 * <p>
 * Não poderá haver há mesma turma para o mesma sala no mesmo período e
 * horários, exceto que os horários sejam diferentes.</p>
 * <p>
 * Para realizar o cadastro de curso acessar Menu Principal > Menu Financeiro >
 * Serviços</p>
 *
 * @author rtools
 */
@Entity
@Table(name = "esc_turma")
@NamedQueries({
    @NamedQuery(name = "Turma.pesquisaID", query = "SELECT T FROM Turma AS T WHERE T.id = :pid"),
    @NamedQuery(name = "Turma.findAll", query = "SELECT T FROM Turma AS T ORDER BY T.cursos.descricao ASC, T.dtInicio DESC, T.horaInicio ASC "),
    @NamedQuery(name = "Turma.findName", query = "SELECT T FROM Turma AS T WHERE UPPER(T.cursos.descricao) LIKE :pdescricao ORDER BY T.cursos.descricao ASC, T.dtInicio DESC, T.horaInicio ASC ")
})
public class Turma implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_curso", referencedColumnName = "id")
    @ManyToOne
    private Servicos cursos;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_inicio")
    private Date dtInicio;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_termino")
    private Date dtTermino;
    @Column(name = "tm_inicio")
    private String horaInicio;
    @Column(name = "tm_termino")
    private String horaTermino;
    @Column(name = "is_segunda", columnDefinition = "boolean default false")
    private boolean segunda;
    @Column(name = "is_terca", columnDefinition = "boolean default false")
    private boolean terca;
    @Column(name = "is_quarta", columnDefinition = "boolean default false")
    private boolean quarta;
    @Column(name = "is_quinta", columnDefinition = "boolean default false")
    private boolean quinta;
    @Column(name = "is_sexta", columnDefinition = "boolean default false")
    private boolean sexta;
    @Column(name = "is_sabado", columnDefinition = "boolean default false")
    private boolean sabado;
    @Column(name = "is_domingo", columnDefinition = "boolean default false")
    private boolean domingo;
    @JoinColumn(name = "id_filial", referencedColumnName = "id")
    @ManyToOne
    private Filial filial;
    @Column(name = "nr_quantidade")
    private int quantidade;
    @Column(name = "nr_sala", length = 2)
    private String sala;
    @Column(name = "ds_descricao", length = 255)
    private String descricao;
    @Column(name = "nr_idade_ini")
    private int idadeInicial;
    @Column(name = "nr_idade_fim")
    private int idadeFim;

    public Turma(int id, Servicos cursos, String dataInicio, String dataTermino, String horaInicio, String horaTermino,
            boolean segunda, boolean terca, boolean quarta, boolean quinta, boolean sexta, boolean sabado, boolean domingo, Filial filial,
            int quantidade, String sala, String descricao, int idadeInicial, int idadeFim) {
        this.id = id;
        this.cursos = cursos;
        this.dtInicio = DataHoje.converte(dataInicio);
        this.dtTermino = DataHoje.converte(dataTermino);
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
        this.dtInicio = DataHoje.dataHoje();
        this.dtTermino = null;
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
        this.idadeInicial = 0;
        this.idadeFim = 120;
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
