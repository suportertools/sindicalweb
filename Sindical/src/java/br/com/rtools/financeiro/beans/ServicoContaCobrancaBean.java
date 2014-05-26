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
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ServicoContaCobrancaBean {

    private ServicoContaCobranca servicoContaCobranca = new ServicoContaCobranca();
    private Servicos servico = new Servicos();
    private TipoServico tipoServico = new TipoServico();
    private ContaCobranca contaCobranca = new ContaCobranca();
    private String msgConfirma;
    private int idServicos = 0;
    private int idContaCobranca = 0;
    private int idTipoServico = 0;
    private int idIndex = -1;
    private List<ServicoContaCobranca> listaServicoCobranca = new ArrayList();

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
            servico = (Servicos) di.find(new Servicos(), Integer.parseInt(getListaServico().get(getIdServicos()).getDescription()));
            tipoServico = (TipoServico) di.find(new TipoServico(), Integer.parseInt(getListaTipoServico().get(getIdTipoServico()).getDescription()));
            contaCobranca = (ContaCobranca) di.find(new ContaCobranca(), Integer.parseInt(getListaContaCobranca().get(getIdContaCobranca()).getDescription()));
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
                    msgConfirma = "Serviço adicionado!";
                    GenericaMensagem.info("Sucesso", msgConfirma);
                } else {
                    msgConfirma = "Erro ao Salvar!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                }
            } else {
                msgConfirma = "Serviço já existente!";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        }
        servicoContaCobranca = new ServicoContaCobranca();
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
                msgConfirma = "Serviço excluido!";
                GenericaMensagem.info("Sucesso", msgConfirma);
            } else {
                msgConfirma = "Erro ao excluir serviço!";
                GenericaMensagem.warn("Erro", msgConfirma);
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

    public List<SelectItem> getListaServico() {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        ServicosDB db = new ServicosDBToplink();
        List list = db.pesquisaTodos();
        for (int i = 0; i < list.size(); i++) {
            selectItems.add(new SelectItem(i, (String) ((Servicos) list.get(i)).getDescricao(), Integer.toString(((Servicos) list.get(i)).getId())));
        }
        return selectItems;
    }

    public List<SelectItem> getListaContaCobranca() {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        DaoInterface di = new Dao();
        List<ContaCobranca> list = (List<ContaCobranca>) di.list(new ContaCobranca());
        for (int i = 0; i < list.size(); i++) {
            selectItems.add(new SelectItem(i,
                    list.get(i).getCodCedente() + " - "
                    + list.get(i).getContaBanco().getBanco().getBanco(),
                    Integer.toString(list.get(i).getId())));

        }
        return selectItems;
    }

    public List<SelectItem> getListaTipoServico() {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        DaoInterface di = new Dao();
        List<TipoServico> list = (List<TipoServico>) di.list(new TipoServico());
        for (int i = 0; i < list.size(); i++) {
            selectItems.add(new SelectItem(i,
                    list.get(i).getDescricao(),
                    Integer.toString(list.get(i).getId())));
        }
        return selectItems;
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
