package br.com.rtools.atendimento.beans;

import br.com.rtools.atendimento.AteStatus;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class StatusAtendimentoBean implements Serializable {
    private AteStatus status = new AteStatus();
    private List<AteStatus> listAteStatus = new ArrayList<AteStatus>();
    
    public void save(){
        Dao dao = new Dao();
        
        dao.openTransaction();
        if (status.getId() == -1){
            if (!dao.save(status)){
                GenericaMensagem.error("Erro", "Não foi possível salvar Status");
                dao.rollback();
                return;
            }
        }else{
            if (!dao.update(status)){
                GenericaMensagem.error("Erro", "Não foi possível alterar Status");
                dao.rollback();
                return;
            }
        }
        
        dao.commit();
        listAteStatus.clear();
        status = new AteStatus();
        GenericaMensagem.info("Sucesso", "Status Salvo!");
    }
    
    public void edit(AteStatus astatus){
        status = astatus;
    }
    
    public void delete(AteStatus astatus){
        Dao dao = new Dao();
        
        dao.openTransaction();
        
        if (!dao.delete(dao.find(astatus))){
            GenericaMensagem.error("Erro", "Não foi possível excluir Status");
            dao.rollback();
            return;
        }
        
        dao.commit();
        listAteStatus.clear();
        GenericaMensagem.info("Sucesso", "Status Excluído!");
    }
    
    public AteStatus getStatus() {
        return status;
    }

    public void setStatus(AteStatus status) {
        this.status = status;
    }

    public List<AteStatus> getListAteStatus() {
        if (listAteStatus.isEmpty()){
            Dao dao = new Dao();
            listAteStatus = dao.list("AteStatus");
        }
        return listAteStatus;
    }

    public void setListAteStatus(List<AteStatus> listAteStatus) {
        this.listAteStatus = listAteStatus;
    }
    
    
}
