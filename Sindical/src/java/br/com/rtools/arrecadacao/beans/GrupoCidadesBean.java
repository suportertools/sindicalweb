package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.GrupoCidades;
import br.com.rtools.arrecadacao.MensagemConvencao;
import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.arrecadacao.db.MensagemConvencaoDB;
import br.com.rtools.arrecadacao.db.MensagemConvencaoDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GrupoCidadesBean implements Serializable {

    private GrupoCidades grupoCidades;
    private String comoPesquisa;
    private String descPesquisa;
    private String message;
    private String msgGrupoCidade;
    private List<GrupoCidades> listCidade;
    private Cidade cidade;

    @PostConstruct
    public void init() {
        grupoCidades = new GrupoCidades();
        comoPesquisa = "T";
        descPesquisa = "";
        message = "";
        msgGrupoCidade = "";
        listCidade = new ArrayList<GrupoCidades>();
        cidade = new Cidade();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("grupoCidadesBean");
        GenericaSessao.remove("cidadePesquisa");
        GenericaSessao.remove("grupoCidadesPesquisa");
        GenericaSessao.remove("grupoCidadePesquisa");
        GenericaSessao.remove("simplesPesquisa");        
    }
    
    public void clear() {
        GenericaSessao.remove("grupoCidadesBean");
    }

    public void removerGrupoCidade() {
        grupoCidades = new GrupoCidades();
        cidade = new Cidade();
    }

    public List<GrupoCidades> getListCidade() {
        GrupoCidadesDB dbGC = new GrupoCidadesDBToplink();
        if (listCidade.isEmpty()) {
            if (getGrupoCidades().getGrupoCidade().getId() != -1) {
                listCidade = dbGC.pesquisaPorGrupo(getGrupoCidades().getGrupoCidade().getId());
            }
        } else {
            if (((GrupoCidades) listCidade.get(0)).getGrupoCidade().getId() != getGrupoCidades().getGrupoCidade().getId()) {
                listCidade = dbGC.pesquisaPorGrupo(getGrupoCidades().getGrupoCidade().getId());
            }
        }
        return listCidade;
    }

    public void setListCidade(List<GrupoCidades> listCidade) {
        this.listCidade = listCidade;
    }

    public Cidade getCidade() {
        if (GenericaSessao.exists("cidadePesquisa")) {
            cidade = (Cidade) GenericaSessao.getObject("cidadePesquisa", true);
        }
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
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

    public GrupoCidades getGrupoCidades() {
        if (GenericaSessao.exists("simplesPesquisa")) {
            grupoCidades.setGrupoCidade((GrupoCidade) GenericaSessao.getObject("simplesPesquisa", true));
        }
        return grupoCidades;
    }

    public void setGrupoCidades(GrupoCidades grupoCidades) {
        this.grupoCidades = grupoCidades;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgGrupoCidade() {
        return msgGrupoCidade;
    }

    public void setMsgGrupoCidade(String msgGrupoCidade) {
        this.msgGrupoCidade = msgGrupoCidade;
    }

    public void save() {
        for(int i = 0; i < listCidade.size(); i++) {
            grupoCidades = (GrupoCidades) listCidade.get(i);
            this.saveGrupoCidade(grupoCidades);
        }
        grupoCidades = new GrupoCidades();
        listCidade.clear();
        cidade = new Cidade();
        message = "Adicionados com sucesso!";
    }

    public boolean saveGrupoCidade(GrupoCidades grupoCidades) {
        NovoLog novoLog = new NovoLog();
        DaoInterface di = new Dao();
        if (grupoCidades.getId() == -1) {
            di.openTransaction();
            if (di.save(grupoCidades)) {
                di.commit();
                novoLog.save(
                        "ID: " + grupoCidades.getId()
                        + " - Cidade: (" + grupoCidades.getCidade().getId() + ") " + grupoCidades.getCidade().getCidade()
                        + " - Grupo Cidade: (" + grupoCidades.getGrupoCidade().getId() + ") " + grupoCidades.getGrupoCidade().getDescricao()
                );
                return true;
            } else {
                di.rollback();
                return false;
            }
        } else {
            GrupoCidades gc = (GrupoCidades) di.find(grupoCidades);
            String beforeUpdate = "ID: " + gc.getId()
                    + " - Cidade: (" + gc.getCidade().getId() + ") " + gc.getCidade().getCidade()
                    + " - Grupo Cidade: (" + gc.getGrupoCidade().getId() + ") " + gc.getGrupoCidade().getDescricao();
            di.openTransaction();
            if (di.update(grupoCidades)) {
                di.commit();
                novoLog.update(beforeUpdate,
                        "ID: " + grupoCidades.getId()
                        + " - Cidade: (" + grupoCidades.getCidade().getId() + ") " + grupoCidades.getCidade().getCidade()
                        + " - Grupo Cidade: (" + grupoCidades.getGrupoCidade().getId() + ") " + grupoCidades.getGrupoCidade().getDescricao()
                );
                return true;
            } else {
                di.rollback();
                return false;
            }
        }
    }

    public boolean saveMensagemConvencao(MensagemConvencao mensagemConvencao) {
        MensagemConvencaoDB db = new MensagemConvencaoDBToplink();
        NovoLog novoLog = new NovoLog();
        if (mensagemConvencao.getId() == -1) {
            if (db.insert(mensagemConvencao)) {
                novoLog.save("Mensagem convencao inserido " + mensagemConvencao.getId() + " - Referencia: " + mensagemConvencao.getReferencia() + " - vencimento: " + mensagemConvencao.getVencimento() + " - Mensagem Convencao inseridas " + mensagemConvencao.getId() + " - Mensagem compensacao: " + mensagemConvencao.getMensagemCompensacao() + " - Mensagem contribuinte: " + mensagemConvencao.getMensagemContribuinte() + " - Convencao: " + mensagemConvencao.getConvencao() + " - Convencao: " + mensagemConvencao.getConvencao().getDescricao() + " - Grupo Cidade: " + mensagemConvencao.getGrupoCidade().getDescricao() + " - Tipo Servico: " + mensagemConvencao.getTipoServico().getDescricao() + " - Servico: " + mensagemConvencao.getServicos().getDescricao());
                return true;
            } else {
                return false;
            }
        } else {
            db.getEntityManager().getTransaction().begin();
            MensagemConvencao mc = db.pesquisaCodigo(mensagemConvencao.getId());
            String antes = "De - Referencia: " + mc.getReferencia() + " - vencimento: " + mc.getVencimento() + " - Mensagem Convencao inseridas " + mc.getId() + " - Mensagem compensacao: " + mensagemConvencao.getMensagemCompensacao() + " - Mensagem contribuinte: " + mensagemConvencao.getMensagemContribuinte() + " - Convencao: " + mensagemConvencao.getConvencao() + " - Convencao: " + mensagemConvencao.getConvencao().getDescricao() + " - Grupo Cidade: " + mensagemConvencao.getGrupoCidade().getDescricao() + " - Tipo Servico: " + mensagemConvencao.getTipoServico().getDescricao() + " - Servico: " + mc.getServicos().getDescricao();
            if (db.update(mensagemConvencao)) {
                db.getEntityManager().getTransaction().commit();
                novoLog.update(antes, mensagemConvencao.getId() + " - Referencia: " + mc.getReferencia() + " - vencimento: " + mc.getVencimento() + " - Mensagem Convencao inseridas " + mc.getId() + " - Mensagem compensacao: " + mensagemConvencao.getMensagemCompensacao() + " - Mensagem contribuinte: " + mensagemConvencao.getMensagemContribuinte() + " - Convencao: " + mensagemConvencao.getConvencao() + " - Convencao: " + mensagemConvencao.getConvencao().getDescricao() + " - Grupo Cidade: " + mensagemConvencao.getGrupoCidade().getDescricao() + " - Tipo Servico: " + mensagemConvencao.getTipoServico().getDescricao() + " - Servico: " + mc.getServicos().getDescricao());
                return true;
            } else {
                db.getEntityManager().getTransaction().rollback();
                return false;
            }
        }
    }

    public String novo() {
        grupoCidades = new GrupoCidades();
        message = "";
        return "grupoCidades";
    }

    public String delete() {
//        GrupoCidadesDB db = new GrupoCidadesDBToplink();
//        NovoLog log = new NovoLog();
//        if(grupoCidades.getId()!=-1){
//            db.getEntityManager().getTransaction().begin();
//            grupoCidades = db.pesquisaCodigo(grupoCidades.getId());
//            if (db.delete(grupoCidades)){
//                db.getEntityManager().getTransaction().commit();
//                message = "Grupo Excluida com sucesso!";
//                log.novo("Excluido", grupoCidades.getId()+" - Grupo Cidade: "+grupoCidades.getCidade().getCidade());
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            message = "Grupo não pode ser excluido!";}
//        }
        //msgGrupoCidade = "";
        //grupoCidades = new GrupoCidades();
        cidade = new Cidade();
        GenericaSessao.remove("cidadePesquisa");
        return "grupoCidades";
    }

    public List getListGrupoCidades() {
//        Pesquisa pesquisa = new Pesquisa();
        List result = null;
//        result = pesquisa.pesquisar("GrupoCidades", "descricao", descPesquisa, "descricao", comoPesquisa);
        return result;
    }

    public String edit() {
        GenericaSessao.put("grupoCidadesPesquisa", grupoCidades);
        GenericaSessao.put("linkClicado", true);
        return "grupoCidades";
    }

    public void addCidade() {
        //GrupoCidadesDB db = new GrupoCidadesDBToplink();
        if (grupoCidades.getGrupoCidade().getId() == -1) {
            msgGrupoCidade = "Pesquise um grupo Cidades";
            GenericaMensagem.warn("Erro", msgGrupoCidade);
            return;
        }

        if (cidade.getId() == -1) {
            msgGrupoCidade = "Pesquise uma Cidade";
            GenericaMensagem.warn("Erro", msgGrupoCidade);
            return;
        }

        for (int i = 0; i < listCidade.size(); i++) {
            if (listCidade.get(i).getCidade().getId() == cidade.getId()) {
                msgGrupoCidade = "Cidade já pertencente a um grupo!";
                GenericaMensagem.warn("Erro", msgGrupoCidade);
                return;
            }
        }

        DaoInterface di = new Dao();

        di.openTransaction();

        GrupoCidades gc = new GrupoCidades();
        gc.setGrupoCidade(grupoCidades.getGrupoCidade());
        gc.setCidade(cidade);
        if (!di.save(gc)) {
            message = "Erro ao salvar grupo Cidades";
            GenericaMensagem.warn("Erro", message);
            return;
        }
        NovoLog novoLog = new NovoLog();
        novoLog.save(
                "ID: " + gc.getId()
                + " - Cidade: (" + gc.getCidade().getId() + ") " + gc.getCidade().getCidade()
                + " - Grupo Cidade: (" + gc.getGrupoCidade().getId() + ") " + gc.getGrupoCidade().getDescricao()
        );
        GenericaMensagem.info("Sucesso", "Cidade Adicionada com Sucesso!");
        listCidade.clear();
        cidade = new Cidade();
        msgGrupoCidade = "";

        di.commit();
    }

    public void removeCidade(GrupoCidades gc) {
        grupoCidades = gc;
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        if (!di.delete(grupoCidades)) {
            message = "Erro ao excluir Cidade do Grupo!";
            GenericaMensagem.warn("Erro", message);
            di.rollback();
        } else {
            novoLog.delete(
                    "ID: " + grupoCidades.getId()
                    + " - Cidade: (" + grupoCidades.getCidade().getId() + ") " + grupoCidades.getCidade().getCidade()
                    + " - Grupo Cidade: (" + grupoCidades.getGrupoCidade().getId() + ") " + grupoCidades.getGrupoCidade().getDescricao()
            );
            message = "Registro excluido com Sucesso!";
            GenericaMensagem.info("Sucesso", message);
            listCidade.clear();
            di.commit();
        }
    }
}
