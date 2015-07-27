package br.com.rtools.sistema.beans;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.Configuracao;
import br.com.rtools.sistema.ConfiguracaoUpload;
import br.com.rtools.sistema.Resolucao;
import br.com.rtools.sistema.TipoResolucao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Upload;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class ConfiguracaoBean implements Serializable {

    private List<Configuracao> listaConfiguracao;
    private Configuracao configuracao;
    private String mensagem;
    private String descricaoPesquisa;
    private Juridica juridica;
    private Usuario usuario;
    
    private int indexTipoResolucao;
    private List<SelectItem> listaTipoResolucao;
    private Resolucao resolucao;
    private String resolucaoUsuario;

    @PostConstruct
    public void init() {
        listaConfiguracao = new ArrayList();
        configuracao = new Configuracao();
        mensagem = "";
        descricaoPesquisa = "";
        juridica = new Juridica();
        usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        
        indexTipoResolucao = 2;
        listaTipoResolucao = new ArrayList();
        resolucao = new Resolucao();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("configuracaoBean");
        GenericaSessao.remove("configuracaoPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("configuracaoBean");
    }

    public void loadResolucao(){
        getUsuario();
        getListaTipoResolucao();
        getResolucao();
    }
    
    public String salvarResolucao(){
        Dao di = new Dao();
        
        di.openTransaction();
        
        if (getResolucao().getId() == -1){
            resolucao.setUsuario(usuario);
            resolucao.setTipoResolucao( (TipoResolucao) di.find(new TipoResolucao(), Integer.valueOf(listaTipoResolucao.get(indexTipoResolucao).getDescription())) );
            if (di.save(resolucao)) di.commit(); else di.rollback();
        }else{
            resolucao.setTipoResolucao( (TipoResolucao) di.find(new TipoResolucao(), Integer.valueOf(listaTipoResolucao.get(indexTipoResolucao).getDescription())) );
            if (di.update(resolucao)) di.commit(); else di.rollback();
        }
        
//        String retorno = GenericaSessao.getString("urlRetorno").isEmpty() ? "menuPrincipal" : GenericaSessao.getString("urlRetorno");
//        GenericaSessao.put("linkClicado", true);
        
        
        HttpServletRequest paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String urlAtual = paginaRequerida.getRequestURI();
        urlAtual = urlAtual.substring(urlAtual.lastIndexOf("/") + 1, urlAtual.lastIndexOf("."));
        
        return urlAtual;
    }
    
    public void save() {

        DaoInterface di = new Dao();

        configuracao.setJuridica(juridica);

        if (configuracao.getJuridica().getId() == -1) {
            setMensagem("Pesquisar pessoa jurídica!");
            return;
        }
        if (configuracao.getIdentifica().equals("")) {
            setMensagem("Informar o identificador do cliente, deve ser único!");
            return;
        }

        if (configuracao.getIdentifica().equals("")) {
            setMensagem("Informar o identificador do cliente, deve ser único!");
            return;
        }

        if (getConfiguracao().getId() == -1) {
            ConfiguracaoDao configuracaoDB = new ConfiguracaoDao();
            if (configuracaoDB.existeIdentificador(configuracao)) {
                setMensagem("Identificador já existe!");
                return;
            }

            if (configuracaoDB.existeIdentificadorPessoa(configuracao)) {
                setMensagem("Identificador já existe para essa pessoa!");
                return;
            }
            di.openTransaction();
            if (di.save(configuracao)) {
                di.commit();
                setMensagem("Configuração efetuada com sucesso");
            } else {
                di.rollback();
                setMensagem("Erro ao criar configuração.");
            }
        } else {
            di.openTransaction();
            if (di.update(configuracao)) {
                di.commit();
                setMensagem("Configuração atualizada com sucesso");
            } else {
                di.rollback();
                setMensagem("Erro ao atualizar configuração.");
            }
        }
    }

    public void delete() {
        DaoInterface di = new Dao();
        di.openTransaction();
        if (getConfiguracao().getId() != -1) {
            if (di.delete((Configuracao) di.find(configuracao))) {
                di.commit();
                configuracao = new Configuracao();
                setMensagem("Configuração excluída com sucesso");
            } else {
                di.commit();
                setMensagem("Erro ao excluir configuração.");
            }
        }
    }

    public String edit(Configuracao c) {
        GenericaSessao.put("linkClicado", true);
        configuracao = c;
        juridica = configuracao.getJuridica();
        return "configuracao";
    }

    public List<Configuracao> getListaConfiguracao() {
        if (listaConfiguracao.isEmpty()) {
            if (!descricaoPesquisa.equals("")) {
                ConfiguracaoDao configuracaoDB = new ConfiguracaoDao();
                listaConfiguracao = (List<Configuracao>) configuracaoDB.listaConfiguracao(descricaoPesquisa);
            } else {
                DaoInterface di = new Dao();
                listaConfiguracao = (List<Configuracao>) di.list("Configuracao");
            }
        }
        return listaConfiguracao;
    }

    public void limparListaConfiguracao() {
        listaConfiguracao.clear();
    }

    public void setListaConfiguracao(List<Configuracao> listaConfiguracao) {
        this.listaConfiguracao = listaConfiguracao;
    }

    public Configuracao getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(Configuracao configuracao) {
        this.configuracao = configuracao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public Juridica getJuridica() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public void upload(FileUploadEvent event) {
        ConfiguracaoUpload cu = new ConfiguracaoUpload();
        cu.setArquivo(event.getFile().getFileName());
        cu.setDiretorio("Imagens");
        cu.setSubstituir(true);
        cu.setRenomear("LogoCliente" + ".png");
        cu.setEvent(event);
        Upload.enviar(cu, true);
    }

    public Usuario getUsuario() {
        usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getIndexTipoResolucao() {
        return indexTipoResolucao;
    }

    public void setIndexTipoResolucao(int indexTipoResolucao) {
        this.indexTipoResolucao = indexTipoResolucao;
    }

    public List<SelectItem> getListaTipoResolucao() {
        if (listaTipoResolucao.isEmpty()){
            Dao di = new Dao();
            
            List<TipoResolucao> result = di.list(new TipoResolucao());
            
            for (int i = 0; i < result.size(); i++){
                listaTipoResolucao.add(new SelectItem(i, result.get(i).getDescricao(), ""+result.get(i).getId()));
            }
        }
        return listaTipoResolucao;
    }

    public void setListaTipoResolucao(List<SelectItem> listaTipoResolucao) {
        this.listaTipoResolucao = listaTipoResolucao;
    }

    public Resolucao getResolucao() {
        if (resolucao.getId() == -1){
            ConfiguracaoDao db = new ConfiguracaoDao();
            
            if(usuario != null)
                resolucao = db.pesquisaResolucaoUsuario(usuario.getId());
            
            if (resolucao.getId() != -1){
                for(int i = 0; i < listaTipoResolucao.size(); i++){
                    if (resolucao.getTipoResolucao().getId() == Integer.valueOf(listaTipoResolucao.get(i).getDescription())){
                        indexTipoResolucao = i;
                    }
                }
            }
        }
        return resolucao;
    }

    public void setResolucao(Resolucao resolucao) {
        this.resolucao = resolucao;
    }
}
