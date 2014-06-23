package br.com.rtools.academia.beans;

import br.com.rtools.academia.AcademiaGrade;
import br.com.rtools.academia.AcademiaServicoValor;
import br.com.rtools.academia.db.AcademiaDB;
import br.com.rtools.academia.db.AcademiaDBToplink;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.sistema.Periodo;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
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

    @PostConstruct
    public void init() {
        academiaServicoValor = new AcademiaServicoValor();
        listAcademiaServicoValors = new ArrayList<AcademiaServicoValor>();
        listSelectItem = new ArrayList[]{new ArrayList<SelectItem>()};
        index = new Integer[]{0};
        listAcademiaGrades = new ArrayList<AcademiaGrade>();
        listPeriodos = new ArrayList<Periodo>();
        academiaGrade = new AcademiaGrade();
        periodo = new Periodo();
        maximoParcelas = 0;
        ocultaParcelas = true;
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void save() {
        DaoInterface di = new Dao();
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
        academiaServicoValor.setServicos((Servicos) di.find(new Servicos(), Integer.parseInt(getListServicos().get(index[0]).getDescription())));
        academiaServicoValor.setPeriodo(periodo);
        academiaServicoValor.setAcademiaGrade(academiaGrade);
        NovoLog novoLog = new NovoLog();
        if (academiaServicoValor.getId() == -1) {
            AcademiaDB academiaDB = new AcademiaDBToplink();
            if (((AcademiaServicoValor) academiaDB.existeAcademiaServicoValor(academiaServicoValor)) != null) {
                GenericaMensagem.warn("Sistema", "Horário já cadastrado!");
                return;
            }
            di.openTransaction();
            if (di.save(academiaServicoValor)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro inserido");
                novoLog.save("ID: " + academiaServicoValor.getId() + " - Grade: " + academiaServicoValor.getAcademiaGrade().getId() + " - Fórmula: " + academiaServicoValor.getFormula() + " - Serviço: (" + academiaServicoValor.getServicos().getId() + ") " + academiaServicoValor.getServicos().getDescricao() + " - Nº Parcelas: " + academiaServicoValor.getNumeroParcelas() + " - Período: " + academiaServicoValor.getPeriodo().getDescricao());
                listAcademiaServicoValors.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            AcademiaServicoValor asv = (AcademiaServicoValor) di.find(academiaServicoValor);
            String beforeString = "ID: " + asv.getId() + " - Grade: " + asv.getAcademiaGrade().getId() + " - Fórmula: " + asv.getFormula() + " - Serviço: (" + asv.getServicos().getId() + ") " + asv.getServicos().getDescricao() + " - Nº Parcelas: " + asv.getNumeroParcelas() + " - Período: " + asv.getPeriodo().getDescricao();
            di.openTransaction();
            if (di.update(academiaServicoValor)) {
                novoLog.update(beforeString, "ID: " + academiaServicoValor.getId() + " - Grade: " + academiaServicoValor.getAcademiaGrade().getId() + " - Fórmula: " + academiaServicoValor.getFormula() + " - Serviço: (" + academiaServicoValor.getServicos().getId() + ") " + academiaServicoValor.getServicos().getDescricao() + " - Nº Parcelas: " + academiaServicoValor.getNumeroParcelas() + " - Período: " + academiaServicoValor.getPeriodo().getDescricao());
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro atualizado");
                listAcademiaServicoValors.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }
        clear();
    }

    public void clear() {
        GenericaSessao.remove("academiaServicoValorBean");
    }

    public void edit(AcademiaServicoValor asv) {
        academiaServicoValor = asv;
        academiaGrade = academiaServicoValor.getAcademiaGrade();
        periodo = academiaServicoValor.getPeriodo();
        for (int i = 0; i < getListServicos().size(); i++) {
            if (Integer.parseInt(getListServicos().get(i).getDescription()) == asv.getServicos().getId()) {
                index[0] = i;
                break;
            }
        }
    }

    public void delete(AcademiaServicoValor asv) {
        if (asv.getId() != -1) {
            DaoInterface di = new Dao();
            NovoLog novoLog = new NovoLog();
            di.openTransaction();
            if (di.delete(asv)) {
                novoLog.delete("ID: " + asv.getId() + " - Grade: " + asv.getAcademiaGrade().getId() + " - Fórmula: " + asv.getFormula() + " - Serviço: (" + asv.getServicos().getId() + ") " + asv.getServicos().getDescricao() + " - Nº Parcelas: " + asv.getNumeroParcelas() + " - Período: " + asv.getPeriodo().getDescricao());
                di.commit();
                GenericaMensagem.info("Sucesso", "Excluído com sucesso");
                listAcademiaServicoValors.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao excluir registro!");
            }
        }
    }

    public List<AcademiaServicoValor> getListAcademiaServicoValors() {
        if(!getListServicos().isEmpty()) {
            if (listAcademiaServicoValors.isEmpty()) {
                AcademiaDB academiaDB = new AcademiaDBToplink();
                listAcademiaServicoValors = academiaDB.listaAcademiaServicoValor(Integer.parseInt(getListServicos().get(index[0]).getDescription()));
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
            DaoInterface di = new Dao();
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
            DaoInterface di = new Dao();
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
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

}
