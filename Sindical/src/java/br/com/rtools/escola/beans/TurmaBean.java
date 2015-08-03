package br.com.rtools.escola.beans;

import br.com.rtools.escola.dao.TurmaDao;
import br.com.rtools.escola.ComponenteCurricular;
import br.com.rtools.escola.Professor;
import br.com.rtools.escola.Turma;
import br.com.rtools.escola.TurmaProfessor;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.db.FilialDao;
import br.com.rtools.seguranca.FilialRotina;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleAcessoBean;
import br.com.rtools.seguranca.dao.FilialRotinaDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class TurmaBean implements Serializable {

    private Turma turma;
    private String message;
    private int idServicos;
    private List<TurmaProfessor> listTurmaProfessor;
    private List<Turma> listTurma;
    private MacFilial macFilial;
    private Filial filial;
    private String msgStatusFilial;
    private List<Professor> listProfessores;
    private List<ComponenteCurricular> listComponenteCurricular;
    private List<SelectItem> listServicos;
    private List<SelectItem> listFiliais;
    private Date date;
    private Date horaInicio;
    private Date horaTermino;
    private Professor professor;
    private ComponenteCurricular componenteCurricular;
    private Boolean liberaAcessaFilial;
    private Integer filial_id;

    @PostConstruct
    public void init() {
        turma = new Turma();
        message = "";
        idServicos = 0;
        professor = new Professor();
        componenteCurricular = new ComponenteCurricular();
        listTurmaProfessor = new ArrayList<>();
        listTurma = new ArrayList<>();
        listFiliais = new ArrayList<>();
        macFilial = new MacFilial();
        filial = new Filial();
        msgStatusFilial = "";
        listServicos = new ArrayList<>();
        listProfessores = new ArrayList<>();
        listComponenteCurricular = new ArrayList<>();
        date = new Date();
        turma.setDtInicio(DataHoje.dataHoje());
        turma.setDtTermino(DataHoje.dataHoje());
        horaInicio = new Date();
        horaTermino = new Date();
        liberaAcessaFilial = false;
        filial_id = 0;
        loadLiberaAcessaFilial();

    }

    @PreDestroy
    public void destroy() {
        clear();
        GenericaSessao.remove("turmaPesquisa");
    }

    public void loadLiberaAcessaFilial() {
        if (new ControleAcessoBean().permissaoValida("libera_acesso_filiais", 4)) {
            liberaAcessaFilial = true;
        }
    }

    public void clear() {
        GenericaSessao.remove("turmaBean");
    }

    public void save() {
        if (listServicos.isEmpty()) {
            message = "Cadastrar serviços!";
            return;
        }
        if (turma.getFilial().getId() == -1) {
            message = "Informar a filial! Obs: Necessário acessar o sistema usando autênticação.";
            return;
        }
        if (turma.getQuantidade() == 0) {
            message = "Informar a quantidade de vagas!";
            return;
        }
        if (turma.getDataInicio().equals("__:__") || turma.getDataInicio().equals("") || turma.getDataInicio().isEmpty()) {
            message = "Informar a data inicial da turma!";
            return;
        }
        if (turma.getDataTermino().equals("__:__") || turma.getDataTermino().equals("") || turma.getDataTermino().isEmpty()) {
            message = "Informar a data de termino da turma!";
            return;
        }
        turma.setHoraInicio(DataHoje.livre(horaInicio, "HH:mm"));
        turma.setHoraTermino(DataHoje.livre(horaTermino, "HH:mm"));
        if (turma.getId() == -1) {
            int dataInicioInteger = DataHoje.converteDataParaInteger(turma.getDataInicio());
            int dataFinalInteger = DataHoje.converteDataParaInteger(turma.getDataTermino());
            int dataHojeInteger = DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()));
            // IF COMENTADO POR CONTA DE LANÇAMENTO DE TURMA QUE JÁ INICIARAM
//            if (dataInicioInteger < dataHojeInteger) {
//                message = "A data inicial do curso deve ser maior ou igual a data de hoje!";
//                return;
//            }
            if (dataFinalInteger < dataHojeInteger) {
                message = "A data final do curso deve ser maior ou igual a data de hoje!";
                return;
            }
            if (dataFinalInteger < dataInicioInteger) {
                message = "A data final deve ser maior ou igual a data inicial!";
                return;
            }
        }
        if (DataHoje.validaHora(turma.getHoraInicio()).isEmpty()) {
            message = "Hora inicial invalida!";
            return;
        }
        if (DataHoje.validaHora(turma.getHoraTermino()).isEmpty()) {
            message = "Hora final invalida!";
            return;
        }
        if (turma.getHoraInicio().equals("__:__")) {
            turma.setHoraInicio("");
        }
        if (turma.getHoraTermino().equals("__:__")) {
            turma.setHoraTermino("");
        }
        Dao dao = new Dao();
        turma.setCursos((Servicos) dao.find(new Servicos(), Integer.parseInt(listServicos.get(idServicos).getDescription())));
        turma.setFilial((Filial) dao.find(new Filial(), Integer.parseInt(listFiliais.get(filial_id).getDescription())));
        NovoLog novoLog = new NovoLog();
        TurmaDao td = new TurmaDao();
        if (turma.getId() == -1) {
            if (td.existeTurma(turma)) {
                message = "Turma já existe!";
                return;
            }
            dao.openTransaction();
            if (!dao.save(turma)) {
                dao.rollback();
                message = "Erro ao salvar turma!";
                return;
            }
            dao.commit();
            novoLog.setTabela("esc_turma");
            novoLog.setCodigo(turma.getId());
            novoLog.save(
                    "ID: " + turma.getId()
                    + " - Curso: (" + turma.getCursos().getId() + ") " + turma.getCursos().getDescricao()
                    + " - Descrição: " + turma.getDescricao()
                    + " - Período de " + turma.getDataInicio() + " até " + turma.getDataTermino()
                    + " - Horario das " + turma.getHoraInicio() + " às " + turma.getHoraTermino()
                    + " - Vagas: " + turma.getQuantidade()
                    + " - Sala: " + turma.getSala()
                    + " - Filial (" + turma.getFilial().getFilial().getPessoa().getId() + ")"
            );
            message = "Turma salva com sucesso!";
            listTurma.clear();
        } else {
            Turma t = (Turma) dao.find(turma);
            String beforeUpdate
                    = "ID: " + t.getId()
                    + " - Curso: (" + t.getCursos().getId() + ") " + t.getCursos().getDescricao()
                    + " - Descrição: " + t.getDescricao()
                    + " - Período de " + t.getDataInicio() + " até " + t.getDataTermino()
                    + " - Horario das " + t.getHoraInicio() + " às " + t.getHoraTermino()
                    + " - Vagas: " + t.getQuantidade()
                    + " - Sala: " + t.getSala()
                    + " - Filial (" + t.getFilial().getFilial().getPessoa().getId() + ")";
            dao.openTransaction();
            if (!dao.update(turma)) {
                dao.rollback();
                message = "Erro ao atualizar turma!";
                return;
            }
            novoLog.setTabela("esc_turma");
            novoLog.setCodigo(turma.getId());
            novoLog.update(beforeUpdate,
                    "ID: " + turma.getId()
                    + " - Curso: (" + turma.getCursos().getId() + ") " + turma.getCursos().getDescricao()
                    + " - Descrição: " + turma.getDescricao()
                    + " - Período de " + turma.getDataInicio() + " até " + turma.getDataTermino()
                    + " - Horario das " + turma.getHoraInicio() + " às " + turma.getHoraTermino()
                    + " - Vagas: " + turma.getQuantidade()
                    + " - Sala: " + turma.getSala()
                    + " - Filial (" + turma.getFilial().getFilial().getPessoa().getId() + ")"
            );
            dao.commit();
            message = "Turma atualizada com sucesso!";
            listTurma.clear();
        }
    }

    public String edit(Turma t) throws ParseException {
        Dao dao = new Dao();
        Turma turmaC = (Turma) dao.find(t);
        for (int i = 0; i < listServicos.size(); i++) {
            if (Integer.parseInt(listServicos.get(i).getDescription()) == t.getCursos().getId()) {
                idServicos = i;
                break;
            }
        }
        GenericaSessao.put("turmaPesquisa", turmaC);
        GenericaSessao.put("linkClicado", true);
        SimpleDateFormat formatador = new SimpleDateFormat("HH:mm");
        this.horaInicio = formatador.parse(turmaC.getHoraInicio());
        this.horaTermino = formatador.parse(turmaC.getHoraTermino());
        if (GenericaSessao.exists("urlRetorno")) {
            return (String) GenericaSessao.getString("urlRetorno");
        } else {
            return "turma";
        }
    }

    public void delete() {
        Dao dao = new Dao();
        if (turma.getId() != -1) {
            dao.openTransaction();
            for (TurmaProfessor listaTurmaProfessor1 : listTurmaProfessor) {
                if (listaTurmaProfessor1.getId() != -1) {
                    if (!dao.delete((TurmaProfessor) dao.find(listaTurmaProfessor1))) {
                        dao.rollback();
                        message = "Erro ao excluir Professores!";
                        return;
                    }
                }
            }

            if (!dao.delete(turma)) {
                dao.rollback();
                message = "Erro ao excluir Turma!";
                return;
            }
            NovoLog novoLog = new NovoLog();
            novoLog.setTabela("esc_turma");
            novoLog.setCodigo(turma.getId());
            novoLog.delete(
                    "ID: " + turma.getId()
                    + " - Curso: (" + turma.getCursos().getId() + ") " + turma.getCursos().getDescricao()
                    + " - Descrição: " + turma.getDescricao()
                    + " - Período de " + turma.getDataInicio() + " até " + turma.getDataTermino()
                    + " - Horario das " + turma.getHoraInicio() + " às " + turma.getHoraTermino()
                    + " - Vagas: " + turma.getQuantidade()
                    + " - Sala: " + turma.getSala()
                    + " - Filial (" + turma.getFilial().getFilial().getPessoa().getId() + ")"
            );
            dao.commit();
            message = "Cadastro excluído com sucesso!";
            turma = new Turma();
            clear();
        } else {
            message = "Pesquise uma turma para ser excluída!";
        }
    }

    public void removeTurmaProfessor(TurmaProfessor tp) {
        Dao dao = new Dao();
        if (tp.getId() != -1) {
            dao.openTransaction();
            if (!dao.delete(tp)) {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Ao remover este registro!");
            } else {
                NovoLog novoLog = new NovoLog();
                novoLog.setTabela("esc_professor");
                novoLog.setCodigo(tp.getId());
                dao.commit();
                novoLog.delete("Turma Professor - ID: " + tp.getId() + " - Turma: (" + tp.getId() + ") - Professor: (" + tp.getProfessor().getId() + ") " + tp.getProfessor().getProfessor().getNome());
                GenericaMensagem.info("Sucesso", "Professor e Componente Curricular removidos com sucesso");
                listTurmaProfessor.clear();
            }
        }
    }

    public void addTurmaProfessor() {
        message = "";
        TurmaProfessor turmaProfessor = new TurmaProfessor();
        TurmaDao td = new TurmaDao();
        Dao dao = new Dao();
        if (listProfessores.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar professores!");
            return;
        }
        if (listComponenteCurricular.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar componente currícular!");
            return;
        }
        turmaProfessor.setProfessor(professor);
        turmaProfessor.setComponenteCurricular(componenteCurricular);
        turmaProfessor.setTurma(turma);
        if (td.existeTurmaProfessor(turmaProfessor)) {
            GenericaMensagem.warn("Validação", "Cadastro já existe!");
            return;
        }
        dao.openTransaction();
        if (dao.save(turmaProfessor)) {
            GenericaMensagem.info("Sucesso", "Professor e Componente curricular adaocionados");
            dao.commit();
            NovoLog novoLog = new NovoLog();
            novoLog.save("Turma Professor - ID: " + turmaProfessor.getId() + " - Turma: (" + turmaProfessor.getId() + ") - Professor: (" + turmaProfessor.getProfessor().getId() + ") " + turmaProfessor.getProfessor().getProfessor().getNome());
            novoLog.setTabela("esc_professor");
            novoLog.setCodigo(turmaProfessor.getId());
            listTurmaProfessor.clear();
            professor = new Professor();
            componenteCurricular = new ComponenteCurricular();
        } else {
            dao.rollback();
            GenericaMensagem.warn("Erro", "Ao adaocionar este registro!");
        }
    }

    public List<SelectItem> getListServicos() {
        if (listServicos.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            List<Servicos> list = db.listaServicoSituacao(150, "A");
            for (int i = 0; i < list.size(); i++) {
                listServicos.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listServicos;
    }

    public void setListServicos(List<SelectItem> listServicos) {
        this.listServicos = listServicos;
    }

    public List<Professor> getListProfessor() {
        if (listProfessores.isEmpty()) {
            Dao dao = new Dao();
            listProfessores = (List<Professor>) dao.list(new Professor(), true);
            if (!listProfessores.isEmpty()) {
                professor = listProfessores.get(0);
            }
        }
        return listProfessores;
    }

    public void setListProfessor(List<Professor> listProfessores) {
        this.listProfessores = listProfessores;
    }

    public List<ComponenteCurricular> getListComponenteCurricular() {
        if (listComponenteCurricular.isEmpty()) {
            Dao dao = new Dao();
            listComponenteCurricular = (List<ComponenteCurricular>) dao.list(new ComponenteCurricular(), true);
            if (!listComponenteCurricular.isEmpty()) {
                componenteCurricular = listComponenteCurricular.get(0);
            }
        }
        return listComponenteCurricular;
    }

    public void setListComponenteCurricular(List<ComponenteCurricular> listComponenteCurricular) {
        this.listComponenteCurricular = listComponenteCurricular;
    }

    public Turma getTurma() {
        if (GenericaSessao.exists("turmaPesquisa")) {
            turma = (Turma) GenericaSessao.getObject("turmaPesquisa", true);
        }
        if (turma.getFilial().getId() == -1) {
            getMacFilial();
        }
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public List<TurmaProfessor> getListTurmaProfessor() {
        if (listTurmaProfessor.isEmpty()) {
            TurmaDao td = new TurmaDao();
            if (turma.getId() != -1) {
                listTurmaProfessor = td.listaTurmaProfessor(turma.getId());
            }
        }
        return listTurmaProfessor;
    }

    public void setListTurmaProfessor(List<TurmaProfessor> listTurmaProfessor) {
        this.listTurmaProfessor = listTurmaProfessor;
    }

    public List<Turma> getListTurma() {
        if (listTurma.isEmpty()) {
            listTurma = new TurmaDao().findbyFilial(Integer.parseInt(getListFiliais().get(filial_id).getDescription()));
        }
        return listTurma;
    }

    public void setListTurma(List<Turma> listTurma) {
        this.listTurma = listTurma;
    }

    public String getMsgStatusFilial() {
        if (msgStatusFilial.equals("")) {
            getFilial();
        }
        return msgStatusFilial;
    }

    public void setMsgStatusFilial(String msgStatusFilial) {
        this.msgStatusFilial = msgStatusFilial;
    }

    public MacFilial getMacFilial() {
        if (GenericaSessao.exists("acessoFilial")) {
            macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");
            msgStatusFilial = "";
        } else {
            msgStatusFilial = "Informar filial";
        }
        return macFilial;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public String validaHoraInicio() {
        String novaHora;
        if (!turma.getHoraInicio().equals("__:__")) {
            novaHora = DataHoje.validaHora(turma.getHoraInicio());
        } else {
            novaHora = turma.getHoraInicio();
        }
        if (novaHora.equals("")) {
            turma.setHoraInicio("__:__");
        } else {
            turma.setHoraInicio(novaHora);
        }
        validaHorarios();
        return "turma";
    }

    public String validaHoraTermino() {
        String novaHora;
        if (!turma.getHoraTermino().equals("__:__")) {
            novaHora = DataHoje.validaHora(turma.getHoraTermino());
        } else {
            novaHora = turma.getHoraTermino();
        }
        if (novaHora.equals("")) {
            turma.setHoraTermino("__:__");
        } else {
            turma.setHoraTermino(novaHora);
        }
        validaHorarios();
        return "turma";
    }

    public String validaHorarios() {
        if (!turma.getHoraInicio().equals("__:__") && !turma.getHoraTermino().equals("__:__")) {
            int n1a = Integer.parseInt(turma.getHoraInicio().substring(0, 2));
            int n1b = Integer.parseInt(turma.getHoraInicio().substring(3, 4));
            int n2a = Integer.parseInt(turma.getHoraTermino().substring(0, 2));
            int n2b = Integer.parseInt(turma.getHoraTermino().substring(3, 4));
            if (n1a >= n2a) {
                turma.setHoraTermino("__:__");
            }
        }
        return "turma";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(Date horaTermino) {
        this.horaTermino = horaTermino;
    }

    public void dataListener(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.date = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaHoraInicio(SelectEvent event) {
        this.horaInicio = (Date) event.getObject();
    }

    public void selecionaHoraTermino(SelectEvent event) {
        this.horaTermino = (Date) event.getObject();
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public ComponenteCurricular getComponenteCurricular() {
        return componenteCurricular;
    }

    public void setComponenteCurricular(ComponenteCurricular componenteCurricular) {
        this.componenteCurricular = componenteCurricular;
    }

    public List<SelectItem> getListFiliais() {
        if (listFiliais.isEmpty()) {
            Filial f = MacFilial.getAcessoFilial().getFilial();
            if (f.getId() != -1) {
                if (liberaAcessaFilial || Usuario.getUsuario().getId() == 1) {
                    liberaAcessaFilial = true;
                    // ROTINA MATRÍCULA ESCOLA
                    List<FilialRotina> list = new FilialRotinaDao().findByRotina(new Rotina().get().getId());
                    // ID DA FILIAL
                    if (!list.isEmpty()) {
                        for (int i = 0; i < list.size(); i++) {
                            if (i == 0) {
                                filial_id = i;
                            }
                            if (f.getId() == list.get(i).getFilial().getId()) {
                                filial_id = i;
                            }
                            listFiliais.add(new SelectItem(i, list.get(i).getFilial().getFilial().getPessoa().getNome(), "" + list.get(i).getFilial().getId()));
                        }
                    } else {
                        filial_id = 0;
                        listFiliais.add(new SelectItem(0, f.getFilial().getPessoa().getNome(), "" + f.getId()));
                    }
                } else {
                    filial_id = 0;
                    listFiliais.add(new SelectItem(0, f.getFilial().getPessoa().getNome(), "" + f.getId()));
                }
            }
        }
        return listFiliais;
    }

    public void setListFiliais(List<SelectItem> listFiliais) {
        this.listFiliais = listFiliais;
    }

    public Boolean getLiberaAcessaFilial() {
        return liberaAcessaFilial;
    }

    public void setLiberaAcessaFilial(Boolean liberaAcessaFilial) {
        this.liberaAcessaFilial = liberaAcessaFilial;
    }

    public Integer getFilial_id() {
        return filial_id;
    }

    public void setFilial_id(Integer filial_id) {
        this.filial_id = filial_id;
    }

}
