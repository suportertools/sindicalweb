package br.com.rtools.escola.beans;

import br.com.rtools.escola.EFinanceiro;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import java.util.ArrayList;
import java.util.List;
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
        Dao dao = new Dao();
        if (getListaServicos().isEmpty()) {
            GenericaMensagem.warn("Atenção", "Lista de Multa vazia!");
            return null;
        }

        Servicos serv = (Servicos) dao.find(new Servicos(), Integer.parseInt(getListaServicos().get(idServicos).getDescription()));

        for (EFinanceiro listaMulta : listaMultas) {
            if (listaMulta.getMulta().getId() == serv.getId()) {
                GenericaMensagem.warn("Atenção", "Multa para esse serviço já existe!");
                eFinanceiro = new EFinanceiro();
                return "eFinanceiro";
            }
        }

        eFinanceiro.setMulta(serv);
        eFinanceiro.setNrMultaCancelamento(Moeda.substituiVirgulaFloat(valorMulta));
        if (dao.save(eFinanceiro, true)) {
            GenericaMensagem.info("Sucesso", "Multa Salva!");
        } else {
            GenericaMensagem.error("Erro", "Não foi possível salvar Multa!");
        }

        listaMultas.clear();
        eFinanceiro = new EFinanceiro();
        return "eFinanceiro";
    }

    public String excluir() {
        Dao dao = new Dao();
        if (dao.delete(eFinanceiro, true)) {
            GenericaMensagem.info("Sucesso", "Multa Excluída!");
        } else {
            GenericaMensagem.error("Erro", "Não foi possível excluir Multa!");
        }
        eFinanceiro = new EFinanceiro();
        return "eFinanceiro";
    }

    public List<SelectItem> getListaServicos() {
        List<SelectItem> listaSe = new ArrayList<>();
        int i = 0;
        ServicosDB db = new ServicosDBToplink();
        List select = db.pesquisaTodos(147);
        while (i < select.size()) {
            listaSe.add(new SelectItem(i,
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
        Dao dao = new Dao();
        if (listaMultas.isEmpty()) {
            listaMultas = dao.list(new EFinanceiro());
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
