package br.com.rtools.seguranca.beans;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDBToplink;
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
import javax.faces.model.SelectItem;

public class MacFilialJSFBean {
    private MacFilial macFilial;
    private int idFilial;
    private int idDepartamento;
    private int idIndex;
    private String msgConfirma;
    private List<MacFilial> listaMacs;
    
    public MacFilialJSFBean(){
        macFilial = new MacFilial();
        idFilial = 0;
        idDepartamento = 0;
        msgConfirma = "";
        listaMacs = new ArrayList();
    }

    public String adicionar(){
        Filial filial = new Filial();
        FilialDB dbf = new FilialDBToplink();
        Departamento departamento = new Departamento();
        MacFilialDB dbm = new MacFilialDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        filial = dbf.pesquisaCodigo(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()));
        departamento = (Departamento)sv.pesquisaCodigo(Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()), "Departamento");

        if (macFilial.getMac().isEmpty()){
            msgConfirma = "Digite um mac válido!";
            return null;
        }

        if (dbm.pesquisaMac(macFilial.getMac()) != null){
            msgConfirma = "Este computador ja está registrado!";
            return null;
        }
        
        Registro registro = (Registro)sv.pesquisaCodigo(1, "Registro");
        
        if (registro.isSenhaHomologacao() && macFilial.getMesa() <= 0){
            msgConfirma = "O campo mesa é obrigatório devido Senha Homologação em Registro ser verdadeiro";
            return null;
        }

        macFilial.setDepartamento(departamento);
        macFilial.setFilial(filial);
        if (dbm.insert(macFilial)){
            msgConfirma = "Computador registrado com sucesso!";
            macFilial = new MacFilial();
            listaMacs.clear();
        }else{
            msgConfirma = "Erro ao registrar computador!";
        }
        return null;
    }

    public String excluir(){
        MacFilialDB db = new MacFilialDBToplink();
        macFilial = (MacFilial)listaMacs.get(idIndex);
        if ( db.delete(db.pesquisaCodigo(macFilial.getId())) ){
            msgConfirma = "Computador excluído com sucesso!";
            listaMacs.clear();
        } else
            msgConfirma = "Erro ao excluir computador!";
        macFilial = new MacFilial();
        return null;
    }

    public List<SelectItem> getListaFiliais(){
        List<SelectItem> result = new Vector<SelectItem>();
        List<Filial> select = new ArrayList();
        FilialDB db = new FilialDBToplink();
        select = db.pesquisaTodos();
        for(int i = 0; i < select.size(); i++){
            result.add(new SelectItem(new Integer(i),
                                      select.get(i).getFilial().getPessoa().getDocumento()+" / "+ select.get(i).getFilial().getPessoa().getNome(),
                                      Integer.toString(select.get(i).getId())));
        }
        return result;
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

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}
