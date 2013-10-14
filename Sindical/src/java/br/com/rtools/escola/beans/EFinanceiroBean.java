package br.com.rtools.escola.beans;

import br.com.rtools.escola.EFinanceiro;
import br.com.rtools.escola.db.EFinanceiroDB;
import br.com.rtools.escola.db.EFinanceiroDBToplink;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.utilitarios.Moeda;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class EFinanceiroBean implements java.io.Serializable {

    private EFinanceiro eFinanceiro = new EFinanceiro();
    private String msgConfirma = "";
    private int idServicos = 0;
    private List<EFinanceiro> listaMultas = new ArrayList();
    private String valorMulta = "0";

    public void refreshForm() {
    }

    public String adicionar() {
        EFinanceiroDB db = new EFinanceiroDBToplink();
        ServicosDB dbs = new ServicosDBToplink();
        Servicos serv = dbs.pesquisaCodigo(Integer.parseInt(getListaServicos().get(idServicos).getDescription()));

        for (int i = 0; i < listaMultas.size(); i++) {
            if (listaMultas.get(i).getMulta().getId() == serv.getId()) {
                msgConfirma = "Multa para esse serviço já existe!";
                eFinanceiro = new EFinanceiro();
                return "eFinanceiro";
            }
        }

        eFinanceiro.setMulta(serv);
        eFinanceiro.setNrMultaCancelamento(Moeda.substituiVirgulaFloat(valorMulta));
        if (db.insert(eFinanceiro)) {
            msgConfirma = "Multa salva com sucesso!";
        } else {
            msgConfirma = "Erro ao salvar multa!";
        }
        listaMultas.clear();
        eFinanceiro = new EFinanceiro();
        return "eFinanceiro";
    }

    public String excluir() {
        EFinanceiroDB db = new EFinanceiroDBToplink();
        //eFinanceiro = (EFinanceiro)htmlTable.getRowData();
        if (db.delete(db.pesquisaCodigo(eFinanceiro.getId()))) {
            msgConfirma = "Multa excluído com sucesso!";
            //listaMultas.remove(htmlTable.getRowIndex());
        } else {
            msgConfirma = "Erro ao excluir multa!";
        }
        eFinanceiro = new EFinanceiro();
        return "eFinanceiro";
    }

    public List<SelectItem> getListaServicos() {
        List<SelectItem> listaSe = new Vector<SelectItem>();
        int i = 0;
        ServicosDB db = new ServicosDBToplink();
        List select = db.pesquisaTodos(147);
        while (i < select.size()) {
            listaSe.add(new SelectItem(new Integer(i),
                    (String) ((Servicos) select.get(i)).getDescricao(),
                    Integer.toString(((Servicos) select.get(i)).getId())));
            i++;
        }
        return listaSe;
    }

    public EFinanceiro geteFinanceiro() {
        return eFinanceiro;
    }

    public void seteFinanceiro(EFinanceiro eFinanceiro) {
        this.eFinanceiro = eFinanceiro;
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

    public List<EFinanceiro> getListaMultas() {
        EFinanceiroDB db = new EFinanceiroDBToplink();
        if (listaMultas.isEmpty()) {
            listaMultas = db.pesquisaTodos();
        }
        return listaMultas;
    }

    public void setListaMultas(List<EFinanceiro> listaMultas) {
        this.listaMultas = listaMultas;
    }

    public String getValorMulta() {
        if (valorMulta.isEmpty()) {
            valorMulta = "0";
        }
        return Moeda.converteR$(valorMulta);
    }

    public void setValorMulta(String valorMulta) {
        if (valorMulta.isEmpty()) {
            valorMulta = "0";
        }
        this.valorMulta = Moeda.substituiVirgula(valorMulta);
    }
}