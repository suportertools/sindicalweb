package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.db.*;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

public class ConvencaoJSFBean {

    private Convencao convencao = new Convencao();
    private ConvencaoCidade conCidade = new ConvencaoCidade();
    private String comoPesquisa = "P";
    private String descPesquisa = "";
    private String msgConfirma;
    private String msgErro;
    //private String linkVoltar;
    private int idGrupoCidade = 0;
    private List<DataObject> listaGpCidade = new ArrayList();
    //private boolean adicionado = false;
    private int idIndex = -1;
    private List<Convencao> listaConvencao = new ArrayList();

    public ConvencaoJSFBean() {
    }

    public String salvar() {
        ConvencaoDB db = new ConvencaoDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        msgErro = "";
        if (db.pesquisaConvencaoDesc(convencao.getDescricao()) != null) {
            msgConfirma = "Esta convenção já existe no Sistema.";
            return null;
        }
        if (convencao.getDescricao().equals("")) {
            msgConfirma = "Digite uma Convenção por favor!";
            return null;
        }

        sv.abrirTransacao();
        if (convencao.getId() == -1) {

            if (!sv.inserirObjeto(convencao)) {
                sv.desfazerTransacao();
                msgConfirma = "Erro ao salvar Convenção!";
                return null;
            }
            msgConfirma = "Convenção salvo com Sucesso!";
        } else {
            if (!sv.alterarObjeto(convencao)) {
                sv.desfazerTransacao();
                msgConfirma = "Erro ao atualizar convenção!";
                return null;
            }
            msgConfirma = "Convenção atualizado com Sucesso!";
        }
        novo();
        sv.comitarTransacao();
        return null;
    }

    public String adicionar() {
        GrupoCidadeDB db = new GrupoCidadeDBToplink();
        ConvencaoDB dbCon = new ConvencaoDBToplink();
        ConvencaoCidadeDB dbCC = new ConvencaoCidadeDBToplink();
        List listaCidades = new ArrayList();
        GrupoCidade gpCid = new GrupoCidade();
        gpCid = db.pesquisaCodigo(Integer.valueOf(getListaGrupoCidade().get(idGrupoCidade).getDescription()));

//        if (dbCon.pesquisaConvencaoDesc(convencao.getDescricao()) != null){
//            msgErro = "Essa convenção já existe!";
//            return null;
//        }

        listaCidades = dbCC.ListaCidadesConvencao(convencao.getId(), Integer.valueOf(getListaGrupoCidade().get(idGrupoCidade).getDescription()));
        if (!listaCidades.isEmpty()) {
            msgErro = "Grupo com cidade já existente!";
            return "convencao";
        }

        if (convencao.getDescricao().equals("")) {
            msgErro = "Digite uma Convenção!";
            return "convencao";
        }

        if (convencao.getId() != -1) {
            conCidade.setConvencao(convencao);
            conCidade.setGrupoCidade(gpCid);
            if (dbCC.insert(conCidade)) {
                msgErro = "Adicionado!";
            } else {
                msgErro = "Erro ao Adicionar!";
                return "convencao";
            }
            List li = dbCC.pesquisarGruposPorConvencao(convencao.getId());
            listaGpCidade.clear();
            for (int i = 0; i < li.size(); i++) {
                listaGpCidade.add(new DataObject(li.get(i), ""));
            }
            conCidade = new ConvencaoCidade();
        } else {
            dbCon.insert(convencao);
            conCidade.setConvencao(convencao);
            conCidade.setGrupoCidade(gpCid);
            if (dbCC.insert(conCidade)) {
                msgErro = "Adicionado!";
            } else {
                msgErro = "Erro ao Adicionar!";
                return "convencao";
            }
            List li = dbCC.pesquisarGruposPorConvencao(convencao.getId());
            for (int i = 0; i < li.size(); i++) {
                listaGpCidade.add(new DataObject(li.get(i), ""));
            }

            conCidade = new ConvencaoCidade();
        }
        return "convencao";
    }

    public String btnExcluir() {
        ConvencaoCidadeDB db = new ConvencaoCidadeDBToplink();
        ConvencaoCidade conv = new ConvencaoCidade();
        GrupoCidade gpCid = new GrupoCidade();
        gpCid = (GrupoCidade) listaGpCidade.get(idIndex).getArgumento0();
        conv = db.pesquisarConvencao(convencao.getId(), gpCid.getId());
        if (conv != null) {
            if (conv.getCaminho() != null || !conv.getCaminho().isEmpty()) {
                excluirPDF(conv.getCaminho());
            }

            db.getEntityManager().getTransaction().begin();
            if (db.delete(conv)) {
                db.getEntityManager().getTransaction().commit();
                msgErro = "Excluido!";
                listaGpCidade.remove(idIndex);
            } else {
                db.getEntityManager().getTransaction().rollback();
            }
        }
        return "convencao";
    }

