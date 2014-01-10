package br.com.rtools.seguranca.beans;

import br.com.rtools.financeiro.Caixa;
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
    private int idCaixa = 0;
    private String msgConfirma;
    private List<MacFilial> listaMacs;
    public List<SelectItem> listaFiliais = new ArrayList<SelectItem>();
    public List<SelectItem> listaDepartamentos = new ArrayList<SelectItem>();
    private List<SelectItem> listaCaixa = new ArrayList<SelectItem>();
    
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
        
        if (listaCaixa.get(idCaixa).getDescription().equals("-1")){
            macFilial.setCaixa(null);
        }else{
            for (int i = 0; i < listaMacs.size(); i++){
                if (listaMacs.get(i).getCaixa() != null && listaMacs.get(i).getCaixa().getId() == Integer.valueOf(listaCaixa.get(idCaixa).getDescription())){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Já existe uma filial cadastrada para este Caixa"));
                    return null;
                }
            }
            macFilial.setCaixa((Caixa)sv.pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()), "Caixa"));
        }
        sv.abrirTransacao();
        if (sv.inserirObjeto(macFilial)) {
            sv.comitarTransacao();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo", "Este Computador registrado com sucesso!"));
            macFilial = new MacFilial();
            listaMacs.clear();
        } else {
            sv.desfazerTransacao();
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

    public int getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(int idCaixa) {
        this.idCaixa = idCaixa;
    }

    public List<SelectItem> getListaCaixa() {
        if (listaCaixa.isEmpty()){
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            List<Caixa> result = sv.listaObjeto("Caixa");
            
            listaCaixa.add(new SelectItem(new Integer(0), "NENHUM CAIXA", "-1"));
            for(int i = 0; i < result.size(); i++){
                listaCaixa.add(new SelectItem(new Integer(i+1),
                              ((String.valueOf(result.get(i).getCaixa()).length() == 1) ? ("0"+String.valueOf(result.get(i).getCaixa())) : result.get(i).getCaixa()) +" - "+ result.get(i).getDescricao(),
                              Integer.toString(result.get(i).getId())));
            }
        }
        return listaCaixa;
    }

    public void setListaCaixa(List<SelectItem> listaCaixa) {
        this.listaCaixa = listaCaixa;
    }
}
