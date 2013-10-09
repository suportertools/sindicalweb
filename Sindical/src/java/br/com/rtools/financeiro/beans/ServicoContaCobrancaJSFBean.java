package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.ServicoContaCobranca;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.model.SelectItem;

public class ServicoContaCobrancaJSFBean {

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
        ServicosDB servicosDB = new ServicosDBToplink();
        TipoServicoDB tipoServicoDB = new TipoServicoDBToplink();
        ContaCobrancaDB contaCobrancaDB = new ContaCobrancaDBToplink();
        if ((servicoContaCobranca != null) && (servicoContaCobranca.getId() == -1)) {
            servico = servicosDB.pesquisaCodigo(Integer.valueOf(getListaServico().get(getIdServicos()).getDescription()));
            tipoServico = tipoServicoDB.pesquisaCodigo(Integer.valueOf(getListaTipoServico().get(getIdTipoServico()).getDescription()));
            contaCobranca = contaCobrancaDB.pesquisaCodigo(Integer.valueOf(getListaContaCobranca().get(getIdContaCobranca()).getDescription()));
            List serv = servContaCobrancaDB.pesquisaServPorIdServIdTipoServ(servico.getId(), tipoServico.getId());
            if (serv.isEmpty()) {
                servicoContaCobranca.setServicos(servico);
                servicoContaCobranca.setTipoServico(tipoServico);
                servicoContaCobranca.setContaCobranca(contaCobranca);
                if (servContaCobrancaDB.insert(servicoContaCobranca)) {
                    msgConfirma = "Serviço adicionado!";
                } else {
                    msgConfirma = "Erro ao Salvar!";
                }
            } else {
                msgConfirma = "Serviço já existente!";
            }
        }
        servicoContaCobranca = new ServicoContaCobranca();
        listaServicoCobranca.clear();
        return null;
    }

    public String excluir() {
        ServicoContaCobrancaDB db = new ServicoContaCobrancaDBToplink();
        servicoContaCobranca = listaServicoCobranca.get(idIndex);
        if (servicoContaCobranca.getId() != -1) {
            db.getEntityManager().getTransaction().begin();
            servicoContaCobranca = db.pesquisaCodigo(servicoContaCobranca.getId());
            if (db.delete(servicoContaCobranca)) {
                db.getEntityManager().getTransaction().commit();
                msgConfirma = "Serviço excluido!";
            } else {
                db.getEntityManager().getTransaction().rollback();
                msgConfirma = "Erro ao excluir serviço!";
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
        List<SelectItem> result = new Vector<SelectItem>();
        int i = 0;
        ServicosDB db = new ServicosDBToplink();
        List select = db.pesquisaTodos();
        while (i < select.size()) {
            result.add(new SelectItem(new Integer(i),
                    (String) ((Servicos) select.get(i)).getDescricao(),
                    Integer.toString(((Servicos) select.get(i)).getId())));
            i++;
        }
        return result;
    }

    public List<SelectItem> getListaContaCobranca() {
        List<SelectItem> result = new Vector<SelectItem>();
        int i = 0;
        ContaCobrancaDB db = new ContaCobrancaDBToplink();
        List contaCobrancas = db.pesquisaTodos();
        while (i < contaCobrancas.size()) {
            result.add(new SelectItem(new Integer(i),
                    (String) ((ContaCobranca) contaCobrancas.get(i)).getCodCedente() + " - "
                    + (String) ((ContaCobranca) contaCobrancas.get(i)).getContaBanco().getBanco().getBanco(),
                    Integer.toString(((ContaCobranca) contaCobrancas.get(i)).getId())));
            i++;
        }
        return result;
    }

    public List<SelectItem> getListaTipoServico() {
        List<SelectItem> result = new Vector<SelectItem>();
        int i = 0;
        TipoServicoDB db = new TipoServicoDBToplink();
        List tipoServicos = db.pesquisaTodos();
        while (i < tipoServicos.size()) {
            result.add(new SelectItem(new Integer(i),
                    (String) ((TipoServico) tipoServicos.get(i)).getDescricao(),
                    Integer.toString(((TipoServico) tipoServicos.get(i)).getId())));
            i++;
        }
        return result;
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