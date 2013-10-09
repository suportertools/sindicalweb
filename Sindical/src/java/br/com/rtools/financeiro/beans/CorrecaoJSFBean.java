package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Correcao;
import br.com.rtools.financeiro.Indice;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.CorrecaoDB;
import br.com.rtools.financeiro.db.CorrecaoDBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.model.SelectItem;

public class CorrecaoJSFBean {

    private int idServicos = 0;
    private int idIndices = 0;
    private int idIndex = -1;
    private Correcao correcao = new Correcao();
    private String msgConfirma = "";
    private List listaCorrecao = new ArrayList();

    public String salvar() {
        CorrecaoDB db = new CorrecaoDBToplink();
        ServicosDB dbSer = new ServicosDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Indice indice = new Indice();
        Servicos servico = new Servicos();

        indice = (Indice) sv.pesquisaCodigo(Integer.valueOf(getListaIndices().get(idIndices).getDescription()), "Indice");
        servico = dbSer.pesquisaCodigo(Integer.valueOf(getListaServico().get(idServicos).getDescription()));
        List dd = new ArrayList();

        correcao.setIndice(indice);
        correcao.setServicos(servico);

        if (correcao.getId() == -1) {
            if (DataHoje.validaReferencias(correcao.getReferenciaInicial(), correcao.getReferenciaFinal())) {
                dd = db.pesquisaRefValida(servico, correcao.getReferenciaInicial(), correcao.getReferenciaFinal());
                if (Integer.parseInt(String.valueOf((Long) dd.get(0))) == 0) {
                    if (db.insert(correcao)) {
                        msgConfirma = "Adicionado!";
                        correcao = new Correcao();
                        idIndices = 0;
                        idServicos = 0;
                    } else {
                        msgConfirma = "Erro ao Salvar!";
                    }
                } else {
                    msgConfirma = "Correção já existente!";
                }
            } else {
                msgConfirma = "Referencia Invalida!";
            }
        } else if (DataHoje.validaReferencias(correcao.getReferenciaInicial(), correcao.getReferenciaFinal())) {
            if (db.update(correcao)) {
                msgConfirma = "Correção atualizada!";
                correcao = new Correcao();
                idIndices = 0;
                idServicos = 0;
            } else {
                msgConfirma = "Erro ao atualizar!";
            }
        } else {
            msgConfirma = "Referencia Invalida!";
        }
        listaCorrecao.clear();
        return null;
    }

    public String novo() {
        correcao = new Correcao();
        idIndices = 0;
        idServicos = 0;
        msgConfirma = "";
        listaCorrecao.clear();
        return "correcao";
    }

    public String btnExcluir() {
        CorrecaoDB db = new CorrecaoDBToplink();
        Correcao cor = new Correcao();
        correcao = (Correcao) listaCorrecao.get(idIndex);
        cor = db.pesquisaCodigo(correcao.getId());
        if (db.delete(cor)) {
            msgConfirma = "Correção Excluida!";
        } else {
            msgConfirma = "Erro ao excluir Correção!";
        }
        correcao = new Correcao();
        listaCorrecao.clear();
        return null;
    }

    public String editar() {
        correcao = (Correcao) listaCorrecao.get(idIndex);
        for (int i = 0; i < getListaServico().size(); i++) {
            if (Integer.parseInt(getListaServico().get(i).getDescription()) == correcao.getServicos().getId()) {
                setIdServicos(i);
                break;
            }
        }
        for (int i = 0; i < getListaIndices().size(); i++) {
            if (Integer.parseInt(getListaIndices().get(i).getDescription()) == correcao.getIndice().getId()) {
                setIdIndices(i);
                break;
            }
        }
        return null;
    }

    public List<Correcao> getListaCorrecao() {
        if (listaCorrecao.isEmpty()) {
            CorrecaoDB db = new CorrecaoDBToplink();
            listaCorrecao = db.pesquisaTodos();
        }
        return listaCorrecao;
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

    public List<SelectItem> getListaIndices() {
        List<SelectItem> result = new Vector<SelectItem>();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        List select = sv.listaObjetoGenericoOrdem("Indice");
        for (int i = 0; i < select.size(); i++) {
            result.add(new SelectItem(new Integer(i),
                    ((Indice) select.get(i)).getDescricao(),
                    Integer.toString(((Indice) select.get(i)).getId())));
        }
        return result;
    }

    public void refreshForm() {
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdIndices() {
        return idIndices;
    }

    public void setIdIndices(int idIndices) {
        this.idIndices = idIndices;
    }

    public Correcao getCorrecao() {
        return correcao;
    }

    public void setCorrecao(Correcao correcao) {
        this.correcao = correcao;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public void setListaCorrecao(List listaCorrecao) {
        this.listaCorrecao = listaCorrecao;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}
