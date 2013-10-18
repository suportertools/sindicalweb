package br.com.rtools.pessoa.beans;

import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.FilialCidade;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.db.FilialCidadeDB;
import br.com.rtools.pessoa.db.FilialCidadeDBToplink;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDBToplink;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class FilialJSFBean {

    private Filial filial = new Filial();
    private List<Filial> listaFilial = new ArrayList();
    ;
    private String renderAdicionar = "false";
    private int idFilial = 0;
    private List<DataObject> listaCidade = new ArrayList();
    private boolean adicionarLista = false;
    private List<SelectItem> result = new ArrayList<SelectItem>();
    private String msgConfirma = "";
    private int idIndex = -1;

    public Filial getFilial() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            filial.setFilial((Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        }
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public String btnAdicionarFilial() {

        FilialDB db = new FilialDBToplink();

        if (!db.pesquisaFilialExiste(filial.getFilial().getId()).isEmpty()) {
            msgConfirma = "Filial já existe!";
            return null;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

        if (filial.getId() == -1) {
            filial.setMatriz((Juridica) sv.pesquisaCodigo(1, "Juridica"));
            if (sv.inserirObjeto(filial)) {
                msgConfirma = "Registro adicionado com sucesso!";
                sv.comitarTransacao();
            } else {
                msgConfirma = "Falha ao adicionar a filial!";
                sv.desfazerTransacao();
            }
            filial = new Filial();
            adicionarLista = true;
        } else {
            if (sv.alterarObjeto(filial)) {
                msgConfirma = "Registro atualizado com sucesso!";
                sv.comitarTransacao();
            } else {
                msgConfirma = "Falha ao atualizar a filial!";
                sv.desfazerTransacao();
            }
        }
        listaFilial.clear();
        filial = new Filial();
        renderAdicionar = "false";
        return null;
    }

    public void salvarCidadeFilial(int indexCidade, int index) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        FilialCidadeDB db = new FilialCidadeDBToplink();
        FilialCidade filialCidade;
        int iCidade = ((Cidade) listaCidade.get(indexCidade).getArgumento0()).getId();
        int iFilial = Integer.parseInt(result.get(index).getDescription());
        if (iFilial != -1) {
            filialCidade = db.pesquisaFilialPorCidade(iCidade);
            if (filialCidade.getId() == -1) {
                filialCidade = new FilialCidade();
                filialCidade.setCidade((Cidade) salvarAcumuladoDB.pesquisaCodigo(iCidade, "Cidade"));
                filialCidade.setFilial((Filial) salvarAcumuladoDB.pesquisaCodigo(iFilial, "Filial"));
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.inserirObjeto(filialCidade)) {
                    salvarAcumuladoDB.comitarTransacao();
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                }
            } else {
                filialCidade.setFilial((Filial) salvarAcumuladoDB.pesquisaCodigo(iFilial, "Filial"));
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.alterarObjeto(filialCidade)) {
                    salvarAcumuladoDB.comitarTransacao();
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                }
            }
        } else {
            filialCidade = db.pesquisaFilialPorCidade(iCidade);
            if (filialCidade.getId() != -1) {
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.deletarObjeto(filialCidade)) {
                    salvarAcumuladoDB.comitarTransacao();
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                }
            }
        }
        result = new ArrayList();
    }

    public String novo() {
        filial = new Filial();
        return "filial";
    }

    public String excluir(Filial f) {
        if (f.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            filial = (Filial) sv.pesquisaCodigo(f.getId(), "Filial");
            if (sv.deletarObjeto(filial)) {
                msgConfirma = "Filial excluída com sucesso.";
                listaFilial.clear();
                filial = new Filial();
                sv.comitarTransacao();
            } else {
                msgConfirma = "Não foi possível excluir essa filial. Verifique se há vínculos!";
                listaFilial.clear();
                filial = new Filial();
                sv.desfazerTransacao();
            }
        }
        return null;
    }

    public List<Filial> getListaFilial() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        listaFilial = (List<Filial>) salvarAcumuladoDB.listaObjeto("Filial", true);
        msgConfirma = "";
        return listaFilial;
    }

    public List<Filial> getListaFilialSemMatriz() {
        if (listaFilial.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaFilial = (List<Filial>) salvarAcumuladoDB.listaObjeto("Filial", true);
        }
        return listaFilial;
    }

    public void refreshForm() {
    }

    public String getRenderAdicionar() {
        if (filial.getFilial().getId() != -1) {
            renderAdicionar = "true";
        } else {
            renderAdicionar = "false";
        }
        return renderAdicionar;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public List<DataObject> getListaCidade() {
        if (listaCidade.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            GrupoCidadesDB dbCids = new GrupoCidadesDBToplink();
            List<Cidade> lis = dbCids.pesquisaCidadesBase();
            FilialCidadeDB dbc = new FilialCidadeDBToplink();
            List<FilialCidade> fc = dbc.pesquisaTodos();
            if (!lis.isEmpty() && fc != null) {
                boolean tem;
                for (int i = 0; i < lis.size(); i++) {
                    tem = false;
                    for (int w = 0; w < fc.size(); w++) {
                        if (lis.get(i).getId() == fc.get(w).getCidade().getId()) {
                            for (int u = 0; u < getResult().size(); u++) {
                                if (fc.get(w).getFilial().getId() == Integer.valueOf(result.get(u).getDescription())) {
                                    listaCidade.add(new DataObject( (Cidade) salvarAcumuladoDB.pesquisaCodigo(lis.get(i).getId(), "Cidade"), u));
                                    tem = true;
                                }
                                if (tem) {
                                    break;
                                }
                            }
                            if (tem) {
                                break;
                            }
                        }
                        if (tem) {
                            break;
                        }
                    }
                    if (!tem) {
                        listaCidade.add(new DataObject( (Cidade) salvarAcumuladoDB.pesquisaCodigo(lis.get(i).getId(), "Cidade"), 0));
                    }
                }
            }
        }
        return listaCidade;
    }

    public void setListaCidade(List<DataObject> listaCidade) {
        this.listaCidade = listaCidade;
    }

    public List<SelectItem> getResult() {
        if ((result.isEmpty()) || (this.adicionarLista)) {
            result.clear();
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<Filial> fi = (List<Filial>) salvarAcumuladoDB.listaObjeto("Filial", true);
            result.add(new SelectItem(new Integer(0),
                    " -- NENHUM -- ",
                    "-1"));
            for (int i = 0; i < fi.size(); i++) {
                result.add(new SelectItem(new Integer(i + 1),
                        fi.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(fi.get(i).getId())));
            }
            this.adicionarLista = false;
        }
        return result;
    }

    public void setResult(List<SelectItem> result) {
        this.result = result;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}