    public List getListaGpCidadeParaConvencao() {
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = "";
        caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/convencao/arqTemp.pdf");
        File fl = new File(caminho);
        if (convencao.getId() != -1 && idIndex != -1 && !listaGpCidade.isEmpty()) {
            ConvencaoCidadeDB db = new ConvencaoCidadeDBToplink();
            try {
                if (fl.exists()) {
                    GrupoCidade gc = (GrupoCidade) listaGpCidade.get(idIndex).getArgumento0();

                    ConvencaoCidade cc = db.pesquisarConvencao(convencao.getId(), gc.getId());
                    listaGpCidade.get(idIndex).setArgumento1(String.valueOf(gc.getId()) + "_" + convencao.getId());
                    cc.setCaminho(String.valueOf(gc.getId()) + "_" + convencao.getId());
                    db.getEntityManager().getTransaction().begin();
                    if (!db.update(cc)) {
                        db.getEntityManager().getTransaction().rollback();
                        return listaGpCidade;
                    }

                    File item = new File(caminho);
                    caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/convencao/" + String.valueOf(gc.getId()) + "_" + convencao.getId() + ".pdf");
                    fl = new File(caminho);
                    FileInputStream in = new FileInputStream(item);
                    FileOutputStream out = new FileOutputStream(fl.getPath());

                    byte[] buf = new byte[(int) item.length()];
                    int count;
                    while ((count = in.read(buf)) >= 0) {
                        out.write(buf, 0, count);
                    }
                    in.close();
                    out.flush();
                    out.close();


                    caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/convencao/arqTemp.pdf");
                    fl = new File(caminho);
                    fl.delete();
                    db.getEntityManager().getTransaction().commit();
                }
            } catch (Exception e) {
                db.getEntityManager().getTransaction().rollback();
                System.out.println(e);
            }
        } else if (fl.exists()) {
            fl.delete();
        }
        return listaGpCidade;
    }

    public String excluirPDF(String nome) {
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/convencao/" + nome + ".pdf");
        File fl = new File(caminho);

        if (fl.exists()) {
            ConvencaoCidadeDB db = new ConvencaoCidadeDBToplink();
            GrupoCidade gc = (GrupoCidade) listaGpCidade.get(idIndex).getArgumento0();

            ConvencaoCidade cc = db.pesquisarConvencao(convencao.getId(), gc.getId());
            listaGpCidade.get(idIndex).setArgumento1("");
            cc.setCaminho("");
            db.getEntityManager().getTransaction().begin();
            if (!db.update(cc)) {
                db.getEntityManager().getTransaction().rollback();
                return null;
            }

            db.getEntityManager().getTransaction().commit();
            fl.delete();
        }
        return null;
    }

    public String novo() {
        convencao = new Convencao();
        msgConfirma = "";
        msgErro = "";
        listaGpCidade = new ArrayList();
        return "convencao";
    }

    public String alterarNome() {
        return "menuPrincipal";
    }

    public String excluir() {
        ConvencaoDB db = new ConvencaoDBToplink();
        msgErro = "";
        if (convencao.getId() != -1) {
            db.getEntityManager().getTransaction().begin();
            convencao = db.pesquisaCodigo(convencao.getId());
            if (db.delete(convencao)) {
                db.getEntityManager().getTransaction().commit();
                msgConfirma = "Convenção Excluida com Sucesso!";
                convencao = new Convencao();
            } else {
                db.getEntityManager().getTransaction().rollback();
                msgConfirma = "Convenção não pode ser Excluida!";
            }
        }
        return null;
    }

    public List<Convencao> getListaConvencao() {
        ConvencaoDB db = new ConvencaoDBToplink();
        listaConvencao = db.pesquisaTodos();
        return listaConvencao;
    }

    public List<SelectItem> getListaGrupoCidade() {
        List<SelectItem> result = new Vector<SelectItem>();
        int i = 0;
        GrupoCidadeDB db = new GrupoCidadeDBToplink();
        List select = null;
        select = db.pesquisaTodos();
        while (i < select.size()) {
            result.add(new SelectItem(new Integer(i),
                    ((GrupoCidade) select.get(i)).getDescricao(),
                    Integer.toString(((GrupoCidade) select.get(i)).getId())));
            i++;
        }
        return result;
    }

    public void refreshForm() {
    }

    public String editar() {
        ConvencaoCidadeDB db = new ConvencaoCidadeDBToplink();
        convencao = (Convencao) listaConvencao.get(idIndex);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("convencaoPesquisa", convencao);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        List<GrupoCidade> li = db.pesquisarGruposPorConvencao(convencao.getId());
        listaGpCidade.clear();
        for (int i = 0; i < li.size(); i++) {
            ConvencaoCidade cc = db.pesquisarConvencao(convencao.getId(), li.get(i).getId());
            if (cc.getCaminho() == null || cc.getCaminho().isEmpty()) {
                listaGpCidade.add(new DataObject(li.get(i), ""));
            } else {
                listaGpCidade.add(new DataObject(li.get(i), cc.getCaminho()));
            }

        }
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "convencao";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public int getIdGrupoCidade() {
        return idGrupoCidade;
    }

    public void setIdGrupoCidade(int idGrupoCidade) {
        this.idGrupoCidade = idGrupoCidade;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public Convencao getConvencao() {
        return convencao;
    }

    public void setConvencao(Convencao convencao) {
        this.convencao = convencao;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getMsgErro() {
        return msgErro;
    }

    public void setMsgErro(String msgErro) {
        this.msgErro = msgErro;
    }

    public ConvencaoCidade getConCidade() {
        return conCidade;
    }

    public void setConCidade(ConvencaoCidade conCidade) {
        this.conCidade = conCidade;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}
