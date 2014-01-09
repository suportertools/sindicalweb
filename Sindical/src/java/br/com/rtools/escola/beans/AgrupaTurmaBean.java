package br.com.rtools.escola.beans;

import br.com.rtools.escola.AgrupaTurma;
import br.com.rtools.escola.ListaAgrupaTurma;
import br.com.rtools.escola.Turma;
import br.com.rtools.escola.db.AgrupaTurmaDB;
import br.com.rtools.escola.db.AgrupaTurmaDBToplink;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class AgrupaTurmaBean implements Serializable {

    private AgrupaTurma agrupaTurma = new AgrupaTurma();
    private List<AgrupaTurma> listaAgrupaTurma = new ArrayList<AgrupaTurma>();
    private List<ListaAgrupaTurma> itensAgrupados = new ArrayList<ListaAgrupaTurma>();
    private boolean integral = false;

    public void novo() {
        agrupaTurma = new AgrupaTurma();
        listaAgrupaTurma.clear();
        itensAgrupados.clear();
        integral = false;
    }

    public void salvar() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (itensAgrupados.isEmpty()) {
            GenericaMensagem.warn("Validação", "Pesquisar turma e adicionar itens a lista!");
            return;
        }
        boolean erro = false;
        salvarAcumuladoDB.abrirTransacao();
        int idTurmaIntegral = 0;
        for (int i = 0; i < itensAgrupados.size(); i++) {
            if (itensAgrupados.get(i).isIsIntegral()) {
                idTurmaIntegral = itensAgrupados.get(i).getAgrupaTurma().getTurma().getId();
            }
        }
        AgrupaTurmaDB dB = new AgrupaTurmaDBToplink();
        for (int i = 0; i < itensAgrupados.size(); i++) {
            if (itensAgrupados.get(i).getAgrupaTurma().getId() == -1) {
                if(itensAgrupados.isEmpty()){
                    if(!((List) dB.pesquisaPorTurmaIntegral(itensAgrupados.get(i).getAgrupaTurma().getTurmaIntegral().getId())).isEmpty()){
                        GenericaMensagem.warn("Validação", "Grupo integral já cadastrado! Realizar agrupamento com o já existente.");
                        return;
                    }
                }
                if (!salvarAcumuladoDB.inserirObjeto(itensAgrupados.get(i).getAgrupaTurma())) {
                    erro = true;
                    salvarAcumuladoDB.desfazerTransacao();
                    break;
                }
            } else {
                if (!salvarAcumuladoDB.alterarObjeto(itensAgrupados.get(i).getAgrupaTurma())) {
                    erro = true;
                    salvarAcumuladoDB.desfazerTransacao();
                    break;
                }
            }
        }
        if (erro) {
            GenericaMensagem.warn("Erro", "Ao inserir registro(s)!");
            return;
        }
        salvarAcumuladoDB.comitarTransacao();
        listaAgrupaTurma.clear();
        itensAgrupados.clear();
        integral = false;
        agrupaTurma = new AgrupaTurma();
        GenericaMensagem.info("Sucesso", "Registro(s) inserido(s) com sucesso");
    }

    public void editar(AgrupaTurma at) {
        AgrupaTurmaDB dB = new AgrupaTurmaDBToplink();
        List<AgrupaTurma> list = (List<AgrupaTurma>) dB.pesquisaPorTurmaIntegral(at.getTurmaIntegral().getId());
        itensAgrupados.clear();
        boolean turmaIntegral = false;
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

    public void remover(AgrupaTurma at) {
        AgrupaTurmaDB dB = new AgrupaTurmaDBToplink();
        List<AgrupaTurma> list = (List<AgrupaTurma>) dB.pesquisaPorTurmaIntegral(at.getTurmaIntegral().getId());
        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
        if (!list.isEmpty()) {
            sadb.abrirTransacao();
            for (int i = 0; i < list.size(); i++) {
                AgrupaTurma at1 = (AgrupaTurma) sadb.pesquisaCodigo(list.get(i).getId(), "AgrupaTurma");
                if (!sadb.deletarObjeto(at1)) {
                    sadb.desfazerTransacao();
                    GenericaMensagem.warn("Erro", "Ao remover registro(s)!");
                    return;
                }
            }
            sadb.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Registro(s) removido(s)");
        }
        listaAgrupaTurma.clear();
        itensAgrupados.clear();
    }

    public void adicionarItem() {
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

    public void editaItensLista(ListaAgrupaTurma lat) {
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

    public void removeItensLista(ListaAgrupaTurma lat) {
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
                if(grupoIntegral) {
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

    public List<AgrupaTurma> getListaAgrupaTurma() {
        if (listaAgrupaTurma.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            List<AgrupaTurma> list = (List<AgrupaTurma>) dB.listaObjeto("AgrupaTurma", true);
            for (int i = 0; i < list.size(); i++) {
                int idMemoria = list.get(i).getTurmaIntegral().getId();
                if (idMemoria == list.get(i).getTurma().getId()) {
                    listaAgrupaTurma.add(list.get(i));
                }
            }
        }
        return listaAgrupaTurma;
    }

    public void setListaAgrupaTurma(List<AgrupaTurma> listaAgrupaTurma) {
        this.listaAgrupaTurma = listaAgrupaTurma;
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
