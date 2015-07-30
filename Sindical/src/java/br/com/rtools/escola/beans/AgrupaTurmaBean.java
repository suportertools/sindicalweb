package br.com.rtools.escola.beans;

import br.com.rtools.escola.AgrupaTurma;
import br.com.rtools.escola.ListaAgrupaTurma;
import br.com.rtools.escola.Turma;
import br.com.rtools.escola.dao.AgrupaTurmaDao;
import br.com.rtools.logSistema.NovoLog;
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

@ManagedBean
@SessionScoped
public class AgrupaTurmaBean implements Serializable {

    private AgrupaTurma agrupaTurma;
    private List<AgrupaTurma> listAgrupaTurma;
    private List<ListaAgrupaTurma> itensAgrupados;
    private boolean integral;

    @PostConstruct
    public void init() {
        agrupaTurma = new AgrupaTurma();
        listAgrupaTurma = new ArrayList<>();
        itensAgrupados = new ArrayList<>();
        integral = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("turmaPesquisa");
        clear();
    }

    public void clear() {
        GenericaSessao.remove("agrupaTurmaBean");
    }

    public void save() {
        Dao dao = new Dao();
        if (itensAgrupados.isEmpty()) {
            GenericaMensagem.warn("Validação", "Pesquisar turma e adaocionar itens a lista!");
            return;
        }
        boolean erro = false;
        dao.openTransaction();
        int idTurmaIntegral = 0;
        for (int i = 0; i < itensAgrupados.size(); i++) {
            if (itensAgrupados.get(i).isIsIntegral()) {
                idTurmaIntegral = itensAgrupados.get(i).getAgrupaTurma().getTurma().getId();
            }
        }
        AgrupaTurmaDao agrupaTurmaDao = new AgrupaTurmaDao();
        NovoLog novoLog = new NovoLog();
        for (int i = 0; i < itensAgrupados.size(); i++) {
            if (itensAgrupados.get(i).getAgrupaTurma().getId() == -1) {
                if (itensAgrupados.isEmpty()) {
                    if (!((List) agrupaTurmaDao.pesquisaPorTurmaIntegral(itensAgrupados.get(i).getAgrupaTurma().getTurmaIntegral().getId())).isEmpty()) {
                        GenericaMensagem.warn("Validação", "Grupo integral já cadastrado! Realizar agrupamento com o já existente.");
                        return;
                    }
                }
                if (!dao.save(itensAgrupados.get(i).getAgrupaTurma())) {
                    erro = true;
                    break;
                }
            } else {
                if (!dao.update(itensAgrupados.get(i).getAgrupaTurma())) {
                    erro = true;
                    break;
                }
            }
        }
        if (erro) {
            dao.rollback();
            GenericaMensagem.warn("Erro", "Ao inserir registro(s)!");
            return;
        }
        dao.commit();
        listAgrupaTurma.clear();
        itensAgrupados.clear();
        integral = false;
        agrupaTurma = new AgrupaTurma();
        GenericaMensagem.info("Sucesso", "Registro(s) inserido(s) com sucesso");
    }

    public void edit(AgrupaTurma at) {
        AgrupaTurmaDao agrupaTurmaDao = new AgrupaTurmaDao();
        List<AgrupaTurma> list = (List<AgrupaTurma>) agrupaTurmaDao.pesquisaPorTurmaIntegral(at.getTurmaIntegral().getId());
        itensAgrupados.clear();
        boolean turmaIntegral;
        if (!list.isEmpty()) {
            int idMemoria = list.get(0).getTurmaIntegral().getId();
            for (int i = 0; i < list.size(); i++) {
                if (idMemoria == list.get(i).getTurma().getId()) {
                    turmaIntegral = true;
                } else {
                    turmaIntegral = false;
                }
                itensAgrupados.add(new ListaAgrupaTurma(list.get(i), turmaIntegral));
            }
        }

    }

    public void delete(AgrupaTurma at) {
        AgrupaTurmaDao agrupaTurmaDao = new AgrupaTurmaDao();
        List<AgrupaTurma> list = (List<AgrupaTurma>) agrupaTurmaDao.pesquisaPorTurmaIntegral(at.getTurmaIntegral().getId());
        Dao dao = new Dao();
        if (!list.isEmpty()) {
            dao.openTransaction();
            for (int i = 0; i < list.size(); i++) {
                AgrupaTurma at1 = (AgrupaTurma) dao.find(new AgrupaTurma(), list.get(i).getId());
                if (!dao.delete(at1)) {
                    dao.rollback();
                    GenericaMensagem.warn("Erro", "Ao remover registro(s)!");
                    return;
                }
            }
            dao.commit();
            NovoLog novoLog = new NovoLog();
            novoLog.delete(list.toString());
            novoLog.setTabela("esc_agrupa_turma");
            novoLog.setCodigo(at.getId());
            GenericaMensagem.info("Sucesso", "Registro(s) removido(s)");
        }
        listAgrupaTurma.clear();
        itensAgrupados.clear();
    }

