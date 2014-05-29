package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.ModeloCarteirinha;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ModeloCarteirinhaBean {
    private ModeloCarteirinha modelo = new ModeloCarteirinha();
    private int idCategoria = 0;
    private int idRotina = 0;
    private List<SelectItem> listaCategoria = new ArrayList<SelectItem>();
    private List<SelectItem> listaRotina = new ArrayList<SelectItem>();
    private List<ModeloCarteirinha> listaModelo = new ArrayList<ModeloCarteirinha>();
    
    public void salvar(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        if (modelo.getDescricao().isEmpty()){
            GenericaMensagem.warn("Erro", "Digite um nome para o Modelo!");
            return;
        }
        
        if (modelo.getJasper().isEmpty()){
            GenericaMensagem.warn("Erro", "Digite o caminho do Jasper!");
            return;
        }
        
        sv.abrirTransacao();
        
        modelo.setCategoria( (Categoria) sv.pesquisaCodigo(Integer.valueOf(listaCategoria.get(idCategoria).getDescription()), "Categoria") );
        modelo.setRotina( (Rotina) sv.pesquisaCodigo(Integer.valueOf(listaRotina.get(idRotina).getDescription()), "Rotina") );
        
        if (modelo.getId() == -1){
            if (!sv.inserirObjeto(modelo)){
                GenericaMensagem.warn("Erro", "Não foi possível adicionar Modelo!");
                sv.desfazerTransacao();
                return;
            }
        }else{
            if (!sv.alterarObjeto(modelo)){
                GenericaMensagem.warn("Erro", "Não foi possível alterar Modelo!");
                sv.desfazerTransacao();
                return;
            }
        }
        
        sv.comitarTransacao();
        listaModelo.clear();
        modelo = new ModeloCarteirinha();
        GenericaMensagem.info("Sucesso", "Modelo Adicionado!");
    }
    
    public void excluir(ModeloCarteirinha linha){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        if (!sv.deletarObjeto(sv.find(linha))){
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Não foi possível excluir Modelo!");
            return;
        }
        
        sv.comitarTransacao();
        listaModelo.clear();
        GenericaMensagem.info("Sucesso", "Modelo Excluído!");
    }
    
    public void editar(ModeloCarteirinha linha){
        modelo = linha;
        
        for (int i = 0; i < listaCategoria.size(); i++){
            if (Integer.valueOf(listaCategoria.get(i).getDescription()) == modelo.getCategoria().getId() ){
                idCategoria = i;
            }
        }
        
        for (int i = 0; i < listaRotina.size(); i++){
            if (Integer.valueOf(listaRotina.get(i).getDescription()) == modelo.getRotina().getId() ){
                idRotina = i;
            }
        }
    }
    
    public ModeloCarteirinha getModelo() {
        return modelo;
    }

    public void setModelo(ModeloCarteirinha modelo) {
        this.modelo = modelo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public List<SelectItem> getListaCategoria() {
        if (listaCategoria.isEmpty()){
            List<Categoria> result = (new SalvarAcumuladoDBToplink()).listaObjeto("Categoria");
            
            if (!result.isEmpty()){
                for (int i = 0; i < result.size(); i++) {
                    listaCategoria.add(new SelectItem(i, result.get(i).getCategoria(), Integer.toString(result.get(i).getId())));
                }
            }else{
                listaCategoria.add(new SelectItem(0, "Nenhuma Categoria encontrada", "0"));
            }
        }
        return listaCategoria;
    }

    public void setListaCategoria(List<SelectItem> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public List<SelectItem> getListaRotina() {
        if (listaRotina.isEmpty()){
            List<Rotina> result = new ArrayList<Rotina>();
            result.add((Rotina)new SalvarAcumuladoDBToplink().pesquisaCodigo(170, "Rotina"));
            
            if (!result.isEmpty()){
                for (int i = 0; i < result.size(); i++) {
                    listaRotina.add(new SelectItem(i, result.get(i).getRotina(), Integer.toString(result.get(i).getId())));
                }
            }else{
                listaRotina.add(new SelectItem(0, "Nenhuma Rotina encontrada", "0"));
            }
        }
        return listaRotina;
    }

    public void setListaRotina(List<SelectItem> listaRotina) {
        this.listaRotina = listaRotina;
    }

    public List<ModeloCarteirinha> getListaModelo() {
        if (listaModelo.isEmpty()){
            listaModelo = new SalvarAcumuladoDBToplink().listaObjeto("ModeloCarteirinha");
        }
        return listaModelo;
    }

    public void setListaModelo(List<ModeloCarteirinha> listaModelo) {
        this.listaModelo = listaModelo;
    }
    
    
}
