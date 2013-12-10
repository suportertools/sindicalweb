package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.AEvento;
import br.com.rtools.associativo.DescricaoEvento;
import br.com.rtools.associativo.GrupoEvento;
import br.com.rtools.associativo.db.DescricaoEventoDB;
import br.com.rtools.associativo.db.DescricaoEventoDBToplink;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.model.SelectItem;

public class DescricaoEventoJSFBean {

    private DescricaoEvento descricaoEvento = new DescricaoEvento();
    private String msgConfirma = "";
    private int idGrupoEvento = 0;
    private int idServicos = 0;
    private int idIndex = -1;
    private List<DescricaoEvento> listaDescricao = new ArrayList();

    public String salvar() {
        DescricaoEventoDB db = new DescricaoEventoDBToplink();
        ServicosDB dbS = new ServicosDBToplink();
        GrupoEvento gpEvento = new GrupoEvento();
        Servicos servicos = new Servicos();
        if (descricaoEvento.getDescricao().isEmpty()) {
            msgConfirma = "Digite um nome para o Evento!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (getListaGrupoEvento().isEmpty()) {
            msgConfirma = "Lista de Grupo Evento vazia!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        gpEvento = db.pesquisaGrupoEvento(Integer.parseInt(getListaGrupoEvento().get(idGrupoEvento).getDescription()));
        if (gpEvento == null) {
            msgConfirma = "Erro ao encontrar Grupo Evento!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (getListaServicos().isEmpty()) {
            msgConfirma = "Lista Serviços vazia!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        servicos = dbS.pesquisaCodigo(Integer.parseInt(getListaServicos().get(idServicos).getDescription()));
        if (servicos == null) {
            msgConfirma = "Erro ao encontrar Serviço!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        descricaoEvento.setGrupoEvento(gpEvento);
        descricaoEvento.setServicoMovimento(servicos);
        if (db.insert(descricaoEvento)) {
            msgConfirma = "Adicionado com sucesso!";
            GenericaMensagem.info("Sucesso", msgConfirma);
            listaDescricao.clear();
        } else {
            msgConfirma = "Erro ao Salvar!";
            GenericaMensagem.warn("Erro", msgConfirma);
        }
        descricaoEvento = new DescricaoEvento();
        return null;
    }

    public String excluir(DescricaoEvento de) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        DescricaoEventoDB db = new DescricaoEventoDBToplink();
        List<AEvento> ae = new ArrayList();
        ae = db.listaEventoPorDescricao( de.getId() );
        sv.abrirTransacao();
        for (int i = 0; i < ae.size(); i++) {
            if (!sv.deletarObjeto((AEvento) sv.pesquisaCodigo(ae.get(i).getId(), "AEvento"))) {
                msgConfirma = "Erro ao excluir evento!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
                return null;
            }
        }

        if (!sv.deletarObjeto(sv.pesquisaCodigo(de.getId(), "DescricaoEvento"))) {
            msgConfirma = "Erro ao excluir descrição!";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
            return null;
        } else {
            msgConfirma = "Excluido com Sucesso!";
            GenericaMensagem.info("Erro", msgConfirma);
            sv.comitarTransacao();
            listaDescricao.clear();
            return null;
        }
    }

    public List<DescricaoEvento> getListaDescricao() {
        if (listaDescricao.isEmpty()) {
            DescricaoEventoDB db = new DescricaoEventoDBToplink();
            listaDescricao = db.pesquisaTodos();
        }
        return listaDescricao;
    }

    public void setListaDescricao(List<DescricaoEvento> listaDescricao) {
        this.listaDescricao = listaDescricao;
    }

    public List<SelectItem> getListaGrupoEvento() {
        List<SelectItem> result = new Vector<SelectItem>();
        List<GrupoEvento> select = new ArrayList();
        DescricaoEventoDB db = new DescricaoEventoDBToplink();
        select = db.listaGrupoEvento();
        for (int i = 0; i < select.size(); i++) {
            result.add(new SelectItem(new Integer(i), select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
        }
        return result;
    }

    public List<SelectItem> getListaServicos() {
        List<SelectItem> listaSe = new Vector<SelectItem>();
        int i = 0;
        ServicosDB db = new ServicosDBToplink();
        List select = db.pesquisaTodos(143);
        while (i < select.size()) {
            listaSe.add(new SelectItem(new Integer(i),
                    (String) ((Servicos) select.get(i)).getDescricao(),
                    Integer.toString(((Servicos) select.get(i)).getId())));
            i++;
        }
        return listaSe;
    }

    public void refreshForm() {
    }

    public DescricaoEvento getDescricaoEvento() {
        return descricaoEvento;
    }

    public void setDescricaoEvento(DescricaoEvento descricaoEvento) {
        this.descricaoEvento = descricaoEvento;
    }

    public int getIdGrupoEvento() {
        return idGrupoEvento;
    }

    public void setIdGrupoEvento(int idGrupoEvento) {
        this.idGrupoEvento = idGrupoEvento;
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

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}
