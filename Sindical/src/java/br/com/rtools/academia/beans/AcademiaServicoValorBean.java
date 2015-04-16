package br.com.rtools.academia.beans;

import br.com.rtools.academia.AcademiaGrade;
import br.com.rtools.academia.AcademiaSemana;
import br.com.rtools.academia.AcademiaServicoValor;
import br.com.rtools.academia.dao.AcademiaDao;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.sistema.Periodo;
import br.com.rtools.sistema.Semana;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AcademiaServicoValorBean implements Serializable {

    private AcademiaServicoValor academiaServicoValor;
    private List<AcademiaServicoValor> listAcademiaServicoValors;
    private List<AcademiaGrade> listAcademiaGrades;
    private List<Periodo> listPeriodos;
    private AcademiaGrade academiaGrade;
    private Periodo periodo;
    /**
     * <ul>
     * <li>[0] Serviços </li>
     * </ul>
     */
    private List<SelectItem>[] listSelectItem;
    private Integer[] index;
    private int maximoParcelas;
    private boolean ocultaParcelas;

    // SEMANA
    private boolean dom;
    private boolean seg;
    private boolean ter;
    private boolean qua;
    private boolean qui;
    private boolean sex;
    private boolean sab;

    private List<Semana> listaSemana;
    private List<AcademiaSemana> listaAcademiaSemana;

    private int indexSemana;

    @PostConstruct
    public void init() {
        academiaServicoValor = new AcademiaServicoValor();
        academiaServicoValor.setValidade(null);
        listAcademiaServicoValors = new ArrayList<AcademiaServicoValor>();
        listSelectItem = new ArrayList[]{new ArrayList<SelectItem>()};
        index = new Integer[]{0};
        listAcademiaGrades = new ArrayList<AcademiaGrade>();
        listPeriodos = new ArrayList<Periodo>();
        academiaGrade = new AcademiaGrade();
        periodo = new Periodo();
        maximoParcelas = 0;
        ocultaParcelas = true;
        listaSemana = new ArrayList<Semana>();
        listaAcademiaSemana = new ArrayList<AcademiaSemana>();
        indexSemana = 0;
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public String getListaGrades(AcademiaServicoValor academiaServicoValor) {
        AcademiaDao db = new AcademiaDao();

        List<AcademiaSemana> lista = db.listaAcademiaSemana(academiaServicoValor.getId());

        if (!lista.isEmpty()) {
            String text = "";
            if (lista.size() != 1) {
                for (int i = 0; i < lista.size(); i++) {
                    text += " [" + lista.get(i).getAcademiaGrade().getHoraInicio() + " às " + lista.get(i).getAcademiaGrade().getHoraFim() + " " + lista.get(i).getSemana().getDescricao() + "]  ";
                }
            } else {
                text += lista.get(0).getAcademiaGrade().getHoraInicio() + " às " + lista.get(0).getAcademiaGrade().getHoraFim() + " " + lista.get(0).getSemana().getDescricao();
            }
            return text;
        }

        return null;
    }

    public void adicionarHorario(int id_semana) {
        indexSemana = id_semana;
    }

    public void booleanHorario(int id_semana, boolean status) {
        indexSemana = id_semana;

        for (int i = 0; i < listaAcademiaSemana.size(); i++) {
            if (listaAcademiaSemana.get(i).getSemana().getId() == indexSemana && status == false) {
                if (listaAcademiaSemana.get(i).getAcademiaServicoValor() != null) {
                    Dao di = new Dao();

                    di.openTransaction();

                    di.delete(di.find(listaAcademiaSemana.get(i)));

                    di.commit();
                }

                listaAcademiaSemana.remove(i);
                return;
            }
        }
    }

    public void addListaSemana() {
        for (int i = 0; i < listaAcademiaSemana.size(); i++) {
            if (listaAcademiaSemana.get(i).getSemana().getId() == indexSemana) {
                listaAcademiaSemana.get(i).setAcademiaGrade(academiaGrade);
                return;
            }
        }

        for (int i = 0; i < getListaSemana().size(); i++) {
            if (listaSemana.get(i).getId() == indexSemana) {
                if (academiaServicoValor.getId() == -1) {
                    listaAcademiaSemana.add(new AcademiaSemana(-1, academiaGrade, listaSemana.get(i), null));
                } else {
                    listaAcademiaSemana.add(new AcademiaSemana(-1, academiaGrade, listaSemana.get(i), academiaServicoValor));
                }
                break;
            }
        }
    }

    public void save() {
        Dao di = new Dao();
        if (listSelectItem[0].isEmpty()) {
            GenericaMensagem.warn("Sistema", "Cadastrar serviços!");
            return;
        }
        if (listPeriodos.isEmpty()) {
            GenericaMensagem.warn("Sistema", "Cadastrar lista de períodos!");
            return;
        }
        if (listAcademiaGrades.isEmpty()) {
            GenericaMensagem.warn("Sistema", "Cadastrar grade de horários e dias da semana!");
            return;
        }

//        if (academiaServicoValor.getValidade() != null) {
//            int dataValidade = DataHoje.converteDataParaInteger(academiaServicoValor.getValidadeString());
//            int dataHoje = DataHoje.converteDataParaInteger(DataHoje.data());
//            if (dataValidade < dataHoje) {
//                GenericaMensagem.warn("Sistema", "Data de válidade inválida! Deve ser maior ou igual a data de hoje.");
//                return;
//            }
//        }
        academiaServicoValor.setServicos((Servicos) di.find(new Servicos(), Integer.parseInt(getListServicos().get(index[0]).getDescription())));
        academiaServicoValor.setPeriodo(periodo);

        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        academiaServicoValor.setFormula(academiaServicoValor.getFormula().toLowerCase());
        AcademiaDao academiaDB = new AcademiaDao();
        if (academiaServicoValor.getId() == -1) {

//            if (((AcademiaServicoValor) academiaDB.existeAcademiaServicoValor(academiaServicoValor)) != null) {
//                GenericaMensagem.warn("Sistema", "Horário já cadastrado!");
//                return;
//            }
//            
            if (di.save(academiaServicoValor)) {
                novoLog.save("ID: " + academiaServicoValor.getId() + " - Fórmula: " + academiaServicoValor.getFormula() + " - Serviço: (" + academiaServicoValor.getServicos().getId() + ") " + academiaServicoValor.getServicos().getDescricao() + " - Nº Parcelas: " + academiaServicoValor.getNumeroParcelas() + " - Período: " + academiaServicoValor.getPeriodo().getDescricao());
                listAcademiaServicoValors.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            AcademiaServicoValor asv = (AcademiaServicoValor) di.find(academiaServicoValor);
            String beforeString = "ID: " + asv.getId() + " - Fórmula: " + asv.getFormula() + " - Serviço: (" + asv.getServicos().getId() + ") " + asv.getServicos().getDescricao() + " - Nº Parcelas: " + asv.getNumeroParcelas() + " - Período: " + asv.getPeriodo().getDescricao();
            if (di.update(academiaServicoValor)) {
                novoLog.update(beforeString, "ID: " + academiaServicoValor.getId() + " - Fórmula: " + academiaServicoValor.getFormula() + " - Serviço: (" + academiaServicoValor.getServicos().getId() + ") " + academiaServicoValor.getServicos().getDescricao() + " - Nº Parcelas: " + academiaServicoValor.getNumeroParcelas() + " - Período: " + academiaServicoValor.getPeriodo().getDescricao());
                listAcademiaServicoValors.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }

        if (!listaAcademiaSemana.isEmpty()) {
            int igual = 0;
            for (AcademiaSemana listaAcademias : listaAcademiaSemana) {
                if (!academiaDB.existeAcademiaSemana(listaAcademias.getAcademiaGrade().getId(), listaAcademias.getSemana().getId(), academiaServicoValor.getServicos().getId(), academiaServicoValor.getPeriodo().getId()).isEmpty()) {
                    igual++;
                }
            }

            if (igual == listaAcademiaSemana.size() && (listaAcademiaSemana.get(0).getAcademiaServicoValor() != null && academiaServicoValor.getId() != listaAcademiaSemana.get(0).getAcademiaServicoValor().getId() || listaAcademiaSemana.get(0).getAcademiaServicoValor() == null)) {
                GenericaMensagem.warn("Erro", "Essa grade já existe!");
                di.rollback();
                academiaServicoValor = new AcademiaServicoValor();
                return;
            }
        }

        for (int i = 0; i < listaAcademiaSemana.size(); i++) {
//            if (academiaDB.existeAcademiaSemana(listaAcademiaSemana.get(i).getAcademiaGrade().getId(), listaAcademiaSemana.get(i).getSemana().getId(), academiaServicoValor.getServicos().getId(), academiaServicoValor.getPeriodo().getId()) != null){
//                GenericaMensagem.warn("Erro", "Não foi possível salvar lista de grades!");
//                di.rollback();
//                academiaServicoValor = new AcademiaServicoValor();
//                return;
//            }

            if (listaAcademiaSemana.get(i).getAcademiaServicoValor() == null) {
                listaAcademiaSemana.get(i).setAcademiaServicoValor(academiaServicoValor);
                if (!di.save(listaAcademiaSemana.get(i))) {
                    GenericaMensagem.warn("Erro", "Não foi possível salvar lista de grades!");
                    di.rollback();
                    academiaServicoValor = new AcademiaServicoValor();
                    return;
                }
            } else if (listaAcademiaSemana.get(i).getId() == -1) {
                if (!di.save(listaAcademiaSemana.get(i))) {
                    GenericaMensagem.warn("Erro", "Não foi possível salvar lista de grades!");
                    di.rollback();
                    academiaServicoValor = new AcademiaServicoValor();
                    return;
                }
            } else {
                if (!di.update(listaAcademiaSemana.get(i))) {
                    GenericaMensagem.warn("Erro", "Não foi possível alterar lista de grades!");
                    di.rollback();
                    academiaServicoValor = new AcademiaServicoValor();
                    return;
                }
            }
        }
        if (academiaServicoValor.getId() == -1) {
            GenericaMensagem.info("Sucesso", "Registro Inserido!");
        } else {
            GenericaMensagem.info("Sucesso", "Registro Atualizado!");
        }
        di.commit();
        clear();
    }

    public void clear() {
        GenericaSessao.remove("academiaServicoValorBean");
    }

    public void edit(AcademiaServicoValor asv) {
        academiaServicoValor = asv;

        periodo = academiaServicoValor.getPeriodo();
        for (int i = 0; i < getListServicos().size(); i++) {
            if (Integer.parseInt(getListServicos().get(i).getDescription()) == asv.getServicos().getId()) {
                index[0] = i;
                break;
            }
        }
        AcademiaDao db = new AcademiaDao();
        listaAcademiaSemana = db.listaAcademiaSemana(academiaServicoValor.getId());
        dom = false;
        seg = false;
        ter = false;
        qua = false;
        qui = false;
        sex = false;
        sab = false;

        for (AcademiaSemana las : listaAcademiaSemana) {
            if (las.getSemana().getId() == 1) {
                dom = true;
            }
            if (las.getSemana().getId() == 2) {
                seg = true;
            }
            if (las.getSemana().getId() == 3) {
                ter = true;
            }
            if (las.getSemana().getId() == 4) {
                qua = true;
            }
            if (las.getSemana().getId() == 5) {
                qui = true;
            }
            if (las.getSemana().getId() == 6) {
                sex = true;
            }
            if (las.getSemana().getId() == 7) {
                sab = true;
            }
        }
    }

    public void delete(AcademiaServicoValor asv) {
        if (asv.getId() != -1) {
            Dao di = new Dao();
            NovoLog novoLog = new NovoLog();
            AcademiaDao academiaDao = new AcademiaDao();
            di.openTransaction();

            listaAcademiaSemana = academiaDao.listaAcademiaSemana(asv.getId());

            for (AcademiaSemana las : listaAcademiaSemana) {
                AcademiaSemana as = (AcademiaSemana) di.find(las);
                if (!di.delete(as)) {
                    di.rollback();
                    GenericaMensagem.warn("Erro", "Não foi possível excluir Linha!");
                    return;
                }
            }

            if (di.delete(asv)) {
                novoLog.delete("ID: " + asv.getId() + " - Fórmula: " + asv.getFormula() + " - Serviço: (" + asv.getServicos().getId() + ") " + asv.getServicos().getDescricao() + " - Nº Parcelas: " + asv.getNumeroParcelas() + " - Período: " + asv.getPeriodo().getDescricao());
                di.commit();
                GenericaMensagem.info("Sucesso", "Excluído com sucesso");
                listAcademiaServicoValors.clear();
                listaAcademiaSemana.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao excluir registro!");
            }
        }
    }

    public List<AcademiaServicoValor> getListAcademiaServicoValors() {
        if (!getListServicos().isEmpty()) {
            if (listAcademiaServicoValors.isEmpty()) {
                AcademiaDao academiaDao = new AcademiaDao();
                listAcademiaServicoValors = academiaDao.listaAcademiaServicoValor(Integer.parseInt(getListServicos().get(index[0]).getDescription()));
            }
        }
        return listAcademiaServicoValors;
    }

    public void setListAcademiaServicoValors(List<AcademiaServicoValor> listAcademiaServicoValors) {
        this.listAcademiaServicoValors = listAcademiaServicoValors;
    }

    public AcademiaServicoValor getAcademiaServicoValor() {
        return academiaServicoValor;
    }

    public void setAcademiaServicoValor(AcademiaServicoValor academiaServicoValor) {
        this.academiaServicoValor = academiaServicoValor;
    }

    public List<SelectItem> getListServicos() {
        if (listSelectItem[0].isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            List list = db.pesquisaTodos(122);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i,
                        ((Servicos) list.get(i)).getDescricao(),
                        Integer.toString(((Servicos) list.get(i)).getId())));
            }
        }
        return listSelectItem[0];
    }

    public List<Periodo> getListPeriodos() {
        if (listPeriodos.isEmpty()) {
            Dao di = new Dao();
            listPeriodos = (List<Periodo>) di.list(new Periodo(), true);
            if (!listPeriodos.isEmpty()) {
                periodo = listPeriodos.get(0);
            }
        }
        return listPeriodos;
    }

    public void setListPeriodos(List<Periodo> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

    public List<AcademiaGrade> getListAcademiaGrades() {
        if (listAcademiaGrades.isEmpty()) {
            Dao di = new Dao();
            listAcademiaGrades = (List<AcademiaGrade>) di.list(new AcademiaGrade(), true);
            if (!listAcademiaGrades.isEmpty()) {
                academiaGrade = listAcademiaGrades.get(0);
            }
        }
        return listAcademiaGrades;
    }

    public void setListAcademiaGrades(List<AcademiaGrade> listAcademiaGrades) {
        this.listAcademiaGrades = listAcademiaGrades;
    }

    public boolean isOcultaParcelas() {
        if (!getListPeriodos().isEmpty()) {
            int id = periodo.getId();
            switch (id) {
                case 5:
                    ocultaParcelas = false;
                    maximoParcelas = 2;
                    break;
                case 6:
                    ocultaParcelas = false;
                    maximoParcelas = 4;
                    break;
                case 7:
                    ocultaParcelas = false;
                    maximoParcelas = 7;
                    break;
                default:
                    ocultaParcelas = true;
            }
        }
        return ocultaParcelas;
    }

    public void setOcultaParcelas(boolean ocultaParcelas) {
        this.ocultaParcelas = ocultaParcelas;
    }

    public int getMaximoParcelas() {
        return maximoParcelas;
    }

    public void setMaximoParcelas(int maximoParcelas) {
        this.maximoParcelas = maximoParcelas;
    }

    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public AcademiaGrade getAcademiaGrade() {
        return academiaGrade;
    }

    public void setAcademiaGrade(AcademiaGrade academiaGrade) {
        this.academiaGrade = academiaGrade;
    }

    public Periodo getPeriodo() {
        if (periodo == null) {
            periodo = new Periodo();
        }
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public boolean isDom() {
        return dom;
    }

    public void setDom(boolean dom) {
        this.dom = dom;
    }

    public boolean isSeg() {
        return seg;
    }

    public void setSeg(boolean seg) {
        this.seg = seg;
    }

    public boolean isTer() {
        return ter;
    }

    public void setTer(boolean ter) {
        this.ter = ter;
    }

    public boolean isQua() {
        return qua;
    }

    public void setQua(boolean qua) {
        this.qua = qua;
    }

    public boolean isQui() {
        return qui;
    }

    public void setQui(boolean qui) {
        this.qui = qui;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public boolean isSab() {
        return sab;
    }

    public void setSab(boolean sab) {
        this.sab = sab;
    }

    public List<Semana> getListaSemana() {
        if (listaSemana.isEmpty()) {
            Dao di = new Dao();
            listaSemana = (List<Semana>) di.list(new Semana());
        }
        return listaSemana;
    }

    public void setListaSemana(List<Semana> listaSemana) {
        this.listaSemana = listaSemana;
    }

    public List<AcademiaSemana> getListaAcademiaSemana() {
        return listaAcademiaSemana;
    }

    public void setListaAcademiaSemana(List<AcademiaSemana> listaAcademiaSemana) {
        this.listaAcademiaSemana = listaAcademiaSemana;
    }

    public String getMostraDomingo() {
        if (!listaAcademiaSemana.isEmpty()) {
            for (AcademiaSemana horario : listaAcademiaSemana) {
                if (horario.getSemana().getId() == 1) {
                    return horario.getAcademiaGrade().getHoraInicio() + " às " + horario.getAcademiaGrade().getHoraFim();
                }
            }
        }
        return null;
    }

    public String getMostraSegunda() {
        if (!listaAcademiaSemana.isEmpty()) {
            for (AcademiaSemana horario : listaAcademiaSemana) {
                if (horario.getSemana().getId() == 2) {
                    return horario.getAcademiaGrade().getHoraInicio() + " às " + horario.getAcademiaGrade().getHoraFim();
                }
            }
        }
        return null;
    }

    public String getMostraTerca() {
        if (!listaAcademiaSemana.isEmpty()) {
            for (AcademiaSemana horario : listaAcademiaSemana) {
                if (horario.getSemana().getId() == 3) {
                    return horario.getAcademiaGrade().getHoraInicio() + " às " + horario.getAcademiaGrade().getHoraFim();
                }
            }
        }
        return null;
    }

    public String getMostraQuarta() {
        if (!listaAcademiaSemana.isEmpty()) {
            for (AcademiaSemana horario : listaAcademiaSemana) {
                if (horario.getSemana().getId() == 4) {
                    return horario.getAcademiaGrade().getHoraInicio() + " às " + horario.getAcademiaGrade().getHoraFim();
                }
            }
        }
        return null;
    }

    public String getMostraQuinta() {
        if (!listaAcademiaSemana.isEmpty()) {
            for (AcademiaSemana horario : listaAcademiaSemana) {
                if (horario.getSemana().getId() == 5) {
                    return horario.getAcademiaGrade().getHoraInicio() + " às " + horario.getAcademiaGrade().getHoraFim();
                }
            }
        }
        return null;
    }

    public String getMostraSexta() {
        if (!listaAcademiaSemana.isEmpty()) {
            for (AcademiaSemana horario : listaAcademiaSemana) {
                if (horario.getSemana().getId() == 6) {
                    return horario.getAcademiaGrade().getHoraInicio() + " às " + horario.getAcademiaGrade().getHoraFim();
                }
            }
        }
        return null;
    }

    public String getMostraSabado() {
        if (!listaAcademiaSemana.isEmpty()) {
            for (AcademiaSemana horario : listaAcademiaSemana) {
                if (horario.getSemana().getId() == 7) {
                    return horario.getAcademiaGrade().getHoraInicio() + " às " + horario.getAcademiaGrade().getHoraFim();
                }
            }
        }
        return null;
    }
}
