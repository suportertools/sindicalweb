package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.ServicoRotina;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicoRotinaDB;
import br.com.rtools.financeiro.db.ServicoRotinaDBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.seguranca.Rotina;
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
public class ServicoRotinaBean implements Serializable {

    private ServicoRotina servicoRotina;
    private List<ServicoRotina> listServicoRotina;
    /**
     * <ul>
     * <li>[0] Serviços</li>
     * <li>[1] Rotina</li>
     * </ul>
     */
    private Integer[] index;
    private List[] listSelectItem;

    @PostConstruct
    public void init() {
        index = new Integer[]{0, 0};
        listSelectItem = new ArrayList[]{
            new ArrayList<SelectItem>(),
            new ArrayList<SelectItem>()
        };
        servicoRotina = new ServicoRotina();
        listServicoRotina = new ArrayList<ServicoRotina>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("servicoRotinaBean");
    }

    public List<SelectItem> getListServicos() {
        if (listSelectItem[0].isEmpty()) {
            ServicosDB servicosDB = new ServicosDBToplink();
            List<Servicos> list = (List<Servicos>) servicosDB.listaServicoSituacaoAtivo();

            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<>();
            }
        }
        return listSelectItem[0];
    }

    public List<SelectItem> getListRotinas() {
        if (listSelectItem[1].isEmpty()) {
            if (listSelectItem[0].isEmpty()) {
                return listSelectItem[0];
            }
            ServicoRotinaDB db = new ServicoRotinaDBToplink();
            List<Rotina> list = (List<Rotina>) db.pesquisaTodasRotinasSemServicoOrdenado(Integer.parseInt(getListServicos().get(index[0]).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[1].add(new SelectItem(i, list.get(i).getRotina(), Integer.toString((list.get(i).getId()))));
            }
            if (listSelectItem[1].isEmpty()) {
                listSelectItem[1] = new ArrayList<SelectItem>();
            }
        }
        return listSelectItem[1];
    }

    public void clear(int type) {
        if (type == 1) {
            getListRotinas().clear();
            listServicoRotina.clear();
            index[1] = 0;
        } else if (type == 2) {
            listServicoRotina.clear();
        }
    }

    public void add() {
        DaoInterface di = new Dao();
        ServicoRotinaDB servicoRotinaDB = new ServicoRotinaDBToplink();
        if (getListServicos().isEmpty()) {
            GenericaMensagem.warn("Validação", "Serviços não existe!");
            return;
        }
        if (getListRotinas().isEmpty()) {
            GenericaMensagem.warn("Erro", "Não existem rotinas!");
            return;
        }
        servicoRotina.setServicos((Servicos) di.find(new Servicos(), Integer.parseInt(getListServicos().get(index[0]).getDescription())));
        servicoRotina.setRotina((Rotina) di.find(new Rotina(), Integer.parseInt(getListRotinas().get(index[1]).getDescription())));
        if (servicoRotinaDB.existeServicoRotina(servicoRotina.getServicos().getId(), servicoRotina.getRotina().getId())) {
            GenericaMensagem.warn("Erro", "Serviço Rotina já existe!");
            return;
        }
        if (di.save(servicoRotina, true)) {
            NovoLog log = new NovoLog();
            log.save(
                    "ID: " + servicoRotina.getId()
                    + " - Serviços: (" + servicoRotina.getServicos().getId() + ") " + servicoRotina.getServicos().getDescricao()
                    + " - Rotina: (" + servicoRotina.getRotina().getId() + ") " + servicoRotina.getRotina().getRotina()
            );
            GenericaMensagem.info("Sucesso", "Registro adicionado");
            listServicoRotina.clear();
            getListRotinas().clear();
        } else {
            GenericaMensagem.warn("Erro", "Erro ao Salvar!");
        }
        servicoRotina = new ServicoRotina();
    }

    public void delete(ServicoRotina sr) {
        if (sr.getId() != -1) {
            servicoRotina = sr;
        }
        if (sr.getId() == -1) {
            return;
        }
        DaoInterface di = new Dao();
        if (di.delete(servicoRotina, true)) {
            NovoLog log = new NovoLog();
            log.delete(
                    "ID: " + servicoRotina.getId()
                    + " - Serviços: (" + servicoRotina.getServicos().getId() + ") " + servicoRotina.getServicos().getDescricao()
                    + " - Rotina: (" + servicoRotina.getRotina().getId() + ") " + servicoRotina.getRotina().getRotina()
            );
            GenericaMensagem.info("Sucesso", "Registro excluído");
            listServicoRotina.clear();
            getListRotinas().clear();
        } else {
            GenericaMensagem.warn("Erro", "Erro ao Excluir!");
        }
        servicoRotina = new ServicoRotina();
    }

    public List<ServicoRotina> getListServicoRotina() {
        if (listServicoRotina.isEmpty()) {
            if (!getListServicos().isEmpty()) {
                ServicoRotinaDB db = new ServicoRotinaDBToplink();
                listServicoRotina = db.pesquisaServicoRotinaPorServico(Integer.parseInt(getListServicos().get(index[0]).getDescription()));
            }
        }
        return listServicoRotina;
    }

    public void setListServicoRotina(List<ServicoRotina> listServicoRotina) {
        this.listServicoRotina = listServicoRotina;
    }

    public ServicoRotina getServicoRotina() {
        return servicoRotina;
    }

    public void setServicoRotina(ServicoRotina servicoRotina) {
        this.servicoRotina = servicoRotina;
    }

    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }
}
