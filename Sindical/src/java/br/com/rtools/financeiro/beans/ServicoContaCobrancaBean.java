package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.ServicoContaCobranca;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ServicoContaCobrancaBean implements Serializable {

    private ServicoContaCobranca servicoContaCobranca = new ServicoContaCobranca();
    private int idServicos = 0;
    private int idContaCobranca = 0;
    private int idTipoServico = 0;
    private final List<SelectItem> listaServico = new ArrayList();
    private final List<SelectItem> listaContaCobranca = new ArrayList();
    private final List<SelectItem> listaTipoServico = new ArrayList();
    private int idIndex = -1;
    private List<ServicoContaCobranca> listaServicoCobranca = new ArrayList();

    public ServicoContaCobrancaBean() {
        getListaServico();
        getListaContaCobranca();
        getListaTipoServico();
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdContaCobranca() {
        return idContaCobranca;
    }

    public void setIdContaCobranca(int idContaCobranca) {
        this.idContaCobranca = idContaCobranca;
    }

    public int getIdTipoServico() {
        return idTipoServico;
    }

    public void setIdTipoServico(int idTipoServico) {
        this.idTipoServico = idTipoServico;
    }

    public String adicionar() {
        ServicoContaCobrancaDB servContaCobrancaDB = new ServicoContaCobrancaDBToplink();
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if ((servicoContaCobranca != null) && (servicoContaCobranca.getId() == -1)) {
            Servicos servico = (Servicos) di.find(new Servicos(), Integer.parseInt(listaServico.get(idServicos).getDescription()));
            TipoServico tipoServico = (TipoServico) di.find(new TipoServico(), Integer.parseInt(listaTipoServico.get(idTipoServico).getDescription()));
            ContaCobranca contaCobranca = (ContaCobranca) di.find(new ContaCobranca(), Integer.parseInt(listaContaCobranca.get(idContaCobranca).getDescription()));
            List serv = servContaCobrancaDB.pesquisaServPorIdServIdTipoServ(servico.getId(), tipoServico.getId());
            if (serv.isEmpty()) {
                servicoContaCobranca.setServicos(servico);
                servicoContaCobranca.setTipoServico(tipoServico);
                servicoContaCobranca.setContaCobranca(contaCobranca);
                if (di.save(servicoContaCobranca, true)) {
                    novoLog.save(
                            "ID: " + servicoContaCobranca.getId()
                            + " - Serviço: (" + servicoContaCobranca.getServicos().getId() + ") " + servicoContaCobranca.getServicos().getDescricao()
                            + " - Tipo Serviço: (" + servicoContaCobranca.getTipoServico().getId() + ") " + servicoContaCobranca.getTipoServico().getDescricao()
                            + " - Conta Cobrança: " + servicoContaCobranca.getContaCobranca().getId()
                    );
                    GenericaMensagem.info("Sucesso", "Serviço adicionado!");
                } else {
                    GenericaMensagem.warn("Erro", "Erro ao Salvar!");
                }
            } else {
                GenericaMensagem.warn("Erro", "Serviço já existente!");
            }
        }
        servicoContaCobranca = new ServicoContaCobranca();
        listaServicoCobranca.clear();
        return null;
    }

    public String adicionarTodosTipo() {
        ServicoContaCobrancaDB servContaCobrancaDB = new ServicoContaCobrancaDBToplink();
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        servicoContaCobranca = new ServicoContaCobranca();

        Servicos servico = (Servicos) di.find(new Servicos(), Integer.parseInt(listaServico.get(idServicos).getDescription()));
        ContaCobranca contaCobranca = (ContaCobranca) di.find(new ContaCobranca(), Integer.parseInt(getListaContaCobranca().get(getIdContaCobranca()).getDescription()));

        for (SelectItem ts : listaTipoServico) {
            TipoServico tipoServico = (TipoServico) di.find(new TipoServico(), Integer.parseInt(ts.getDescription()));
            List serv = servContaCobrancaDB.pesquisaServPorIdServIdTipoServ(servico.getId(), tipoServico.getId());

            if (serv.isEmpty()) {
                servicoContaCobranca.setServicos(servico);
                servicoContaCobranca.setTipoServico(tipoServico);
                servicoContaCobranca.setContaCobranca(contaCobranca);
                if (di.save(servicoContaCobranca, true)) {
                    novoLog.save(
                            "ID: " + servicoContaCobranca.getId()
                            + " - Serviço: (" + servicoContaCobranca.getServicos().getId() + ") " + servicoContaCobranca.getServicos().getDescricao()
                            + " - Tipo Serviço: (" + servicoContaCobranca.getTipoServico().getId() + ") " + servicoContaCobranca.getTipoServico().getDescricao()
                            + " - Conta Cobrança: " + servicoContaCobranca.getContaCobranca().getId()
                    );
                    GenericaMensagem.info("Sucesso", tipoServico.getDescricao() + " adicionado!");
                } else {
                    GenericaMensagem.warn("Erro", "Erro ao Salvar!");
                }
            } else {
                GenericaMensagem.warn("Erro", "Tipo Serviço " + tipoServico.getDescricao() + " já existente para esse Serviço!");
            }
            servicoContaCobranca = new ServicoContaCobranca();
        }

        listaServicoCobranca.clear();
        return null;
    }

    public String excluir(ServicoContaCobranca scc) {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        servicoContaCobranca = scc;
        if (servicoContaCobranca.getId() != -1) {
            if (di.delete(servicoContaCobranca, true)) {
                novoLog.delete(
                        "ID: " + servicoContaCobranca.getId()
                        + " - Serviço: (" + servicoContaCobranca.getServicos().getId() + ") " + servicoContaCobranca.getServicos().getDescricao()
                        + " - Tipo Serviço: (" + servicoContaCobranca.getTipoServico().getId() + ") " + servicoContaCobranca.getTipoServico().getDescricao()
                        + " - Conta Cobrança: " + servicoContaCobranca.getContaCobranca().getId()
                );
                GenericaMensagem.info("Sucesso", "Serviço excluido!");
            } else {
                GenericaMensagem.warn("Erro", "Erro ao excluir serviço!");
            }
        }
        servicoContaCobranca = new ServicoContaCobranca();
        listaServicoCobranca.clear();
        return null;
    }

    public String novo() {
        setIdContaCobranca(0);
        setIdServicos(0);
        setIdTipoServico(0);
        listaServicoCobranca.clear();
        return "servicoContaCobranca";
    }

    public void refreshForm() {
    }

    public final List<SelectItem> getListaServico() {
        if (listaServico.isEmpty()) {
            List<Servicos> list = new Dao().list(new Servicos(), true);
            for (int i = 0; i < list.size(); i++) {
                listaServico.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaServico;
    }

    public final List<SelectItem> getListaContaCobranca() {
        if (listaContaCobranca.isEmpty()) {
            DaoInterface di = new Dao();
            List<ContaCobranca> list = (List<ContaCobranca>) di.list(new ContaCobranca());
            for (int i = 0; i < list.size(); i++) {
                listaContaCobranca.add(new SelectItem(i,
                        list.get(i).getCodCedente() + " - "
                        + list.get(i).getContaBanco().getBanco().getBanco(),
                        Integer.toString(list.get(i).getId())));

            }
        }
        return listaContaCobranca;
    }

    public final List<SelectItem> getListaTipoServico() {
        if (listaTipoServico.isEmpty()) {
            DaoInterface di = new Dao();
            List<TipoServico> list = (List<TipoServico>) di.list(new TipoServico());
            for (int i = 0; i < list.size(); i++) {
                listaTipoServico.add(new SelectItem(i,
                        list.get(i).getDescricao(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaTipoServico;
    }

    public ServicoContaCobranca getServicoContaCobranca() {
        return servicoContaCobranca;
    }

    public void setServicoContaCobranca(ServicoContaCobranca servicoContaCobranca) {
        this.servicoContaCobranca = servicoContaCobranca;
    }

    public List<ServicoContaCobranca> getListaServicoCobranca() {
        if (listaServicoCobranca.isEmpty()) {
            ServicoContaCobrancaDB db = new ServicoContaCobrancaDBToplink();
            listaServicoCobranca = db.pesquisaTodos();
        }
        return listaServicoCobranca;
    }

    public void setListaServicoCobranca(List<ServicoContaCobranca> listaServicoCobranca) {
        this.listaServicoCobranca = listaServicoCobranca;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}
