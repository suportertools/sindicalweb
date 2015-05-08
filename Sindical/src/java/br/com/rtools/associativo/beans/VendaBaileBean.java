package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.AStatus;
import br.com.rtools.associativo.BVenda;
import br.com.rtools.associativo.EventoBaile;
import br.com.rtools.associativo.EventoBaileMapa;
import br.com.rtools.associativo.Mesa;
import br.com.rtools.associativo.db.EventoBaileDB;
import br.com.rtools.associativo.db.EventoBaileDBToplink;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class VendaBaileBean implements Serializable {

    private final List<SelectItem> listaEventoBaile = new ArrayList();
    private List<SelectItem> listaMesasBaile = new ArrayList();
    private List<EventoBaile> listae = new ArrayList();
    private int idMesaBaile = 0;
    private int indexEventoBaile = 0;
    private Fisica fisica = new Fisica();
    private BVenda venda = new BVenda();
    private List<Mesa> listaMesa = new ArrayList();
    private List<Integer> listaQuantidade = new ArrayList();
    
    private Boolean todos = false;

    public void adicionarMesa() {
        int idEmb = Integer.parseInt(listaMesasBaile.get(idMesaBaile).getDescription());
        for (int i = 0; i < listaMesa.size(); i++) {
            if (listaMesa.get(i).getEventoBaileMapa().getId() == idEmb) {
                listaMesa.remove(i);
                return;
            }
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        listaMesa.add(new Mesa(-1, null, (AStatus) sv.find(new AStatus(), 1), (EventoBaileMapa) sv.find(new EventoBaileMapa(), idEmb)));
    }

    public void removerMesa() {
        int idEmb = Integer.parseInt(listaMesasBaile.get(idMesaBaile).getDescription());
        for (int i = 0; i < listaMesa.size(); i++) {
            if (listaMesa.get(i).getEventoBaileMapa().getId() == idEmb) {
                listaMesa.remove(i);
                return;
            }
        }
    }

    public void adicionarMesa(EventoBaileMapa ebm) {
        for (int i = 0; i < listaMesa.size(); i++) {
            if (listaMesa.get(i).getEventoBaileMapa().getId() == ebm.getId()) {
                listaMesa.remove(i);
                return;
            }
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        listaMesa.add(new Mesa(-1, null, (AStatus) sv.find("AStatus", 1), ebm));
    }

    public void removerMesa(Mesa me) {
        for (int i = 0; i < listaMesa.size(); i++) {
            if (listaMesa.get(i).getEventoBaileMapa().getId() == me.getEventoBaileMapa().getId()) {
                listaMesa.remove(i);
                return;
            }
        }
    }
    
    public String retornaStatus(EventoBaileMapa ebm) {
        for (Mesa listaMesa1 : listaMesa) {
            if (listaMesa1.getEventoBaileMapa().getId() == ebm.getId()) {
                return "background-color: yellow;";
            }
        }
        return "";
    }

    public void salvar() {
        if (fisica.getId() == -1) {
            GenericaMensagem.warn("Atenção", "Pesquise uma pessoa para salvar esta venda!");
            return;
        }

        if (listaMesa.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Lista de Mesa esta vazia!");
        }
    }

    public List<Integer> getListaQuantidade() {
        if (listaQuantidade.isEmpty()) {
            for (int i = 1; i < 426; i++) {
                listaQuantidade.add(i);
            }
        }
        return listaQuantidade;
    }

    public void setListaQuantidade(List<Integer> listaQuantidade) {
        this.listaQuantidade = listaQuantidade;
    }

    public void removerFisica() {
        fisica = new Fisica();
    }

    public List<SelectItem> getListaEventoBaile() {
        if (listaEventoBaile.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            listae = sv.listaObjeto("EventoBaile");
            if (!listae.isEmpty()) {
                for (int i = 0; i < listae.size(); i++) {
                    listaEventoBaile.add(new SelectItem(
                            i,
                            listae.get(i).getEvento().getDescricaoEvento().getDescricao() + " -  "
                            + listae.get(i).getDataString() + " - ("
                            + listae.get(i).getHoraInicio() + " às  "
                            + listae.get(i).getHoraFim() + ")   "
                            + listae.get(i).getQuantidadeMesas() + " mesas  ",
                            Integer.toString((listae.get(i)).getId())));
                }
            }
        }
        return listaEventoBaile;
    }

    public int getIndexEventoBaile() {
        return indexEventoBaile;
    }

    public void setIndexEventoBaile(int indexEventoBaile) {
        this.indexEventoBaile = indexEventoBaile;
    }

    public Fisica getFisica() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            fisica = (Fisica) GenericaSessao.getObject("fisicaPesquisa", true);
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public BVenda getVenda() {
        return venda;
    }

    public void setVenda(BVenda venda) {
        this.venda = venda;
    }

    public List<Mesa> getListaMesa() {
        return listaMesa;
    }

    public void setListaMesa(List<Mesa> listaMesa) {
        this.listaMesa = listaMesa;
    }

    public List<SelectItem> getListaMesasBaile() {
        if (listaMesasBaile.isEmpty() && !listaEventoBaile.isEmpty()) {
            EventoBaileDB eventoBaileDB = new EventoBaileDBToplink();
            //List<Mesa> list = (List<Mesa>) eventoBaileDB.listaMesasEvento(Integer.parseInt(listaEventoBaile.get(indexEventoBaile).getDescription()));
            List<EventoBaileMapa> list = (List<EventoBaileMapa>) eventoBaileDB.listaBaileMapaDisponiveis(Integer.parseInt(listaEventoBaile.get(indexEventoBaile).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listaMesasBaile.add(new SelectItem(i, "" + list.get(i).getMesa(), "" + list.get(i).getId()));
            }
        }
        return listaMesasBaile;
    }

    public void setListaMesasBaile(List<SelectItem> listaMesasBaile) {
        this.listaMesasBaile = listaMesasBaile;
    }

    public int getIdMesaBaile() {
        return idMesaBaile;
    }

    public void setIdMesaBaile(int idMesaBaile) {
        this.idMesaBaile = idMesaBaile;
    }

    public Boolean getTodos() {
        return todos;
    }

    public void setTodos(Boolean todos) {
        this.todos = todos;
    }

}