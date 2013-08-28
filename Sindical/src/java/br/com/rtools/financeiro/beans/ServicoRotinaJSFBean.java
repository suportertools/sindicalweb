package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.ServicoRotina;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicoRotinaDB;
import br.com.rtools.financeiro.db.ServicoRotinaDBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.db.RotinaDB;
import br.com.rtools.seguranca.db.RotinaDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.model.SelectItem;

public class ServicoRotinaJSFBean {
    private int idServicos = 0;
    private int idRotinas = 0;
    private int idIndex = -1;
    private ServicoRotina servicoRotina = new ServicoRotina();
    private List<ServicoRotina> listaServicoRotina = new ArrayList();
    private String msgConfirma = "";
    
    public List<SelectItem> getListaServicos(){
        List<SelectItem> listaSe = new Vector<SelectItem>();
        int i = 0;
        ServicosDB db = new ServicosDBToplink();
        List select = db.pesquisaTodos();
        while (i < select.size()){
           listaSe.add(new SelectItem( new Integer(i),
                                 (String) ((Servicos) select.get(i)).getDescricao(),
                                 Integer.toString(((Servicos) select.get(i)).getId()) ));
           i++;
        }
        return listaSe;
    }

    public List<SelectItem> getListaRotinas(){
        List<SelectItem> listaRo = new Vector<SelectItem>();
        int i = 0;
        ServicoRotinaDB db = new ServicoRotinaDBToplink();
        List select = db.pesquisaTodasRotinasSemServicoOrdenado(Integer.parseInt(getListaServicos().get(idServicos).getDescription()));
        while (i < select.size()){
           listaRo.add(new SelectItem( new Integer(i),
                                 (String) ((Rotina) select.get(i)).getRotina(),
                                 Integer.toString(((Rotina) select.get(i)).getId()) ));
           i++;
        }
        return listaRo;
    }

    public String adicionarServicoRotina(){
        ServicoRotinaDB db = new ServicoRotinaDBToplink();
        ServicosDB dbs = new ServicosDBToplink();
        RotinaDB dbr = new RotinaDBToplink();
        Rotina rotina = dbr.pesquisaCodigo( Integer.parseInt(getListaRotinas().get(idRotinas).getDescription()) );
        Servicos servico = dbs.pesquisaCodigo( Integer.parseInt(getListaServicos().get(idServicos).getDescription()) );
        servicoRotina.setRotina(rotina);
        servicoRotina.setServicos(servico);
        if (db.insert(servicoRotina)){
            msgConfirma = "Registro adicionado!";
            listaServicoRotina.clear();
            servicoRotina = new ServicoRotina();
        }else
            msgConfirma = "Erro ao Salvar!";
        return null;
    }

    public String deletarServicoRotina(){
        ServicoRotinaDB db = new ServicoRotinaDBToplink();
        servicoRotina = db.pesquisaCodigo( ( (ServicoRotina)listaServicoRotina.get(idIndex) ).getId() );
        if (db.delete(servicoRotina)){
            msgConfirma = "Registro excluído!";
            listaServicoRotina.clear();
            servicoRotina = new ServicoRotina();
        }else
            msgConfirma = "Erro ao Excluir!";
        return null;
    }

    public void refreshForm(){
        
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdRotinas() {
        return idRotinas;
    }

    public void setIdRotinas(int idRotinas) {
        this.idRotinas = idRotinas;
    }

    public List<ServicoRotina> getListaServicoRotina() {
        //if (listaServicoRotina.isEmpty()){
            ServicoRotinaDB db = new ServicoRotinaDBToplink();
            listaServicoRotina = db.pesquisaServicoRotinaPorServico( Integer.parseInt(getListaServicos().get(idServicos).getDescription()) );
        //}
        return listaServicoRotina;
    }

    public void setListaServicoRotina(List<ServicoRotina> listaServicoRotina) {
        this.listaServicoRotina = listaServicoRotina;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
}
