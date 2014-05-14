package br.com.rtools.estoque.beans;

import br.com.rtools.estoque.ProdutoGrupo;
import br.com.rtools.estoque.ProdutoSubGrupo;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ProdutoSubGrupoBean {

    private List<ProdutoGrupo> listProdutoGrupo;
    private ProdutoGrupo produtoGrupo;
    private List<ProdutoSubGrupo> listProdutoSubGrupo;
    private ProdutoSubGrupo produtoSubGrupo;

    @PostConstruct
    public void init() {
        produtoGrupo = new ProdutoGrupo();
        produtoSubGrupo = new ProdutoSubGrupo();
        listProdutoGrupo = new ArrayList<ProdutoGrupo>();
        listProdutoSubGrupo = new ArrayList<ProdutoSubGrupo>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("produtoSubGrupoBean");
    }

    public void save() {
        for (ProdutoSubGrupo psg : listProdutoSubGrupo) {
            if (psg.getDescricao().equals(produtoSubGrupo.getDescricao())) {
                GenericaMensagem.warn("Validação", "Registro já existe!");
                return;
            }
        }
        if(produtoSubGrupo.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar descrição!");
            return;
        }
        if(produtoGrupo == null) {
            GenericaMensagem.warn("Validação", "Selecionar um grupo!");
            return;
        }
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        produtoSubGrupo.setProdutoGrupo(produtoGrupo);
        if (produtoSubGrupo.getId() == -1) {
            if (di.save(produtoSubGrupo, true)) {
                novoLog.save(produtoSubGrupo, true);
                GenericaMensagem.info("Sucesso", "Registro adicionado com sucesso");
            } else {
                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            ProdutoSubGrupo psgBefore = (ProdutoSubGrupo) di.find(produtoSubGrupo);
            if (di.update(produtoSubGrupo, true)) {
                novoLog.update(psgBefore, produtoSubGrupo, true);
                GenericaMensagem.info("Sucesso", "Registro atualizado com sucesso");
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }
        produtoSubGrupo = new ProdutoSubGrupo();
        listProdutoSubGrupo.clear();
    }

    public void edit(ProdutoSubGrupo psg) {
        DaoInterface di = new Dao();
        produtoSubGrupo = (ProdutoSubGrupo) di.rebind(psg);
    }

    public void delete(ProdutoSubGrupo psg) {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (di.delete(psg, true)) {
            novoLog.delete(psg, true);
            GenericaMensagem.info("Sucesso", "Registro removido com sucesso");
        } else {
            GenericaMensagem.warn("Erro", "Ao remover registro!");
        }
        produtoSubGrupo = new ProdutoSubGrupo();
        listProdutoSubGrupo.clear();
    }

    public List<ProdutoSubGrupo> getListProdutoSubGrupo() {
        if (listProdutoSubGrupo.isEmpty()) {
            DaoInterface di = new Dao();
            if(produtoGrupo != null) {
                if(produtoGrupo.getId() != -1) {
                    listProdutoSubGrupo = (List<ProdutoSubGrupo>) di.listQuery(new ProdutoSubGrupo(), "findGrupo", new Object[]{produtoGrupo.getId()});
                    if(!listProdutoSubGrupo.isEmpty()) {
                        produtoGrupo = listProdutoSubGrupo.get(0).getProdutoGrupo();
                    }
                }
            }
        }
        return listProdutoSubGrupo;
    }

    public void setListProdutoSubGrupo(List<ProdutoSubGrupo> listProdutoSubGrupo) {
        this.listProdutoSubGrupo = listProdutoSubGrupo;
    }

    public List<ProdutoGrupo> getListProdutoGrupo() {
        if (listProdutoGrupo.isEmpty()) {
            DaoInterface di = new Dao();
            listProdutoGrupo = (List<ProdutoGrupo>) di.list(new ProdutoGrupo(), true);
            if(!listProdutoGrupo.isEmpty()) {
                produtoGrupo = listProdutoGrupo.get(0);
            }
        }
        return listProdutoGrupo;
    }

    public void setListProdutoGrupo(List<ProdutoGrupo> listProdutoGrupo) {
        this.listProdutoGrupo = listProdutoGrupo;
    }

    public ProdutoGrupo getProdutoGrupo() {
        return produtoGrupo;
    }

    public void setProdutoGrupo(ProdutoGrupo produtoGrupo) {
        this.produtoGrupo = produtoGrupo;
    }

    public ProdutoSubGrupo getProdutoSubGrupo() {
        return produtoSubGrupo;
    }

    public void setProdutoSubGrupo(ProdutoSubGrupo produtoSubGrupo) {
        this.produtoSubGrupo = produtoSubGrupo;
    }

}
