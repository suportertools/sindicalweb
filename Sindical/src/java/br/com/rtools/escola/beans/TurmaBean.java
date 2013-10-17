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
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class TurmaBean implements Serializable {

    private Turma turma = new Turma();
    private String msgConfirma = "";
    private int idServicos = 0;
    private int idProfessor = 0;
    private int idComponenteCurricular = 0;
    private List<TurmaProfessor> listaTurmaProfessor = new ArrayList();
    private List<Turma> listaTurma = new ArrayList();
    private int idIndex = -1;
    private int idIndexPesquisa = -1;
    private MacFilial macFilial = new MacFilial();
    private Filial filial = new Filial();
    private String msgStatusFilial = "";
    private List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private List<SelectItem> listaProfessores = new ArrayList<SelectItem>();
    private List<SelectItem> listaComponenteCurricular = new ArrayList<SelectItem>();

    public String salvar() {
        if (turma.getFilial().getId() == -1) {
            msgConfirma = "Informar a filial! Obs: Necessário acessar o sistema usando autênticação.";
            return null;
        }
        if (turma.getQuantidade() == 0) {
            msgConfirma = "Informar a quantidade de vagas!";
            return null;
        }
        if (turma.getDataInicio().equals("__:__") || turma.getDataInicio().equals("") || turma.getDataInicio().isEmpty()) {
            msgConfirma = "Informar a data inicial da turma!";
            return null;
        }
        if (turma.getDataTermino().equals("__:__") || turma.getDataTermino().equals("") || turma.getDataTermino().isEmpty()) {
            msgConfirma = "Informar a data de termino da turma!";
            return null;
        }
        if (turma.getId() == -1) {
            int dataInicioInteger = DataHoje.converteDataParaInteger(turma.getDataInicio());
            int dataFinalInteger = DataHoje.converteDataParaInteger(turma.getDataTermino());
            int dataHojeInteger = DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()));
            if (dataInicioInteger < dataHojeInteger) {
                msgConfirma = "A data inicial do curso deve ser maior ou igual a data de hoje!";
                return null;
            }
            if (dataFinalInteger < dataHojeInteger) {
                msgConfirma = "A data final do curso deve ser maior ou igual a data de hoje!";
                return null;
            }
            if (dataFinalInteger >= dataInicioInteger) {
                msgConfirma = "A data final deve ser maior ou igual a data inicial!";
                return null;
            }
        }
        if (DataHoje.validaHora(turma.getHoraInicio()).isEmpty()) {
            msgConfirma = "Hora inicial invalida!";
            return null;
        }
        if (DataHoje.validaHora(turma.getHoraTermino()).isEmpty()) {
            msgConfirma = "Hora final invalida!";
            return null;
        }
        if (turma.getHoraInicio().equals("__:__")) {
            turma.setHoraInicio("");
        }
        if (turma.getHoraTermino().equals("__:__")) {
            turma.setHoraTermino("");
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        turma.setCursos((Servicos) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaServicos.get(idServicos).getDescription()), "Servicos"));
        TurmaDB turmaDB = new TurmaDBToplink();
        if (turma.getId() == -1) {
            if (turmaDB.existeTurma(turma)) {
                msgConfirma = "Turma já existe!";
                return null;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (!salvarAcumuladoDB.inserirObjeto(turma)) {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao salvar turma!";
                return null;
            }
            salvarAcumuladoDB.comitarTransacao();
            msgConfirma = "Turma salva com sucesso!";
            listaTurma.clear();
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (!salvarAcumuladoDB.alterarObjeto(turma)) {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao atualizar turma!";
                return null;
            }
            salvarAcumuladoDB.comitarTransacao();
            msgConfirma = "Turma atualizada com sucesso!";
            listaTurma.clear();
        }
        return null;
    }

    public String editar(Turma t) {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        Turma turmaC = (Turma) dB.pesquisaCodigo(t.getId(), "Turma");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("turmaPesquisa", turmaC);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "turma";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public String excluir() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (turma.getId() != -1) {
            salvarAcumuladoDB.abrirTransacao();
            for (int i = 0; i < listaTurmaProfessor.size(); i++) {
                if (listaTurmaProfessor.get(i).getId() != -1) {
                    if (!salvarAcumuladoDB.deletarObjeto((TurmaProfessor) salvarAcumuladoDB.pesquisaCodigo(listaTurmaProfessor.get(i).getId(), "TurmaProfessor"))) {
                        msgConfirma = "Erro ao excluir Professores!";
                        salvarAcumuladoDB.desfazerTransacao();
                        return null;
                    }
                }
            }

            if (!salvarAcumuladoDB.deletarObjeto((Turma) salvarAcumuladoDB.pesquisaCodigo(turma.getId(), "Turma"))) {
                msgConfirma = "Erro ao excluir Turma!";
                salvarAcumuladoDB.desfazerTransacao();
                return null;
            }

            msgConfirma = "Cadastro excluído com sucesso!";
            salvarAcumuladoDB.comitarTransacao();
            turma = new Turma();
            novoGenerico();
        } else {
            msgConfirma = "Pesquise uma turma para ser excluída!";
        }
        return "turma";
    }

    public void removerTurmaProfessor(TurmaProfessor tp) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        tp = (TurmaProfessor) salvarAcumuladoDB.pesquisaCodigo(tp.getId(), "TurmaProfessor");
        if (tp.getId() != -1) {
            salvarAcumuladoDB.abrirTransacao();
            if (!salvarAcumuladoDB.deletarObjeto(tp)) {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao excluir professor!";
            } else {
                salvarAcumuladoDB.comitarTransacao();
                msgConfirma = "Professor e Componente Curricular removidos com sucesso";
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
        return "turma";

    }

    public void novoGenerico() {
        idServicos = 0;
        idProfessor = 0;
        turma = new Turma();
        idComponenteCurricular = 0;
        listaTurmaProfessor.clear();
    }

    public String adicionarTurmaProfessor() {
        msgConfirma = "";
        TurmaProfessor turmaProfessor = new TurmaProfessor();
        TurmaDB turmaDB = new TurmaDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        turmaProfessor.setProfessor((Professor) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaProfessores.get(idProfessor).getDescription()), "Professor"));
        turmaProfessor.setComponenteCurricular((ComponenteCurricular) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaComponenteCurricular.get(idComponenteCurricular).getDescription()), "ComponenteCurricular"));
        turmaProfessor.setTurma(turma);
        if (turmaDB.existeTurmaProfessor(turmaProfessor)) {
            msgConfirma = "Professor e Componente curricular já existem!";
            return null;
        }
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.inserirObjeto(turmaProfessor)) {
            msgConfirma = "Professor e Componente curricular adicionados com sucesso";
            listaTurmaProfessor.clear();
            salvarAcumuladoDB.comitarTransacao();
        } else {
            msgConfirma = "Erro ao adicionar Professor e Componente curricular!";
            salvarAcumuladoDB.desfazerTransacao();
        }
        return null;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            List list = db.pesquisaTodos(150);
            for (int i = 0; i < list.size(); i++) {
                listaServicos.add(new SelectItem(new Integer(i),
                        (String) ((Servicos) list.get(i)).getDescricao(),
                        Integer.toString(((Servicos) list.get(i)).getId())));
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
                listaProfessores.add(new SelectItem(new Integer(i),
                        (String) (list.get(i).getProfessor().getNome()),
                        Integer.toString(list.get(i).getId())));

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
                listaComponenteCurricular.add(new SelectItem(new Integer(i),
                        (String) (list.get(i).getDescricao()),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaComponenteCurricular;
    }

    public void setListaComponenteCurricular(List<SelectItem> listaProfessores) {
        this.listaProfessores = listaProfessores;
    }

    public Turma getTurma() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("turmaPesquisa") != null) {
            TurmaDB turmaDB = new TurmaDBToplink();
            turma = (Turma) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("turmaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("turmaPesquisa");
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

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
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

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getIdIndexPesquisa() {
        return idIndexPesquisa;
    }

    public void setIdIndexPesquisa(int idIndexPesquisa) {
        this.idIndexPesquisa = idIndexPesquisa;
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
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") != null) {
            macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");
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
}