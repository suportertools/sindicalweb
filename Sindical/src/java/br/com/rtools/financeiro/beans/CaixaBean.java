package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
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
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class CaixaBean implements Serializable {

    private int idFilial;
    private List<SelectItem> listaFiliais;
    private Caixa caixa;
    private List<Caixa> listaCaixa;

    @PostConstruct
    public void init() {
        idFilial = 0;
        listaFiliais = new ArrayList<SelectItem>();
        caixa = new Caixa();
        listaCaixa = new ArrayList<Caixa>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("caixaBean");
    }

    public void salvar() {
        if (caixa.getCaixa() == 1){
            Caixa caixaUm = (new FinanceiroDBToplink()).pesquisaCaixaUm();
            Caixa caixa_antigo = (Caixa) (new Dao().find(caixa));
            if (caixaUm.getId() != -1 && caixaUm.getId() != caixa.getId()){
                caixa.setCaixa(caixa_antigo.getCaixa());
                GenericaMensagem.error("Atenção", "Já existe um caixa 01 cadastrado!");
                return;
            }
        }
        
        DaoInterface di = new Dao();
        if (caixa.getId() == -1){
            caixa.setFilial((Filial) di.find(new Filial(), Integer.valueOf(listaFiliais.get(idFilial).getDescription())));
            if (!di.save(caixa, true)) {
                GenericaMensagem.warn("Erro", "Não foi possível salvar Caixa!");
            } else {
                NovoLog novoLog = new NovoLog();
                novoLog.save(
                        "ID: " + caixa.getId()
                        + " - Filial: (" + caixa.getFilial().getId() + ") " + caixa.getFilial().getFilial().getPessoa().getNome()
                        + " - Caixa: " + caixa.getCaixa()
                        + " - Descrição: " + caixa.getDescricao()
                );
                caixa = new Caixa();
                listaCaixa.clear();
                GenericaMensagem.info("Sucesso", "Caixa adicionado com Sucesso!");
            }
        }else{
            Caixa c = (Caixa) di.find(caixa);
            
            String before_update = 
                        "ID: " + c.getId()
                        + " - Filial: (" + c.getFilial().getId() + ") " + c.getFilial().getFilial().getPessoa().getNome()
                        + " - Caixa: " + c.getCaixa()
                        + " - Descrição: " + c.getDescricao();
            
            caixa.setFilial((Filial) di.find(new Filial(), Integer.valueOf(listaFiliais.get(idFilial).getDescription())));
            if (!di.update(caixa, true)) {
                GenericaMensagem.warn("Erro", "Não foi possível alterar Caixa!");
            } else {
                NovoLog novoLog = new NovoLog();
                novoLog.update(before_update,
                        "ID: " + caixa.getId()
                        + " - Filial: (" + caixa.getFilial().getId() + ") " + caixa.getFilial().getFilial().getPessoa().getNome()
                        + " - Caixa: " + caixa.getCaixa()
                        + " - Descrição: " + caixa.getDescricao()
                );
                caixa = new Caixa();
                listaCaixa.clear();
                GenericaMensagem.info("Sucesso", "Caixa alterado com Sucesso!");
            }
        }
    }
    
    public void editar(Caixa c){
        caixa = c;
        
        for (int i = 0; i < listaFiliais.size(); i++) {
            if (Integer.valueOf(listaFiliais.get(i).getDescription()) == c.getFilial().getId()){
                idFilial = i;
            }
        }
    }

    public void excluir(Caixa c) {
        DaoInterface di = new Dao();
        if (!di.delete(c, true)) {
            GenericaMensagem.warn("Erro", "Não foi possível excluir Caixa!");
        } else {
            NovoLog novoLog = new NovoLog();
            novoLog.delete(
                    "ID: " + c.getId()
                    + " - Filial: (" + c.getFilial().getId() + ") " + c.getFilial().getFilial().getPessoa().getNome()
                    + " - Caixa: " + c.getCaixa()
                    + " - Descrição: " + c.getDescricao()
            );
            listaCaixa.clear();
            GenericaMensagem.info("Sucesso", "Caixa excluido com Sucesso!");
        }
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public List<SelectItem> getListaFiliais() {
        if (listaFiliais.isEmpty()) {
            DaoInterface di = new Dao();
            List<Filial> list = (List<Filial>) di.list(new Filial(), true);
            for (int i = 0; i < list.size(); i++) {
                listaFiliais.add(new SelectItem(i, list.get(i).getFilial().getPessoa().getDocumento() + " - " + list.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaFiliais;
    }

    public void setListaFiliais(List<SelectItem> listaFiliais) {
        this.listaFiliais = listaFiliais;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public List<Caixa> getListaCaixa() {
        if (listaCaixa.isEmpty()) {
            DaoInterface di = new Dao();
            listaCaixa = di.list(new Caixa(), true);
        }
        return listaCaixa;
    }

    public void setListaCaixa(List<Caixa> listaCaixa) {
        this.listaCaixa = listaCaixa;
    }
}