    public void addItem() {
        if (agrupaTurma.getTurma().getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquisar uma turma!");
            return;
        }
        if (!itensAgrupados.isEmpty()) {
            for (int i = 0; i < itensAgrupados.size(); i++) {
                if (!itensAgrupados.get(i).getAgrupaTurma().getTurma().getSala().equals(agrupaTurma.getTurma().getSala())) {
                    GenericaMensagem.warn("Validação", "Não é possível agrupar esta turma, não pertence a mesma sala!");
                    return;
                }
                if (itensAgrupados.get(i).getAgrupaTurma().getTurma().getId() == agrupaTurma.getTurma().getId()) {
                    GenericaMensagem.warn("Validação", "Não pode existir duas turmas para o mesmo grupo!");
                    return;
                }
            }
        }
        if (!integral) {
            if (itensAgrupados.isEmpty()) {
                integral = true;
            }
        }
        if (integral) {
            for (int i = 0; i < itensAgrupados.size(); i++) {
                itensAgrupados.get(i).getAgrupaTurma().setTurmaIntegral(agrupaTurma.getTurma());
                itensAgrupados.get(i).setIsIntegral(false);
            }
            agrupaTurma.setTurmaIntegral(agrupaTurma.getTurma());
        } else {
            if (!itensAgrupados.isEmpty()) {
                agrupaTurma.setTurmaIntegral(itensAgrupados.get(0).getAgrupaTurma().getTurmaIntegral());
            }
        }
        itensAgrupados.add(new ListaAgrupaTurma(agrupaTurma, integral));
        agrupaTurma = new AgrupaTurma();
        integral = false;
    }

    public void editItensList(ListaAgrupaTurma lat) {
        for (int i = 0; i < itensAgrupados.size(); i++) {
            itensAgrupados.get(i).getAgrupaTurma().setTurmaIntegral(null);
            itensAgrupados.get(i).setIsIntegral(false);
        }
        for (int i = 0; i < itensAgrupados.size(); i++) {
            if (itensAgrupados.get(i).getAgrupaTurma().getTurma().getId() == lat.getAgrupaTurma().getTurma().getId()) {
                itensAgrupados.get(i).setIsIntegral(true);
            }
            itensAgrupados.get(i).getAgrupaTurma().setTurmaIntegral(lat.getAgrupaTurma().getTurma());
        }
    }

    public void removeItensList(ListaAgrupaTurma lat) {
        boolean grupoIntegral = false;
        for (int i = 0; i < itensAgrupados.size(); i++) {
            if (itensAgrupados.get(i).getAgrupaTurma().getTurma().getId() == lat.getAgrupaTurma().getTurma().getId()) {
                if (itensAgrupados.get(i).getAgrupaTurma().getTurmaIntegral().getId() == lat.getAgrupaTurma().getTurma().getId()) {
                    grupoIntegral = true;
                }
                itensAgrupados.remove(i);
            }
        }
        if (itensAgrupados.size() > 0) {
            int idTurma = itensAgrupados.get(0).getAgrupaTurma().getTurma().getId();
            for (int j = 0; j < itensAgrupados.size(); j++) {
                if (grupoIntegral) {
                    itensAgrupados.get(j).setIsIntegral(grupoIntegral);
                }
                itensAgrupados.get(j).getAgrupaTurma().setTurmaIntegral(itensAgrupados.get(j).getAgrupaTurma().getTurma());
                grupoIntegral = false;
            }
        }
    }

    public AgrupaTurma getAgrupaTurma() {
        if (GenericaSessao.exists("turmaPesquisa")) {
            agrupaTurma.setTurma((Turma) GenericaSessao.getObject("turmaPesquisa", true));
        }
        return agrupaTurma;
    }

    public void setAgrupaTurma(AgrupaTurma agrupaTurma) {
        this.agrupaTurma = agrupaTurma;
    }

    public List<AgrupaTurma> getListAgrupaTurma() {
        if (listAgrupaTurma.isEmpty()) {
            Dao dao = new Dao();
            List<AgrupaTurma> list = (List<AgrupaTurma>) dao.list(new AgrupaTurma(), true);
            for (int i = 0; i < list.size(); i++) {
                int idMemoria = list.get(i).getTurmaIntegral().getId();
                if (idMemoria == list.get(i).getTurma().getId()) {
                    listAgrupaTurma.add(list.get(i));
                }
            }
        }
        return listAgrupaTurma;
    }

    public void setListAgrupaTurma(List<AgrupaTurma> listAgrupaTurma) {
        this.listAgrupaTurma = listAgrupaTurma;
    }

    public List<ListaAgrupaTurma> getItensAgrupados() {
        return itensAgrupados;
    }

    public void setItensAgrupados(List<ListaAgrupaTurma> itensAgrupados) {
        this.itensAgrupados = itensAgrupados;
    }

    public boolean isIntegral() {
        return integral;
    }

    public void setIntegral(boolean integral) {
        this.integral = integral;
    }
}
