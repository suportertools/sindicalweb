package br.com.rtools.escola.beans;

import br.com.rtools.escola.ComponenteCurricular;
import br.com.rtools.escola.Professor;
import br.com.rtools.escola.Turma;
import br.com.rtools.escola.TurmaProfessor;
import br.com.rtools.escola.db.*;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class TurmaBean implements Serializable {

    private Turma turma = new Turma();
    private String mensagem = "";
    private int idServicos = 0;
    private int idProfessor = 0;
    private int idComponenteCurricular = 0;
    private List<TurmaProfessor> listaTurmaProfessor = new ArrayList();
    private List<Turma> listaTurma = new ArrayList();
    private MacFilial macFilial = new MacFilial();
    private Filial filial = new Filial();
    private String msgStatusFilial = "";
    private List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private List<SelectItem> listaProfessores = new ArrayList<SelectItem>();
    private List<SelectItem> listaComponenteCurricular = new ArrayList<SelectItem>();
    private Date date = new Date();
    private Date horaInicio = new Date();
    private Date horaTermino = new Date();

    public void salvar() {
        if (listaServicos.isEmpty()) {
            mensagem = "Cadastrar serviços!";
            return;
        }
        if (turma.getFilial().getId() == -1) {
            mensagem = "Informar a filial! Obs: Necessário acessar o sistema usando autênticação.";
            return;
        }
        if (turma.getQuantidade() == 0) {
            mensagem = "Informar a quantidade de vagas!";
            return;
        }
        if (turma.getDataInicio().equals("__:__") || turma.getDataInicio().equals("") || turma.getDataInicio().isEmpty()) {
            mensagem = "Informar a data inicial da turma!";
            return;
        }
        if (turma.getDataTermino().equals("__:__") || turma.getDataTermino().equals("") || turma.getDataTermino().isEmpty()) {
            mensagem = "Informar a data de termino da turma!";
            return;
        }
        turma.setHoraInicio(DataHoje.livre(horaInicio, "HH:mm"));
        turma.setHoraTermino(DataHoje.livre(horaTermino, "HH:mm"));
        if (turma.getId() == -1) {
            int dataInicioInteger = DataHoje.converteDataParaInteger(turma.getDataInicio());
            int dataFinalInteger = DataHoje.converteDataParaInteger(turma.getDataTermino());
            int dataHojeInteger = DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()));
            if (dataInicioInteger < dataHojeInteger) {
                mensagem = "A data inicial do curso deve ser maior ou igual a data de hoje!";
                return;
            }
            if (dataFinalInteger < dataHojeInteger) {
                mensagem = "A data final do curso deve ser maior ou igual a data de hoje!";
                return;
            }
            if (dataFinalInteger < dataInicioInteger) {
                mensagem = "A data final deve ser maior ou igual a data inicial!";
                return;
            }
        }
        if (DataHoje.validaHora(turma.getHoraInicio()).isEmpty()) {
            mensagem = "Hora inicial invalida!";
            return;
        }
        if (DataHoje.validaHora(turma.getHoraTermino()).isEmpty()) {
            mensagem = "Hora final invalida!";
            return;
        }
        if (turma.getHoraInicio().equals("__:__")) {
            turma.setHoraInicio("");
        }
        if (turma.getHoraTermino().equals("__:__")) {
            turma.setHoraTermino("");
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        turma.setCursos((Servicos) salvarAcumuladoDB.find(new Servicos(), Integer.parseInt(listaServicos.get(idServicos).getDescription())));
        TurmaDB turmaDB = new TurmaDBToplink();
        if (turma.getId() == -1) {
            if (turmaDB.existeTurma(turma)) {
                mensagem = "Turma já existe!";
                return;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (!salvarAcumuladoDB.inserirObjeto(turma)) {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao salvar turma!";
                return;
            }
            salvarAcumuladoDB.comitarTransacao();
            mensagem = "Turma salva com sucesso!";
            listaTurma.clear();
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (!salvarAcumuladoDB.alterarObjeto(turma)) {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao atualizar turma!";
                return;
            }
            salvarAcumuladoDB.comitarTransacao();
            mensagem = "Turma atualizada com sucesso!";
            listaTurma.clear();
        }
    }

    public String editar(Turma t) throws ParseException {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        Turma turmaC = (Turma) dB.find(t);
        for (int i = 0; i < listaServicos.size(); i++) {
            if (Integer.parseInt(listaServicos.get(i).getDescription()) == t.getCursos().getId()) {
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

    public void excluir() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (turma.getId() != -1) {
            salvarAcumuladoDB.abrirTransacao();
            for (TurmaProfessor listaTurmaProfessor1 : listaTurmaProfessor) {
                if (listaTurmaProfessor1.getId() != -1) {
                    if (!salvarAcumuladoDB.deletarObjeto((TurmaProfessor) salvarAcumuladoDB.find(listaTurmaProfessor1))) {
                        salvarAcumuladoDB.desfazerTransacao();
                        mensagem = "Erro ao excluir Professores!";
                        return;
                    }
                }
            }

            if (!salvarAcumuladoDB.deletarObjeto((Turma) salvarAcumuladoDB.find(turma))) {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao excluir Turma!";
                return;
            }

            salvarAcumuladoDB.comitarTransacao();
            mensagem = "Cadastro excluído com sucesso!";
            turma = new Turma();
            novoGenerico();
        } else {
            mensagem = "Pesquise uma turma para ser excluída!";
        }
    }

    public void removerTurmaProfessor(TurmaProfessor tp) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        tp = (TurmaProfessor) salvarAcumuladoDB.find(tp);
        if (tp.getId() != -1) {
            salvarAcumuladoDB.abrirTransacao();
            if (!salvarAcumuladoDB.deletarObjeto(tp)) {
                salvarAcumuladoDB.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Ao remover este registro!");
            } else {
                salvarAcumuladoDB.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Professor e Componente Curricular removidos com sucesso");
                listaTurmaProfessor.clear();
            }
        }
    }

    public String novo() {
        idServicos = 0;
        idProfessor = 0;
        turma = new Turma();
        idComponenteCurricular = 0;
        listaTurmaProfessor.clear();
        horaInicio = new Date();
        horaTermino = new Date();
        return "turma";

    }

    public void novoGenerico() {
        idServicos = 0;
        idProfessor = 0;
        turma = new Turma();
        turma.setDtInicio(DataHoje.dataHoje());
        turma.setDtTermino(DataHoje.dataHoje());
        idComponenteCurricular = 0;
        listaTurmaProfessor.clear();
        horaInicio = new Date();
        horaTermino = new Date();
    }

    public void adicionarTurmaProfessor() {
        mensagem = "";
        TurmaProfessor turmaProfessor = new TurmaProfessor();
        TurmaDB turmaDB = new TurmaDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (listaProfessores.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar professores!");
            return;
        }
        if (listaComponenteCurricular.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar componente currícular!");
            return;
        }
        turmaProfessor.setProfessor((Professor) salvarAcumuladoDB.find(new Professor(), Integer.parseInt(listaProfessores.get(idProfessor).getDescription())));
        turmaProfessor.setComponenteCurricular((ComponenteCurricular) salvarAcumuladoDB.find(new ComponenteCurricular(), Integer.parseInt(listaComponenteCurricular.get(idComponenteCurricular).getDescription())));
        turmaProfessor.setTurma(turma);
        if (turmaDB.existeTurmaProfessor(turmaProfessor)) {
            GenericaMensagem.warn("Validação", "Cadastro já existe!");
            return;
        }
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.inserirObjeto(turmaProfessor)) {
            GenericaMensagem.info("Sucesso", "Professor e Componente curricular adicionados");
            salvarAcumuladoDB.comitarTransacao();
            listaTurmaProfessor.clear();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Ao adicionar este registro!");
        }
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            List list = db.pesquisaTodos(150);
            for (int i = 0; i < list.size(); i++) {
                listaServicos.add(new SelectItem((int) i, (String) ((Servicos) list.get(i)).getDescricao(), Integer.toString(((Servicos) list.get(i)).getId())));
            }
        }
        return listaServicos;
    }

    public void setListaServicos(List<SelectItem> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public List<SelectItem> getListaProfessor() {
        if (listaProfessores.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<Professor> list = (List<Professor>) salvarAcumuladoDB.listaObjeto("Professor", true);
            for (int i = 0; i < list.size(); i++) {
                listaProfessores.add(new SelectItem((int) i, (String) (list.get(i).getProfessor().getNome()), Integer.toString(list.get(i).getId())));

            }
        }
        return listaProfessores;
    }

    public void setListaProfessor(List<SelectItem> listaProfessores) {
        this.listaProfessores = listaProfessores;
    }

    public List<SelectItem> getListaComponenteCurricular() {
        if (listaComponenteCurricular.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<ComponenteCurricular> list = (List<ComponenteCurricular>) salvarAcumuladoDB.listaObjeto("ComponenteCurricular", true);
            for (int i = 0; i < list.size(); i++) {
                listaComponenteCurricular.add(new SelectItem((int) i, (String) (list.get(i).getDescricao()), Integer.toString(list.get(i).getId())));
            }
        }
        return listaComponenteCurricular;
    }

    public void setListaComponenteCurricular(List<SelectItem> listaComponenteCurricular) {
        this.listaComponenteCurricular = listaComponenteCurricular;
    }

    public Turma getTurma() {
        if (GenericaSessao.exists("turmaPesquisa")) {
            turma = (Turma) GenericaSessao.getObject("turmaPesquisa", true);
        }
        if (turma.getFilial().getId() == -1) {
            getMacFilial();
            turma.setFilial(macFilial.getFilial());
        }
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }

    public int getIdComponenteCurricular() {
        return idComponenteCurricular;
    }

    public void setIdComponenteCurricular(int idComponenteCurricular) {
        this.idComponenteCurricular = idComponenteCurricular;
    }

    public List<TurmaProfessor> getListaTurmaProfessor() {
        if (listaTurmaProfessor.isEmpty()) {
            TurmaDB turmaDB = new TurmaDBToplink();
            listaTurmaProfessor = turmaDB.listaTurmaProfessor(turma.getId());
        }
        return listaTurmaProfessor;
    }

    public void setListaTurmaProfessor(List<TurmaProfessor> listaTurmaProfessor) {
        this.listaTurmaProfessor = listaTurmaProfessor;
    }

    public List<Turma> getListaTurma() {
        if (listaTurma.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaTurma = salvarAcumuladoDB.listaObjeto("Turma", true);
        }
        return listaTurma;
    }

    public void setListaTurma(List<Turma> listaTurma) {
        this.listaTurma = listaTurma;
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
            macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial", true);
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
}
