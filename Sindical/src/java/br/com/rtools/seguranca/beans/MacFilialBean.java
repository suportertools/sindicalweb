package br.com.rtools.seguranca.beans;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.seguranca.db.MacFilialDB;
import br.com.rtools.seguranca.db.MacFilialDBToplink;
import br.com.rtools.seguranca.db.PermissaoUsuarioDB;
import br.com.rtools.seguranca.db.PermissaoUsuarioDBToplink;
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
public class MacFilialBean implements Serializable {

    private MacFilial macFilial;
    private int idFilial;
    private int idDepartamento;
    private int idCaixa = 0;
    private List<MacFilial> listaMacs;
    public List<SelectItem> listaFiliais;
    public List<SelectItem> listaDepartamentos;
    private List<SelectItem> listaCaixa;
    private Boolean mostrarTodos;

    @PostConstruct
    public void init() {
        macFilial = new MacFilial();
        idFilial = 0;
        idDepartamento = 0;
        idCaixa = 0;
        listaMacs = new ArrayList<>();
        listaFiliais = new ArrayList<>();
        listaDepartamentos = new ArrayList<>();
        listaCaixa = new ArrayList<>();
        mostrarTodos = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("macFilialBean");
    }

    public void clear() {
        GenericaSessao.remove("macFilialBean");
    }

    public void add() {
        MacFilialDB dbm = new MacFilialDBToplink();
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        Filial filial = (Filial) di.find(new Filial(), Integer.parseInt(getListaFiliais().get(idFilial).getDescription()));
        Departamento departamento = (Departamento) di.find(new Departamento(), Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()));
        if (macFilial.getMac().isEmpty()) {
            GenericaMensagem.warn("Validação", "Digite um mac válido!");
            return;
        }

//        Registro registro = (Registro) di.find(new Registro(), 1);
//        if (registro.isSenhaHomologacao() && macFilial.getMesa() <= 0) {
//            GenericaMensagem.warn("Validação", "O campo mesa é obrigatório devido Senha Homologação em Registro ser verdadeiro");
//            return;
//        }
        if (!macFilial.getMac().matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
            GenericaMensagem.warn("Validação", "Digite um mac válido!");
            return;
        }
        macFilial.setDepartamento(departamento);
        macFilial.setFilial(filial);

        if (listaCaixa.get(idCaixa).getDescription().equals("-1")) {
            macFilial.setCaixa(null);
        } else {
            for (int i = 0; i < listaMacs.size(); i++) {
                if (listaMacs.get(i).getCaixa() != null
                        && listaMacs.get(i).getCaixa().getId() == Integer.valueOf(listaCaixa.get(idCaixa).getDescription())
                        && macFilial.getId() == -1) {
                    GenericaMensagem.warn("Validação", "Já existe uma filial cadastrada para este Caixa");
                    return;
                }
            }
            macFilial.setCaixa((Caixa) di.find(new Caixa(), Integer.valueOf(listaCaixa.get(idCaixa).getDescription())));
        }
        di.openTransaction();

