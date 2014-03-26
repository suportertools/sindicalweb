package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.db.LancamentoFinanceiroDBToplink;
import br.com.rtools.pessoa.TipoDocumento;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class LancamentoFinanceiroBean implements Serializable{
    private List listaLancamento = new ArrayList();
    private int idFilial = 0;
    private int idTipoDocumento = 0;
    private List<SelectItem> listaFilial = new ArrayList<SelectItem>();
    private List<SelectItem> listaTipoDocumento = new ArrayList<SelectItem>();

    public List getListaLancamento() {
        return listaLancamento;
    }

    public void setListaLancamento(List listaLancamento) {
        this.listaLancamento = listaLancamento;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public List<SelectItem> getListaFilial() {
        if (listaFilial.isEmpty()){
            
        }
        return listaFilial;
    }

    public void setListaFilial(List<SelectItem> listaFilial) {
        this.listaFilial = listaFilial;
    }

    public List<SelectItem> getListaTipoDocumento() {
        if (listaTipoDocumento.isEmpty()){
            List<TipoDocumento> result = (new LancamentoFinanceiroDBToplink()).listaTipoDocumento();
            for (int i = 0; i < result.size(); i++){
                listaTipoDocumento.add(
                        new SelectItem(new Integer(i),
                                       result.get(i).getDescricao(),
                                       Integer.toString(result.get(i).getId())
                        )
                );
            }
        }
        return listaTipoDocumento;
    }

    public void setListaTipoDocumento(List<SelectItem> listaTipoDocumento) {
        this.listaTipoDocumento = listaTipoDocumento;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }
}
