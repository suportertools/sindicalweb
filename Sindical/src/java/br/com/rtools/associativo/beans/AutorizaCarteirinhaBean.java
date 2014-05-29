package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.AutorizaImpressaoCartao;
import br.com.rtools.associativo.ModeloCarteirinha;
import br.com.rtools.associativo.db.SocioCarteirinhaDB;
import br.com.rtools.associativo.db.SocioCarteirinhaDBToplink;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AutorizaCarteirinhaBean {
    private Usuario usuario = new Usuario();
    private Fisica fisica = new Fisica();
    private AutorizaImpressaoCartao impressaoCartao = new AutorizaImpressaoCartao();
    private int idModelo = 0;
    private List<SelectItem> listaModelo = new ArrayList<SelectItem>();
    private List<AutorizaImpressaoCartao> listaAutorizacao = new ArrayList<AutorizaImpressaoCartao>();
    
    public AutorizaCarteirinhaBean(){
        if ( GenericaSessao.getObject("sessaoUsuario") != null)
            usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        
        impressaoCartao.setFoto( ((Registro) new SalvarAcumuladoDBToplink().pesquisaCodigo(1, "Registro")).isFotoCartao());
    }

    public void autorizar(){
        
        if (Integer.valueOf(listaModelo.get(0).getDescription()) == 0){
            GenericaMensagem.warn("Erro", "Nenhum Modelo de carteirinha encontrado!");
            return;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();
        
        if (db.listaSocioCarteirinhaAutoriza(fisica.getPessoa().getId(), Integer.valueOf(listaModelo.get(idModelo).getDescription())).isEmpty()){
            GenericaMensagem.warn("Erro", "Esta Pessoa NÃO POSSUI carteirinha para ser autorizada!");
            return;
        }
        
        if (!db.listaAutoriza(fisica.getPessoa().getId(), Integer.valueOf(listaModelo.get(idModelo).getDescription())).isEmpty()){
            GenericaMensagem.warn("Erro", "Esta Pessoa já esta autorizada a imprimir carteirinha!");
            return;
        }
        
        impressaoCartao.setModeloCarteirinha((ModeloCarteirinha)sv.pesquisaCodigo( Integer.valueOf(listaModelo.get(idModelo).getDescription()), "ModeloCarteirinha"));
        impressaoCartao.setUsuario(usuario);
        impressaoCartao.setPessoa(fisica.getPessoa());
        
        sv.abrirTransacao();
        if (!sv.inserirObjeto(impressaoCartao)){
            GenericaMensagem.warn("Erro", "Não foi possível salvar autorização!");
            sv.desfazerTransacao();
            return;
        }
        
        sv.comitarTransacao();
        GenericaMensagem.info("Sucesso", "Autorização Concluída!");
    }
    
    public void excluir(AutorizaImpressaoCartao linha){
        if (linha.getHistoricoCarteirinha() != null){
            GenericaMensagem.warn("Erro", "Essa autorização não pode ser excluída!");
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        if (!sv.deletarObjeto(sv.find(linha))){
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Autorização não pode ser excluído!");
        }else{
            GenericaMensagem.info("Sucesso", "Autorização Excluída!");
            sv.comitarTransacao();
            listaAutorizacao.clear();
        }
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
    public Fisica getFisica() {
        if (GenericaSessao.getObject("fisicaPesquisa") != null){
            fisica = (Fisica) GenericaSessao.getObject("fisicaPesquisa");
            GenericaSessao.remove("fisicaPesquisa");
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public AutorizaImpressaoCartao getImpressaoCartao() {
        return impressaoCartao;
    }

    public void setImpressaoCartao(AutorizaImpressaoCartao impressaoCartao) {
        this.impressaoCartao = impressaoCartao;
    }

    public int getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(int idModelo) {
        this.idModelo = idModelo;
    }

    public List<SelectItem> getListaModelo() {
        if (listaModelo.isEmpty()){
            List<ModeloCarteirinha> result = (new SalvarAcumuladoDBToplink()).listaObjeto("ModeloCarteirinha");
            
            if (!result.isEmpty()){
                for (int i = 0; i < result.size(); i++){
                    listaModelo.add(new SelectItem(i, result.get(i).getDescricao(), String.valueOf(result.get(i).getId())));
                }
            }else
                listaModelo.add(new SelectItem(0, "Nenhum Modelo Encontrado", "0"));
        }
        return listaModelo;
    }

    public void setListaModelo(List<SelectItem> listaModelo) {
        this.listaModelo = listaModelo;
    }

    public List<AutorizaImpressaoCartao> getListaAutorizacao() {
        if (listaAutorizacao.isEmpty()){
            listaAutorizacao = new SalvarAcumuladoDBToplink().listaObjeto("AutorizaImpressaoCartao");
        }
        return listaAutorizacao;
    }

    public void setListaAutorizacao(List<AutorizaImpressaoCartao> listaAutorizacao) {
        this.listaAutorizacao = listaAutorizacao;
    }
}