        if (macFilial.getId() == -1) {
            if (dbm.pesquisaMac(macFilial.getMac()) != null) {
                GenericaMensagem.warn("Validação", "Este computador ja está registrado!");
                return;
            }

            if (di.save(macFilial)) {
                novoLog.save(
                        "ID: " + macFilial.getId()
                        + " - Filial: (" + macFilial.getFilial().getId() + ") " + macFilial.getFilial().getFilial().getPessoa().getNome()
                        + " - Departamento: (" + macFilial.getDepartamento().getId() + ") " + macFilial.getDepartamento().getDescricao()
                        + " - Mesa: " + macFilial.getMesa()
                        + " - Mac: " + macFilial.getMac()
                );
                di.commit();
                GenericaMensagem.info("Salvo", "Este Computador registrado com sucesso!");
                macFilial = new MacFilial();
                listaMacs.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Erro ao inserir esse registro!");
            }
        } else {
            MacFilial mf = (MacFilial) new Dao().find(macFilial);

            String before_update
                    = "ID: " + macFilial.getId()
                    + " - Filial: (" + mf.getFilial().getId() + ") " + mf.getFilial().getFilial().getPessoa().getNome()
                    + " - Departamento: (" + mf.getDepartamento().getId() + ") " + mf.getDepartamento().getDescricao()
                    + " - Mesa: " + mf.getMesa()
                    + " - Mac: " + mf.getMac()
                    + " - Número Caixa: " + ((mf.getCaixa() == null) ? "" : mf.getCaixa().getCaixa())
                    + " - Caixa: " + ((mf.getCaixa() == null) ? "" : mf.getCaixa().getDescricao());

            if (di.update(macFilial)) {
                novoLog.update(
                        before_update,
                        "ID: " + macFilial.getId()
                        + " - Filial: (" + macFilial.getFilial().getId() + ") " + macFilial.getFilial().getFilial().getPessoa().getNome()
                        + " - Departamento: (" + macFilial.getDepartamento().getId() + ") " + macFilial.getDepartamento().getDescricao()
                        + " - Mesa: " + macFilial.getMesa()
                        + " - Mac: " + macFilial.getMac()
                        + " - Número Caixa: " + ((mf.getCaixa() == null) ? "" : (macFilial.getCaixa() != null) ? macFilial.getCaixa().getCaixa() : "")
                        + " - Caixa: " + ((mf.getCaixa() == null) ? "" : (macFilial.getCaixa() != null) ? macFilial.getCaixa().getDescricao() : "")
                );
                di.commit();
                GenericaMensagem.info("Atualizado", "Computador atualizado com sucesso!");
                macFilial = new MacFilial();
                listaMacs.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Erro ao inserir esse registro!");
            }

        }
    }

    public void edit(MacFilial mf) {
        macFilial = mf;

        for (int i = 0; i < listaDepartamentos.size(); i++) {
            if (Integer.valueOf(listaDepartamentos.get(i).getDescription()) == macFilial.getDepartamento().getId()) {
                idDepartamento = i;
            }
        }

        for (int i = 0; i < listaFiliais.size(); i++) {
            if (Integer.valueOf(listaFiliais.get(i).getDescription()) == macFilial.getFilial().getId()) {
                idFilial = i;
            }
        }

        if (macFilial.getCaixa() == null) {
            idCaixa = 0;
        } else {
            for (int i = 0; i < listaCaixa.size(); i++) {
                if (Integer.valueOf(listaCaixa.get(i).getDescription()) == macFilial.getCaixa().getId()) {
                    idCaixa = i;
                }
            }
        }
    }

    public void delete(MacFilial mf) {
        macFilial = mf;
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        if (di.delete(macFilial)) {
            novoLog.delete(
                    "ID: " + macFilial.getId()
                    + " - Filial: (" + macFilial.getFilial().getId() + ") " + macFilial.getFilial().getFilial().getPessoa().getNome()
                    + " - Departamento: (" + macFilial.getDepartamento().getId() + ") " + macFilial.getDepartamento().getDescricao()
                    + " - Mesa: " + macFilial.getMesa()
                    + " - Mac: " + macFilial.getMac()
            );
            di.commit();
            GenericaMensagem.info("Sucesso", "Este Registro excluído com sucesso!");
            listaMacs.clear();
        } else {
            di.rollback();
            GenericaMensagem.info("Sucesso", "Erro ao excluir computador!");

        }
        macFilial = new MacFilial();
    }

    public List<SelectItem> getListaFiliais() {
        if (listaFiliais.isEmpty()) {
//            FilialDB db = new FilialDBToplink();
//            List<Filial> list = db.pesquisaTodos();
            DaoInterface di = new Dao();
            List<Filial> list = (List<Filial>) di.list(new Filial(), true);
            for (int i = 0; i < list.size(); i++) {
                listaFiliais.add(new SelectItem(i,
                        list.get(i).getFilial().getPessoa().getDocumento() + " / " + list.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaFiliais;
    }

    public List<SelectItem> getListaDepartamentos() {
        if (listaDepartamentos.isEmpty()) {
            PermissaoUsuarioDB pu = new PermissaoUsuarioDBToplink();
            //        DepartamentoDB db = new DepartamentoDBToplink();
            List<Departamento> select = pu.pesquisaTodosDepOrdenado();
            for (int i = 0; i < select.size(); i++) {
                listaDepartamentos.add(new SelectItem(i,
                        select.get(i).getDescricao(),
                        Integer.toString(select.get(i).getId())));
            }
        }
        return listaDepartamentos;
    }

    public void refreshForm() {

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

    public List<MacFilial> getListaMacs() {
        if (listaMacs.isEmpty()) {
            MacFilialDB db = new MacFilialDBToplink();
            if (mostrarTodos) {
                listaMacs = db.listaTodosPorFilial(null);
            } else {
                listaMacs = db.listaTodosPorFilial(Integer.parseInt(listaFiliais.get(idFilial).getDescription()));
            }
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
        if (listaCaixa.isEmpty()) {
            DaoInterface di = new Dao();
            List<Caixa> result = di.list(new Caixa());

            listaCaixa.add(new SelectItem(0, "NENHUM CAIXA", "-1"));
            for (int i = 0; i < result.size(); i++) {
                listaCaixa.add(new SelectItem(i + 1,
                        ((String.valueOf(result.get(i).getCaixa()).length() == 1) ? ("0" + String.valueOf(result.get(i).getCaixa())) : result.get(i).getCaixa()) + " - " + result.get(i).getDescricao(),
                        Integer.toString(result.get(i).getId())));
            }
        }
        return listaCaixa;
    }

    public void setListaCaixa(List<SelectItem> listaCaixa) {
        this.listaCaixa = listaCaixa;
    }

    public String selecionaFilial(MacFilial mf) {
        return selecionaFilial(mf, false);
    }

    public String selecionaFilial(MacFilial mf, boolean sair) {
        GenericaSessao.remove("acessoFilial");
        GenericaSessao.remove("chamadaPaginaBean");
        ((ControleUsuarioBean) GenericaSessao.getObject("controleUsuarioBean")).setMacFilial(mf);
        String s = "Filial: ( " + mf.getFilial().getFilial().getPessoa().getNome() + " / " + mf.getDepartamento().getDescricao() + " )";
        if (mf.getMesa() > 0) {
            s += " - Guiche: " + mf.getMesa();
        }
        if (mf.getDescricao() != null && !mf.getDescricao().isEmpty()) {
            s += " - " + mf.getDescricao();
        }
        ((ControleUsuarioBean) GenericaSessao.getObject("controleUsuarioBean")).setFilial(s);
        GenericaSessao.put("acessoFilial", mf);
        GenericaSessao.put("linkClicado", true);
        return "menuPrincipal";

    }

    public Boolean getMostrarTodos() {
        return mostrarTodos;
    }

    public void setMostrarTodos(Boolean mostrarTodos) {
        this.mostrarTodos = mostrarTodos;
    }
}
