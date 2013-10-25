package br.com.rtools.seguranca.beans;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.db.MacFilialDB;
import br.com.rtools.seguranca.db.MacFilialDBToplink;
import br.com.rtools.seguranca.db.PermissaoUsuarioDB;
import br.com.rtools.seguranca.db.PermissaoUsuarioDBToplink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class MacFilialBean {
    private MacFilial macFilial;
    private int idFilial;
    private int idDepartamento;
    private String msgConfirma;
    private List<MacFilial> listaMacs;
    public List<SelectItem> listaFiliais = new ArrayList<SelectItem>();
    public List<SelectItem> listaDepartamentos = new ArrayList<SelectItem>();
    
    public MacFilialBean(){
        macFilial = new MacFilial();
        idFilial = 0;
        idDepartamento = 0;
        msgConfirma = "";
        listaMacs = new ArrayList();
    }
    
    public String adicionar() {
        MacFilialDB dbm = new MacFilialDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Filial filial = (Filial) sv.pesquisaObjeto(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), "Filial");
        Departamento departamento = (Departamento) sv.pesquisaObjeto(Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()), "Departamento");
        if (macFilial.getMac().isEmpty()) {
            msgConfirma = "Digite um mac válido!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        }
        if (dbm.pesquisaMac(macFilial.getMac()) != null) {
            msgConfirma = "Este computador ja está registrado!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        }
        Registro registro = (Registro) sv.pesquisaObjeto(1, "Registro");
        if (registro.isSenhaHomologacao() && macFilial.getMesa() <= 0) {
            msgConfirma = "O campo mesa é obrigatório devido Senha Homologação em Registro ser verdadeiro";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        }
        macFilial.setDepartamento(departamento);
        macFilial.setFilial(filial);
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.inserirObjeto(macFilial)) {
            salvarAcumuladoDB.comitarTransacao();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo", "Este Computador registrado com sucesso!"));
            macFilial = new MacFilial();
            listaMacs.clear();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            msgConfirma = "Erro ao inserir esse registro!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
        }
        return null;
    }    
    

    public String excluir(MacFilial mf) {
        macFilial = mf;
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        macFilial = (MacFilial) sv.pesquisaObjeto(macFilial.getId(), "MacFilial");
        sv.abrirTransacao();
        if (sv.deletarObjeto(macFilial)) {
            sv.comitarTransacao();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Este Registro excluído com sucesso!"));
            listaMacs.clear();
        } else {
            sv.desfazerTransacao();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Erro ao excluir computador!"));
            
        }
        macFilial = new MacFilial();
        return null;
    }
    
    public List<SelectItem> getListaFiliais(){
        if (listaFiliais.isEmpty()) {
//            FilialDB db = new FilialDBToplink();
//            List<Filial> list = db.pesquisaTodos();
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<Filial> list = (List<Filial>) salvarAcumuladoDB.listaObjeto("Filial", true);
            for (int i = 0; i < list.size(); i++) {
                listaFiliais.add(new SelectItem(new Integer(i),
                        list.get(i).getFilial().getPessoa().getDocumento() + " / " + list.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaFiliais;
    }

    public List<SelectItem> getListaDepartamentos(){
        List<SelectItem> result = new Vector<SelectItem>();
        List<Departamento> select = new ArrayList();
        PermissaoUsuarioDB pu = new PermissaoUsuarioDBToplink();
//        DepartamentoDB db = new DepartamentoDBToplink();
        select = pu.pesquisaTodosDepOrdenado();
        for(int i = 0; i < select.size(); i++){
            result.add(new SelectItem(new Integer(i),
                                      select.get(i).getDescricao(),
                                      Integer.toString(select.get(i).getId())));
        }
        return result;
    }

    public void refreshForm(){
        
    }

    public MacFilial getMacFilial() {
        return macFilial;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public List<MacFilial> getListaMacs() {
        if (listaMacs.isEmpty()){
            MacFilialDB db = new MacFilialDBToplink();
            listaMacs = db.pesquisaTodos();
        }
        return listaMacs;
    }

    public void setListaMacs(List<MacFilial> listaMacs) {
        this.listaMacs = listaMacs;
    }
}
