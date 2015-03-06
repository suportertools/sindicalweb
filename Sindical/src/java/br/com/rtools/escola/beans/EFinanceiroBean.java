package br.com.rtools.escola.beans;

import br.com.rtools.escola.EFinanceiro;
import br.com.rtools.escola.db.EFinanceiroDB;
import br.com.rtools.escola.db.EFinanceiroDBToplink;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.utilitarios.GenericaMensagem;
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
    private Integer idServicos = 0;
    private List<EFinanceiro> listaMultas = new ArrayList();
    private String valorMulta = "0";

    public void refreshForm() {
    }

    public String adicionar() {
        EFinanceiroDB db = new EFinanceiroDBToplink();
        ServicosDB dbs = new ServicosDBToplink();
        if (getListaServicos().isEmpty()){
            GenericaMensagem.warn("Atenção", "Lista de Multa vazia!");
            return null;
        }
        
        Servicos serv = dbs.pesquisaCodigo(Integer.parseInt(getListaServicos().get(idServicos).getDescription()));

        for (EFinanceiro listaMulta : listaMultas) {
            if (listaMulta.getMulta().getId() == serv.getId()) {
                GenericaMensagem.warn("Atenção", "Multa para esse serviço já existe!");
                eFinanceiro = new EFinanceiro();
                return "eFinanceiro";
            }
        }

        eFinanceiro.setMulta(serv);
        eFinanceiro.setNrMultaCancelamento(Moeda.substituiVirgulaFloat(valorMulta));
        if (db.insert(eFinanceiro)) {
            GenericaMensagem.info("Sucesso", "Multa Salva!");
        } else {
            GenericaMensagem.error("Erro", "Não foi possível salvar Multa!");
        }
        
        listaMultas.clear();
        eFinanceiro = new EFinanceiro();
        return "eFinanceiro";
    }

    public String excluir() {
        EFinanceiroDB db = new EFinanceiroDBToplink();
        if (db.delete(db.pesquisaCodigo(eFinanceiro.getId()))) {
            GenericaMensagem.info("Sucesso", "Multa Excluída!");
        } else {
            GenericaMensagem.error("Erro", "Não foi possível excluir Multa!");
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

    public Integer getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(Integer idServicos) {
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