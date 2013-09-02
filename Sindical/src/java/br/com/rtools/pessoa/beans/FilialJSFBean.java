package br.com.rtools.pessoa.beans;

import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.endereco.db.CidadeDB;
import br.com.rtools.endereco.db.CidadeDBToplink;
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
        int iFilial = 0, iCidade = 0;
        FilialCidadeDB db = new FilialCidadeDBToplink();
        FilialDB dbF = new FilialDBToplink();
        CidadeDB dbC = new CidadeDBToplink();
        FilialCidade filialCidade = new FilialCidade();
        //for(int i = 0; i < listaCidade.size();i++){
        iCidade = ((Cidade) listaCidade.get(indexCidade).getArgumento0()).getId();
        //iFilial = Integer.parseInt(result.get((Integer)getListaCidade().get(i).getArgumento1()).getDescription());
        iFilial = Integer.parseInt(result.get(index).getDescription());
        if (iFilial != -1) {
            filialCidade = db.pesquisaFilialPorCidade(iCidade);
            if (filialCidade.getId() != -1) {
                filialCidade.setFilial(dbF.pesquisaCodigo(iFilial));
                db.update(filialCidade);
            } else {
                filialCidade = new FilialCidade();
                filialCidade.setCidade(dbC.pesquisaCodigo(iCidade));
                filialCidade.setFilial(dbF.pesquisaCodigo(iFilial));
                db.insert(filialCidade);
            }
        } else {
            filialCidade = db.pesquisaFilialPorCidade(iCidade);
            if (filialCidade.getId() != -1) {
                db.delete(filialCidade);
            }
        }
        //}
        result = new ArrayList();

    }

    public String novo() {
        filial = new Filial();
        return "filial";
    }

    public String excluir() {
        if (listaFilial.get(idIndex).getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            filial = (Filial) sv.pesquisaCodigo(listaFilial.get(idIndex).getId(), "Filial");
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
        FilialDB db = new FilialDBToplink();
        listaFilial = db.pesquisaTodos();
        msgConfirma = "";
        return listaFilial;
    }

    public List<Filial> getListaFilialSemMatriz() {
        if (listaFilial.isEmpty()) {
            FilialDB db = new FilialDBToplink();
            listaFilial = db.pesquisaTodos();
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
            CidadeDB db = new CidadeDBToplink();
            GrupoCidadesDB dbCids = new GrupoCidadesDBToplink();
            //List<GrupoCidades> lis = dbCids.pesquisaTodos();
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
                                    listaCidade.add(new DataObject(db.pesquisaCodigo(lis.get(i).getId()), u));
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
                        listaCidade.add(new DataObject(db.pesquisaCodigo(lis.get(i).getId()), 0));
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
            FilialDB db = new FilialDBToplink();
            List<Filial> fi = db.pesquisaTodos();
            result.add(new SelectItem(new Integer(0),
                    " -- NENHUM -- ",
                    "-1"));
            for (int i = 0; i < fi.size(); i++) {
                result.add(new SelectItem(new Integer(i + 1),
                        fi.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(fi.get(i).getId())));
            }
            this.adicionarLista = false;
//            atualizarIndexFilial();
        }
        return result;
    }

//    public void atualizarIndexFilial(){
//        FilialCidadeDB db = new FilialCidadeDBToplink();
//        FilialDB dbF = new FilialDBToplink();
//        List<FilialCidade> fc = db.pesquisaTodos();
//        //List<Filial> fili = dbF.pesquisaTodos();
//        boolean tem;
//        for(int i = 0; i < fc.size();i++){
//            tem = false;
//            for(int w = 0; w < listaCidade.size(); w++){
//                if (!tem){
//                    if(fc.get(i).getCidade().getId() == listaCidade.get(w).getCidade().getId()){
//                        for (int u = 0; u < result.size();u++){
//                            if (fc.get(i).getFilial().getId() == Integer.valueOf(result.get(u).getDescription())){
//                                listaCidade.get(w).setIndiceFilial(u);
//                                tem = true;
//                                break;
//                            }
//                        }
//                    }
//                }else break;
//            }
//        }
//    }
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
