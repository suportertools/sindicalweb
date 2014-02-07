package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.Plano5DB;
import br.com.rtools.financeiro.db.Plano5DBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class MovimentoBancarioBean implements Serializable{
    private int idConta = 0;
    private int idServicos = 0;
    private List<SelectItem> listaConta = new ArrayList<SelectItem>();
    private List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private String valor = "";
    
    
    public List<SelectItem> getListaConta() {
        if (listaConta.isEmpty()) {
            Plano5DB db = new Plano5DBToplink();
            
            List<Plano5> result = db.pesquisaCaixaBanco();
            for (int i = 0; i < result.size(); i++) {
                listaConta.add(new SelectItem(
                        new Integer(i), 
                        result.get(i).getContaBanco().getBanco().getBanco() + " - " + result.get(i).getContaBanco().getAgencia() + " - " + result.get(i).getContaBanco().getConta(), 
                        Integer.toString((result.get(i).getId())))
                );
            }
        }
        return listaConta;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()){
            ServicosDB db = new ServicosDBToplink();
            List select = db.pesquisaTodos(4);
            if (!select.isEmpty()) {
                for (int i = 0; i < select.size(); i++) {
                    listaServicos.add(new SelectItem(
                            new Integer(i),
                            (String) ((Servicos) select.get(i)).getDescricao(),
                            Integer.toString(((Servicos) select.get(i)).getId())
                        )
                    );
                }
            }
        }
        return listaServicos;
    }
    
    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }
    
    public String getValor() {
        return Moeda.converteR$(valor);
    }

    public void setValor(String valor) {
        this.valor = Moeda.substituiVirgula(valor);
    }
}
