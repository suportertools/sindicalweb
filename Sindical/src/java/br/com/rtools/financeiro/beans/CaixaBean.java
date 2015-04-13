package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.dao.CaixaDao;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Usuario;
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

    private Integer idFilial;
    private int idUsuario;
    private List<SelectItem> listaFiliais;
    private List<SelectItem> listaUsuarios;
    private Caixa caixa;
    private List<Caixa> listaCaixa;
    private final ConfiguracaoFinanceiroBean cfb = new ConfiguracaoFinanceiroBean();
    
    @PostConstruct
    public void init() {
        idFilial = 0;
        listaFiliais = new ArrayList();
        caixa = new Caixa();
        listaCaixa = new ArrayList();
        listaUsuarios = new ArrayList();
        cfb.init();
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
        
        
        
        if (caixa.getDescricao().isEmpty()){
            GenericaMensagem.warn("Atenção", "Digite uma descriçao para este caixa!");
            return;
        }
        
        DaoInterface di = new Dao();
        caixa.setFilial( (listaFiliais.get(idFilial).getDescription() != null) ? (Filial) di.find(new Filial(), Integer.valueOf(listaFiliais.get(idFilial).getDescription())) : null);
        
        if (listaUsuarios.get(idUsuario).getDescription() != null){
            Usuario us = (Usuario) di.find(new Usuario(), Integer.valueOf(listaUsuarios.get(idUsuario).getDescription())) ;

            CaixaDao caixadao = new CaixaDao();
            List<Caixa> listac = caixadao.listaCaixaUsuario(us.getId());

            if (caixa.getId() == -1){
                if (!listac.isEmpty()){
                    GenericaMensagem.warn("Atençao", "Este usuário já tem Caixa definido!");
                    return;
                }
            }else{
                List<Caixa> lusx = caixadao.listaCaixaUsuario( Integer.valueOf(listaUsuarios.get(idUsuario).getDescription()) );

                if (!lusx.isEmpty() && lusx.get(0).getId() != caixa.getId()){
                    GenericaMensagem.warn("Atençao", "Este usuário já tem Caixa definido!");
                    return;
                }
            }

            caixa.setUsuario( us );
        }else{
            caixa.setUsuario( null );
        }

        
        if (caixa.getId() == -1){
            if (!di.save(caixa, true)) {
                GenericaMensagem.warn("Erro", "Não foi possível salvar Caixa!");
            } else {
                NovoLog novoLog = new NovoLog();
                String fi = (caixa.getFilial() != null) ? " - Filial: ("+ caixa.getFilial().getId() + ") " + caixa.getFilial().getFilial().getPessoa().getNome() : "- Filial: () ";
                novoLog.save(
                        "ID: " + caixa.getId()
                        + fi
                        + " - Caixa: " + caixa.getCaixa()
                        + " - Descrição: " + caixa.getDescricao()
                );
                caixa = new Caixa();
                listaCaixa.clear();
                GenericaMensagem.info("Sucesso", "Caixa adicionado com Sucesso!");
            }
        }else{
            Caixa c = (Caixa) di.find(caixa);
            String fi = (c.getFilial() != null) ? " - Filial: ("+ c.getFilial().getId() + ") " + c.getFilial().getFilial().getPessoa().getNome() : "- Filial: () ";
            String before_update = 
                        "ID: " + c.getId()
                        + fi
                        + " - Caixa: " + c.getCaixa()
                        + " - Descrição: " + c.getDescricao();
            
            if (!di.update(caixa, true)) {
                GenericaMensagem.warn("Erro", "Não foi possível alterar Caixa!");
            } else {
                NovoLog novoLog = new NovoLog();
                fi = (caixa.getFilial() != null) ? " - Filial: ("+ caixa.getFilial().getId() + ") " + caixa.getFilial().getFilial().getPessoa().getNome() : "- Filial: () ";
                
                novoLog.update(before_update,
                        "ID: " + caixa.getId()
                        + fi
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
        
        if (c.getFilial() != null){
            for (int i = 0; i < listaFiliais.size(); i++) {
                if (listaFiliais.get(i).getDescription() != null && Integer.valueOf(listaFiliais.get(i).getDescription()) == c.getFilial().getId()){
                    idFilial = i;
                }
            }
        }else{
            idFilial = 0;
        }
        
        if (caixa.getUsuario() != null){
            for (int i = 0; i < listaUsuarios.size(); i++) {
                if (listaUsuarios.get(i).getDescription() != null && Integer.valueOf(listaUsuarios.get(i).getDescription()) == c.getUsuario().getId()){
                    idUsuario = i;
                }
            }
        }else{
            idUsuario = 0;
        }
    }

    public void excluir(Caixa c) {
        DaoInterface di = new Dao();
        if (!di.delete(c, true)) {
            GenericaMensagem.warn("Erro", "Não foi possível excluir Caixa!");
        } else {
            NovoLog novoLog = new NovoLog();
            String fi = (c.getFilial() != null) ? " - Filial: ("+ c.getFilial().getId() + ") " + c.getFilial().getFilial().getPessoa().getNome() : "- Filial: () ";
            novoLog.delete(
                    "ID: " + c.getId()
                    + fi
                    + " - Caixa: " + c.getCaixa()
                    + " - Descrição: " + c.getDescricao()
            );
            listaCaixa.clear();
            GenericaMensagem.info("Sucesso", "Caixa excluido com Sucesso!");
        }
    }

    public Integer getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Integer idFilial) {
        this.idFilial = idFilial;
    }

    public List<SelectItem> getListaFiliais() {
        if (listaFiliais.isEmpty()) {
            DaoInterface di = new Dao();
            
            listaFiliais.add(new SelectItem(0, "Selecione uma Filial", null));
            
            List<Filial> list = (List<Filial>) di.list(new Filial(), true);
            for (int i = 0; i < list.size(); i++) {
                listaFiliais.add(new SelectItem(i+1, list.get(i).getFilial().getPessoa().getDocumento() + " - " + list.get(i).getFilial().getPessoa().getNome(),
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
            CaixaDao dao = new CaixaDao();
            
            listaCaixa = dao.listaTodosCaixas();
        }
        return listaCaixa;
    }

    public void setListaCaixa(List<Caixa> listaCaixa) {
        this.listaCaixa = listaCaixa;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<SelectItem> getListaUsuarios() {
        if (listaUsuarios.isEmpty()) {
            DaoInterface di = new Dao();
            List<Usuario> list = (List<Usuario>) di.list(new Usuario(), true);
            
            listaUsuarios.add(new SelectItem(0, "Selecione um Usuário", null));
            
            for (int i = 0; i < list.size(); i++) {
                listaUsuarios.add(
                        new SelectItem(
                        i+1, 
                        list.get(i).getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())
                        )
                );
            }
        }
        return listaUsuarios;
    }

    public void setListaUsuarios(List<SelectItem> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public ConfiguracaoFinanceiroBean getCfb() {
        return cfb;
    }
}
