package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.ConvencaoServico;
import br.com.rtools.arrecadacao.dao.ConvencaoServicoDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;


@ManagedBean
@SessionScoped
public class ConvencaoServicoBean {
    private ConvencaoServico convencaoServico = new ConvencaoServico();
    private ConvencaoServico convencaoServicoDelete = new ConvencaoServico();
    private Integer idConvencaoCidade = 0;
    private List<SelectItem> listaConvencaoCidade = new ArrayList();
    private List<ConvencaoServico> listaConvencaoServico = new ArrayList();
    
    @PostConstruct
    public void init(){
        loadListaConvencaoCidade();
        loadListaConvencaoServico();
    }
    
    @PreDestroy
    public void destroy(){
        GenericaSessao.remove("convencaoServicoBean");
    }
    
    public void loadListaConvencaoServico(){
        listaConvencaoServico.clear();
        
        listaConvencaoServico = new ConvencaoServicoDao().listaConvencaoServico();
    }
    
    public void save(){
        if (convencaoServico.getClausula().isEmpty()){
            GenericaMensagem.warn("Atenção", "Digite uma Clausula!");
            return;
        }
        
        Dao dao = new Dao();
        
        dao.openTransaction();
        
        if (convencaoServico.getId() == -1){
            if (!new ConvencaoServicoDao().listaConvencaoServico(Integer.valueOf(listaConvencaoCidade.get(idConvencaoCidade).getDescription())).isEmpty()){
                dao.rollback();
                GenericaMensagem.warn("Atenção", "Clausula já existente para esta Convenção Cidade");
                return;
            }
            
            convencaoServico.setConvencaoCidade((ConvencaoCidade) dao.find(new ConvencaoCidade(), Integer.valueOf(listaConvencaoCidade.get(idConvencaoCidade).getDescription())));
            if (!dao.save(convencaoServico)){
                dao.rollback();
                GenericaMensagem.error("Atenção", "Erro ao SALVAR Convenção Serviço");
                return;
            }
            
            GenericaMensagem.info("Sucesso", "Convenção Serviço SALVO!");
        }else{
            List<ConvencaoServico> l = new ConvencaoServicoDao().listaConvencaoServico(Integer.valueOf(listaConvencaoCidade.get(idConvencaoCidade).getDescription()));
            if (!l.isEmpty()){
                if (l.get(0).getConvencaoCidade().getId() != convencaoServico.getConvencaoCidade().getId()){
                    dao.rollback();
                    GenericaMensagem.warn("Atenção", "Clausula já existente para esta Convenção Cidade");
                    return;
                }
            }
            
            convencaoServico.setConvencaoCidade((ConvencaoCidade) dao.find(new ConvencaoCidade(), Integer.valueOf(listaConvencaoCidade.get(idConvencaoCidade).getDescription())));
            if (!dao.update(convencaoServico)){
                dao.rollback();
                GenericaMensagem.error("Atenção", "Erro ao ALTERAR Convenção Serviço");
                return;
            }
            
            GenericaMensagem.info("Sucesso", "Convenção Serviço ALTERADO!");
        }
        
        dao.commit();
        convencaoServico = new ConvencaoServico();
        loadListaConvencaoServico();
    }
    
    public void delete(ConvencaoServico cs){
        if (cs == null){
            Dao dao = new Dao();

            dao.openTransaction();

            if (!dao.delete(dao.find(convencaoServicoDelete))){
                dao.rollback();
                GenericaMensagem.error("Atenção", "Erro ao excluir Convenção Serviço");
                return;
            }

            dao.commit();
            convencaoServicoDelete = new ConvencaoServico();
            convencaoServico = new ConvencaoServico();
            loadListaConvencaoServico();
            GenericaMensagem.info("Sucesso", "Convenção Serviço excluído");
        }else{
            convencaoServicoDelete = cs;
        }
    }
    
    public void edit(ConvencaoServico cs){
        convencaoServico = cs;
        
        for(int i = 0; i < listaConvencaoCidade.size(); i++){
            if ( Integer.valueOf(listaConvencaoCidade.get(i).getDescription()) == convencaoServico.getConvencaoCidade().getId() ){
                idConvencaoCidade = i;
            }
        }
    }
    
    public void loadListaConvencaoCidade(){
        listaConvencaoCidade.clear();
        
        List<ConvencaoCidade> result = new ConvencaoServicoDao().listaConvencaoCidade();
        
        for (int i = 0; i < result.size(); i++){
            listaConvencaoCidade.add(new SelectItem(
                    i, 
                    result.get(i).getConvencao().getDescricao() + " - " + result.get(i).getGrupoCidade().getDescricao(), 
                    Integer.toString(result.get(i).getId())
            )
            );
        }
    }

    public ConvencaoServico getConvencaoServico() {
        return convencaoServico;
    }

    public void setConvencaoServico(ConvencaoServico convencaoServico) {
        this.convencaoServico = convencaoServico;
    }

    public Integer getIdConvencaoCidade() {
        return idConvencaoCidade;
    }

    public void setIdConvencaoCidade(Integer idConvencaoCidade) {
        this.idConvencaoCidade = idConvencaoCidade;
    }

    public List<SelectItem> getListaConvencaoCidade() {
        return listaConvencaoCidade;
    }

    public void setListaConvencaoCidade(List<SelectItem> listaConvencaoCidade) {
        this.listaConvencaoCidade = listaConvencaoCidade;
    }

    public List<ConvencaoServico> getListaConvencaoServico() {
        return listaConvencaoServico;
    }

    public void setListaConvencaoServico(List<ConvencaoServico> listaConvencaoServico) {
        this.listaConvencaoServico = listaConvencaoServico;
    }

    public ConvencaoServico getConvencaoServicoDelete() {
        return convencaoServicoDelete;
    }

    public void setConvencaoServicoDelete(ConvencaoServico convencaoServicoDelete) {
        this.convencaoServicoDelete = convencaoServicoDelete;
    }
}
