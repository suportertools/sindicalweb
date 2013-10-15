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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class MacFilialBean implements Serializable {

    private MacFilial macFilial;
    private int idFilial;
    private int idDepartamento;
    private String mensagem;
    private List<MacFilial> listaMacs;
    public List<SelectItem> listaFiliais = new ArrayList<SelectItem>();
    public List<SelectItem> listaDepartamentos = new ArrayList<SelectItem>();

    public MacFilialBean() {
        macFilial = new MacFilial();
        idFilial = 0;
        idDepartamento = 0;
        mensagem = "";
        listaMacs = new ArrayList();
    }

    public String adicionar() {
        MacFilialDB dbm = new MacFilialDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Filial filial = (Filial) sv.pesquisaObjeto(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), "Filial");
        Departamento departamento = (Departamento) sv.pesquisaObjeto(Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()), "Departamento");
        if (macFilial.getMac().isEmpty()) {
            mensagem = "Digite um mac válido!";
            return null;
        }
        if (dbm.pesquisaMac(macFilial.getMac()) != null) {
            mensagem = "Este computador ja está registrado!";
            return null;
        }
        Registro registro = (Registro) sv.pesquisaObjeto(1, "Registro");
        if (registro.isSenhaHomologacao() && macFilial.getMesa() <= 0) {
            mensagem = "O campo mesa é obrigatório devido Senha Homologação em Registro ser verdadeiro";
            return null;
        }
        macFilial.setDepartamento(departamento);
        macFilial.setFilial(filial);
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.inserirObjeto(macFilial)) {
            salvarAcumuladoDB.comitarTransacao();
            mensagem = "Registro inserido com sucesso!";
            macFilial = new MacFilial();
            listaMacs.clear();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            mensagem = "Erro ao inserir esse registro!";
        }
        return null;
    }

    public String excluir(MacFilial mf) {
        macFilial = mf;
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        macFilial = (MacFilial) salvarAcumuladoDB.pesquisaObjeto(macFilial.getId(), "MacFilial");
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.deletarObjeto(macFilial)) {
            salvarAcumuladoDB.comitarTransacao();
            mensagem = "Registro excluído com sucesso!";
            listaMacs.clear();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            mensagem = "Erro ao excluir esse registro!";
        }
        macFilial = new MacFilial();
        return null;
    }

    public List<SelectItem> getListaFiliais() {
        if (listaFiliais.isEmpty()) {
            FilialDB db = new FilialDBToplink();
            List<Filial> list = db.pesquisaTodos();
            for (int i = 0; i < list.size(); i++) {
                listaFiliais.add(new SelectItem(new Integer(i),
                        list.get(i).getFilial().getPessoa().getDocumento() + " / " + list.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaFiliais;
    }

    public List<SelectItem> getListaDepartamentos() {
        if (listaDepartamentos.isEmpty()) {
            PermissaoUsuarioDB pu = new PermissaoUsuarioDBToplink();
            List<Departamento> list = pu.pesquisaTodosDepOrdenado();
            for (int i = 0; i < list.size(); i++) {
                listaDepartamentos.add(new SelectItem(new Integer(i),
                        list.get(i).getDescricao(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaDepartamentos;
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<MacFilial> getListaMacs() {
        if (listaMacs.isEmpty()) {
            MacFilialDB db = new MacFilialDBToplink();
            listaMacs = db.pesquisaTodos();
        }
        return listaMacs;
    }

    public void setListaMacs(List<MacFilial> listaMacs) {
        this.listaMacs = listaMacs;
    }
}
