package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.CentroComercial;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.TipoCentroComercial;
import br.com.rtools.pessoa.db.CentroComercialDB;
import br.com.rtools.pessoa.db.CentroComercialDBToplink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class CentroComercialJSFBean {

    private CentroComercial centroComercial = new CentroComercial();
    private String msgConfirma = "";
    private int idTipos = 0;
    private int idIndex = -1;
    private List<SelectItem> listaTiposCentroComercial = new ArrayList<SelectItem>();
    private List<CentroComercial> listaCentroComercial = new ArrayList();

    public String salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        CentroComercialDB db = new CentroComercialDBToplink();
        if (centroComercial.getJuridica().getId() == -1) {
            msgConfirma = "Pesquise uma empresa antes de salvar!";
            return null;
        }

        if (!db.listaCentroComercial(Integer.parseInt(listaTiposCentroComercial.get(idTipos).getDescription()), centroComercial.getJuridica().getId()).isEmpty()) {
            msgConfirma = "Essa empresa já existe!";
            return null;
        }

        centroComercial.setTipoCentroComercial((TipoCentroComercial) sv.pesquisaCodigo(Integer.parseInt(listaTiposCentroComercial.get(idTipos).getDescription()), "TipoCentroComercial"));
        sv.abrirTransacao();
        if (centroComercial.getId() == -1) {
            if (!sv.inserirObjeto(centroComercial)) {
                msgConfirma = "Erro ao salvar Centro comercial!";
                sv.desfazerTransacao();
                return null;
            }
            msgConfirma = "Centro salvo com Sucesso!";
        } else {
            if (!sv.alterarObjeto(centroComercial)) {
                msgConfirma = "Erro ao atualizar Centro comercial!";
                sv.desfazerTransacao();
                return null;
            }
            msgConfirma = "Centro atualizado com Sucesso!";
        }
        centroComercial = new CentroComercial();
        listaCentroComercial.clear();
        sv.comitarTransacao();
        return null;
    }

    public String editar(CentroComercial cc) {
        centroComercial = cc;
        for (int i = 0; i < listaTiposCentroComercial.size(); i++) {
            if (Integer.parseInt(listaTiposCentroComercial.get(i).getDescription()) == centroComercial.getTipoCentroComercial().getId()) {
                idTipos = i;
            }
        }
        return null;
    }

    public String excluir() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        centroComercial = (CentroComercial) salvarAcumuladoDB.pesquisaCodigo(centroComercial.getId(), "CentroComercial");
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.deletarObjeto(centroComercial)) {
            salvarAcumuladoDB.comitarTransacao();
            msgConfirma = "Centro excluido com Sucesso!";
            centroComercial = new CentroComercial();
            listaCentroComercial.clear();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            msgConfirma = "Erro ao excluir cadastro!";
            return null;
        }

        return null;
    }

    public List<SelectItem> getListaTiposCentroComercial() {
        if (listaTiposCentroComercial.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<TipoCentroComercial> list = (List<TipoCentroComercial>) salvarAcumuladoDB.listaObjeto("TipoCentroComercial");
            for (int i = 0; i < list.size(); i++) {
                listaTiposCentroComercial.add(new SelectItem(
                        new Integer(i),
                        (String) (list.get(i)).getDescricao(),
                        Integer.toString((list.get(i)).getId())));
            }
        }
        return listaTiposCentroComercial;
    }

    public void setListaTiposCentroComercial(List<SelectItem> listaTiposCentroComercial) {
        this.listaTiposCentroComercial = listaTiposCentroComercial;
    }

    public CentroComercial getCentroComercial() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            centroComercial.setJuridica((Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            msgConfirma = "";
        }
        return centroComercial;
    }

    public void setCentroComercial(CentroComercial centroComercial) {
        this.centroComercial = centroComercial;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdTipos() {
        return idTipos;
    }

    public void setIdTipos(int idTipos) {
        this.idTipos = idTipos;
    }

    public List<CentroComercial> getListaCentroComercial() {
        if (listaCentroComercial.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaCentroComercial = (List<CentroComercial>) salvarAcumuladoDB.listaObjeto("CentroComercial", true);
        }
        return listaCentroComercial;
    }

    public void setListaCentroComercial(List<CentroComercial> listaCentros) {
        this.listaCentroComercial = listaCentros;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}
