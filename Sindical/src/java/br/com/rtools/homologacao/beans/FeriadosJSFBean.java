package br.com.rtools.homologacao.beans;

import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.homologacao.Feriados;
import br.com.rtools.homologacao.db.FeriadosDB;
import br.com.rtools.homologacao.db.FeriadosDBToplink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.model.SelectItem;

public class FeriadosJSFBean {

    private Feriados feriados = new Feriados();
    private String msgConfirma = "";
    private List<SelectItem> listaCidade = new Vector<SelectItem>();
    private int idCidade = 0;
    private int idIndex = -1;
    private boolean chkCidades = false;
    private List<Feriados> listaFeriados = new ArrayList();

    public String salvar() {
        FeriadosDB db = new FeriadosDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (feriados.getId() == -1) {
            if (feriados.getNome().equals("")) {
                msgConfirma = "Digite o nome do Feriado.";
                return null;
            }
            if (feriados.getData().equals("") && feriados.getData().length() < 7) {
                msgConfirma = "Data Inválida.";
                return null;
            }
            if (chkCidades) {
                feriados.setCidade( (Cidade) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaCidade.get(idCidade).getDescription()), "Cidade"));
            } else {
                feriados.setCidade(null);
            }
            if (db.exiteFeriadoCidade(feriados)) {
                msgConfirma = "Feriado ja Cadastrado!";
                return null;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(feriados)) {
                salvarAcumuladoDB.comitarTransacao();
                msgConfirma = "Feriado adicionado com sucesso!";
                feriados = new Feriados();
                listaFeriados.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao Salvar Feriado!";
            }
        }
        return null;
    }

    public String excluir(Feriados fer) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        fer = (Feriados) salvarAcumuladoDB.pesquisaCodigo(fer.getId(), "FeriaFeriadoso");
        if (fer.getId() != -1) {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(fer)) {
                salvarAcumuladoDB.comitarTransacao();
                msgConfirma = "Feriado Excluído com sucesso!";
                listaFeriados.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao Excluído Feriado!";
            }
        }
        return null;
    }

    public List<SelectItem> getListaCidade() {
        if (chkCidades) {
            if (listaCidade.isEmpty()) {
                GrupoCidadesDB db = new GrupoCidadesDBToplink();
                List<Cidade> listaCidades = db.pesquisaTodosCidadeAgrupada();
                for (int i = 0; i < listaCidades.size(); i++) {
                    listaCidade.add(new SelectItem(new Integer(i),
                            (String) listaCidades.get(i).getCidade() + " - " + listaCidades.get(i).getUf(),
                            Integer.toString((listaCidades.get(i)).getId())));
                }
            }
        } else {
            listaCidade.clear();
        }
        return listaCidade;
    }

    public void refreshForm() {
    }

    public Feriados getFeriados() {
        return feriados;
    }

    public void setFeriados(Feriados feriados) {
        this.feriados = feriados;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(int idCidade) {
        this.idCidade = idCidade;
    }

    public boolean isChkCidades() {
        return chkCidades;
    }

    public void setChkCidades(boolean chkCidades) {
        this.chkCidades = chkCidades;
    }

    public List<Feriados> getListaFeriados() {
        if (listaFeriados.isEmpty()) {
            FeriadosDB db = new FeriadosDBToplink();
            listaFeriados = db.pesquisaTodos();
        }
        return listaFeriados;
    }

    public void setListaFeriados(List<Feriados> listaFeriados) {
        this.listaFeriados = listaFeriados;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}