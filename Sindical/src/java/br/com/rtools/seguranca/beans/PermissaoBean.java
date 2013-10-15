package br.com.rtools.seguranca.beans;

import br.com.rtools.seguranca.*;
import br.com.rtools.seguranca.db.*;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
// import java.util.Vector;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class PermissaoBean implements Serializable {

    private Permissao permissao = new Permissao();
    private Modulo modulo;
    private Rotina rotina;
    private Evento evento;
    private List<Permissao> listaPermissoes = new ArrayList();
    private PermissaoDepartamento permissaoDepartamento = new PermissaoDepartamento();
    private String msgConfirma;
    private String indicaTab = "permissao";
    private String descricaoPesquisa = "";
    private String tabDisabled = "true";
    private List listaPermissoesDisponiveis = new ArrayList();
    private List listaPermissoesAdicionadas = new ArrayList();
    private List<SelectItem> listaRotinas = new ArrayList();
    private List<SelectItem> listaModulos = new ArrayList();
    private List<SelectItem> listaEventos = new ArrayList();
    private List<SelectItem> listaDepartamentos = new ArrayList();
    private List<SelectItem> listaNiveis = new ArrayList();
    private int idModulo;
    private int idRotina;
    private int idEvento;
    private int idDepartamento;
    private int idNivel;
    private int idIndex = -1;

    public String novo() {
        permissao = new Permissao();
        setIdModulo(0);
        setIdRotina(0);
        setIdEvento(0);
        setDescricaoPesquisa("");
        setTabDisabled("true");
        return "permissao";
    }

    // MÓDULO / ROTINA
    public String adicionarPermissao() {
        if (listaRotinas.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", "Não há rotinas disponíveis para serem adicionadas a esse módulo"));
            return null;
        }
        PermissaoDB db = new PermissaoDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        modulo = (Modulo) salvarAcumuladoDB.pesquisaCodigo(Integer.valueOf(listaModulos.get(idModulo).getDescription()), "Modulo");
        rotina = (Rotina) salvarAcumuladoDB.pesquisaCodigo(Integer.valueOf(listaRotinas.get(idRotina).getDescription()), "Rotina");
        boolean sucesso = false;
        if (db.pesquisaPermissaoModRot(modulo.getId(), rotina.getId()).isEmpty()) {
            salvarAcumuladoDB.abrirTransacao();
            for (int i = 0; i < getListaEventos().size(); i++) {
                evento = (Evento) salvarAcumuladoDB.pesquisaCodigo(Integer.valueOf(getListaEventos().get(i).getDescription()), "Evento");
                permissao.setModulo(modulo);
                permissao.setRotina(rotina);
                permissao.setEvento(evento);
                if (!salvarAcumuladoDB.inserirObjeto(permissao)) {
                    sucesso = false;
                    break;
                }
                permissao = new Permissao();
                sucesso = true;
            }
            if (sucesso) {
                salvarAcumuladoDB.comitarTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Registro adicionado com sucesso"));
                listaRotinas.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro adicionar permissão(s)!"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", "Permissão já existente!"));
        }
        permissao = new Permissao();
        return null;
    }

    public String removerPermissao(Permissao p) {
        PermissaoDB db = new PermissaoDBToplink();
        List<Permissao> listaPermissao = (List<Permissao>) db.pesquisaPermissaoModRot(p.getModulo().getId(), p.getRotina().getId());
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        boolean sucesso = false;
        for (int i = 0; i < listaPermissao.size(); i++) {
            permissao = (Permissao) salvarAcumuladoDB.pesquisaCodigo(listaPermissao.get(i).getId(), "Permissao");
            if (!salvarAcumuladoDB.deletarObjeto(permissao)) {
                sucesso = false;
                break;
            }
            sucesso = true;
            permissao = new Permissao();
        }
        if (sucesso) {
            salvarAcumuladoDB.comitarTransacao();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão(s) removida(s) com sucesso"));
            listaRotinas.clear();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao remover permissão(s)!"));
        }
        return null;
    }

    // PERMISSÃO DEPARTAMENTO   
    public String adicionarPermissaoDpto() {
        if (!listaPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            boolean temRegistros = false;
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            for (int i = 0; i < listaPermissoesDisponiveis.size(); i++) {
                if ((Boolean) ((DataObject) listaPermissoesDisponiveis.get(i)).getArgumento0() == true) {
                    Permissao perm = (Permissao) ((DataObject) listaPermissoesDisponiveis.get(i)).getArgumento1();
                    Departamento depto = (Departamento) sv.pesquisaCodigo(Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()), "Departamento");
                    Nivel niv = (Nivel) sv.pesquisaCodigo(Integer.parseInt(getListaNiveis().get(idNivel).getDescription()), "Nivel");
                    permissaoDepartamento.setPermissao(perm);
                    permissaoDepartamento.setDepartamento(depto);
                    permissaoDepartamento.setNivel(niv);

                    if (!sv.inserirObjeto(permissaoDepartamento)) {
                        temRegistros = false;
                        erro = true;
                        break;
                    }
                    temRegistros = true;
                    permissaoDepartamento = new PermissaoDepartamento();
                }
            }
            if (erro) {
                sv.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao adicionar permissão(s)!"));
            } else {
                sv.comitarTransacao();
                if (temRegistros) {
                    listaPermissoesAdicionadas.clear();
                    listaPermissoesDisponiveis.clear();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão(s) adicionada(s) com sucesso"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sistema", "Não foi selecionada nenhuma permissão!"));
                }
            }
        }
        return null;
    }

    public String adicionarPermissaoDptoDBClick(Permissao p) {
        if (!listaPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            Permissao perm = p;
            Departamento depto = (Departamento) sv.pesquisaCodigo(Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()), "Departamento");
            Nivel niv = (Nivel) sv.pesquisaCodigo(Integer.parseInt(getListaNiveis().get(idNivel).getDescription()), "Nivel");
            permissaoDepartamento.setPermissao(perm);
            permissaoDepartamento.setDepartamento(depto);
            permissaoDepartamento.setNivel(niv);
            if (!sv.inserirObjeto(permissaoDepartamento)) {
                erro = true;
            }
            permissaoDepartamento = new PermissaoDepartamento();
            if (erro) {
                sv.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao adicionar permissão(s)!"));
            } else {
                sv.comitarTransacao();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão(s) adicionada(s) com sucesso"));
            }
        }
        return null;
    }

    public String adicionarTodasPermissaoDpto() {
        if (!listaPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            for (int i = 0; i < listaPermissoesDisponiveis.size(); i++) {
                Permissao perm = (Permissao) ((DataObject) listaPermissoesDisponiveis.get(i)).getArgumento1();
                Departamento depto = (Departamento) sv.pesquisaCodigo(Integer.parseInt(listaDepartamentos.get(idDepartamento).getDescription()), "Departamento");
                Nivel niv = (Nivel) sv.pesquisaCodigo(Integer.parseInt(listaNiveis.get(idNivel).getDescription()), "Nivel");
                permissaoDepartamento.setPermissao(perm);
                permissaoDepartamento.setDepartamento(depto);
                permissaoDepartamento.setNivel(niv);
                if (!sv.inserirObjeto(permissaoDepartamento)) {
                    erro = true;
                    break;
                }
                permissaoDepartamento = new PermissaoDepartamento();
            }
            if (erro) {
                sv.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao adicionar permissão(s)!"));
            } else {
                sv.comitarTransacao();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão(s) adicionada(s) com sucesso"));
            }
        }
        return null;
    }

    public String excluirPermissaoDepto() {
        if (!listaPermissoesAdicionadas.isEmpty()) {
            boolean erro = false;
            boolean temRegistros = false;
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            for (int i = 0; i < listaPermissoesAdicionadas.size(); i++) {
                if ((Boolean) ((DataObject) listaPermissoesAdicionadas.get(i)).getArgumento0() == true) {
                    permissaoDepartamento = (PermissaoDepartamento) ((DataObject) listaPermissoesAdicionadas.get(i)).getArgumento2();
                    if (!sv.deletarObjeto((PermissaoDepartamento) sv.pesquisaCodigo(permissaoDepartamento.getId(), "PermissaoDepartamento"))) {
                        erro = true;
                        temRegistros = false;
                        break;
                    }
                    temRegistros = true;
                }
                permissaoDepartamento = new PermissaoDepartamento();
            }
            if (erro) {
                sv.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao remover permissão(s)!"));
            } else {
                sv.comitarTransacao();
                if (temRegistros) {
                    listaPermissoesAdicionadas.clear();
                    listaPermissoesDisponiveis.clear();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão(s) removida(s) com sucesso"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sistema", "Não foi selecionada nenhuma permissão!"));
                }
            }
        }
        return null;
    }

    public String excluirPermissaoDeptoDBClick(PermissaoDepartamento pd) {
        if (!listaPermissoesAdicionadas.isEmpty()) {
            boolean erro = false;
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            permissaoDepartamento = pd;
            if (!sv.deletarObjeto((PermissaoDepartamento) sv.pesquisaCodigo(permissaoDepartamento.getId(), "PermissaoDepartamento"))) {
                erro = true;
            }
            permissaoDepartamento = new PermissaoDepartamento();
            if (erro) {
                sv.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao remover permissão(s)!"));
            } else {
                sv.comitarTransacao();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão(s) removida(s) com sucesso"));
            }
        }
        return null;
    }

    public String excluirTodasPermissaoDepto() {
        if (!listaPermissoesAdicionadas.isEmpty()) {
            boolean erro = false;
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            for (int i = 0; i < listaPermissoesAdicionadas.size(); i++) {
                permissaoDepartamento = (PermissaoDepartamento) ((DataObject) listaPermissoesAdicionadas.get(i)).getArgumento2();
                if (!sv.deletarObjeto((PermissaoDepartamento) sv.pesquisaCodigo(permissaoDepartamento.getId(), "PermissaoDepartamento"))) {
                    erro = true;
                    break;
                }
            }
            if (erro) {
                sv.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao remover permissão(s)!"));
            } else {
                sv.comitarTransacao();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Permissão(s) removidas com sucesso"));
            }
        }
        permissaoDepartamento = new PermissaoDepartamento();
        return null;
    }

    public void pesquisaPermissoesDepartamento() {
        listaPermissoesDisponiveis.clear();
        listaPermissoesAdicionadas.clear();
    }

    public void limparPesquisaPermissoesDepartamento() {
        descricaoPesquisa = "";
        listaPermissoesDisponiveis.clear();
        listaPermissoesAdicionadas.clear();
    }

    public List getPermissaoDisponivel() {
        if (listaPermissoesDisponiveis.isEmpty()) {
            listaPermissoesDisponiveis.clear();
            PermissaoDepartamentoDB permissaoDepartamentoDB = new PermissaoDepartamentoDBToplink();
            int idDepto = Integer.parseInt(listaDepartamentos.get(idDepartamento).getDescription());
            int idNiv = Integer.parseInt(listaNiveis.get(idNivel).getDescription());
            List<Permissao> list = permissaoDepartamentoDB.listaPermissaoDepartamentoDisponivel(idDepto, idNiv, descricaoPesquisa);
            DataObject dtObject;
            for (int i = 0; i < list.size(); i++) {
                dtObject = new DataObject(
                        false,
                        (Permissao) list.get(i));
                listaPermissoesDisponiveis.add(dtObject);
            }
        }
        return listaPermissoesDisponiveis;
    }

    public List getPermissaoAdicionada() {
        if (listaPermissoesAdicionadas.isEmpty()) {
            PermissaoDepartamentoDB permissaoDepartamentoDB = new PermissaoDepartamentoDBToplink();
            int idDepto = Integer.parseInt(listaDepartamentos.get(idDepartamento).getDescription());
            int idNiv = Integer.parseInt(listaNiveis.get(idNivel).getDescription());
            List<PermissaoDepartamento> list = permissaoDepartamentoDB.listaPermissaoDepartamentoAdicionada(idDepto, idNiv, descricaoPesquisa);
            DataObject dtObject;
            for (int i = 0; i < list.size(); i++) {
                dtObject = new DataObject(false,
                        ((PermissaoDepartamento) list.get(i)).getPermissao(),
                        ((PermissaoDepartamento) list.get(i)),
                        null,
                        null,
                        null);
                listaPermissoesAdicionadas.add(dtObject);
            }
        }
        return listaPermissoesAdicionadas;
    }

    public List getListaPermissaoDpto() {
        PermissaoDepartamentoDB db = new PermissaoDepartamentoDBToplink();
        List result = db.pesquisaTodos();
        return result;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getIndicaTab() {
        return indicaTab;
    }

    public void setIndicaTab(String indicaTab) {
        this.indicaTab = indicaTab;
    }

    public String getTabDisabled() {
        return tabDisabled;
    }

    public void setTabDisabled(String tabDisabled) {
        this.tabDisabled = tabDisabled;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public PermissaoDepartamento getPermissaoDepartamento() {
        return permissaoDepartamento;
    }

    public void setPermissaoDepartamento(PermissaoDepartamento permissaoDepartamento) {
        this.permissaoDepartamento = permissaoDepartamento;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public List<SelectItem> getListaModulos() {
        if (listaModulos.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List modulos = salvarAcumuladoDB.listaObjeto("Modulo", true);
            for (int i = 0; i < modulos.size(); i++) {
                listaModulos.add(new SelectItem(new Integer(i),
                        ((Modulo) modulos.get(i)).getDescricao(),
                        Integer.toString(((Modulo) modulos.get(i)).getId())));
            }
        }
        return listaModulos;
    }

    public void setListaModulos(List<SelectItem> listaModulos) {
        this.listaModulos = listaModulos;
    }

    public List<SelectItem> getListaRotinas() {
        listaRotinas.clear();
        if (listaRotinas.isEmpty()) {
            RotinaDB rotinaDB = new RotinaDBToplink();
            List list = rotinaDB.pesquisaRotinasDisponiveisModulo(Integer.parseInt(listaModulos.get(idModulo).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listaRotinas.add(new SelectItem(new Integer(i),
                        ((Rotina) list.get(i)).getRotina(),
                        Integer.toString(((Rotina) list.get(i)).getId())));
            }
        }
        return listaRotinas;
    }

    public void setListaRotinas(List<SelectItem> listaRotinas) {
        this.listaRotinas = listaRotinas;
    }

    public List<SelectItem> getListaEventos() {
        if (listaEventos.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List eventos = salvarAcumuladoDB.listaObjeto("Evento", true);
            for (int i = 0; i < eventos.size(); i++) {
                listaEventos.add(new SelectItem(new Integer(i),
                        ((Evento) eventos.get(i)).getDescricao(),
                        Integer.toString(((Evento) eventos.get(i)).getId())));
            }
        }
        return listaEventos;
    }

    public void setListaEventos(List<SelectItem> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public List<SelectItem> getListaNiveis() {
        if (listaNiveis.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List niveis = salvarAcumuladoDB.listaObjeto("Nivel", true);
            for (int i = 0; i < niveis.size(); i++) {
                listaNiveis.add(new SelectItem(new Integer(i),
                        ((Nivel) niveis.get(i)).getDescricao(),
                        Integer.toString(((Nivel) niveis.get(i)).getId())));
            }

        }
        return listaNiveis;
    }

    public void setListaNiveis(List<SelectItem> listaNiveis) {
        this.listaNiveis = listaNiveis;
    }

    public List<SelectItem> getListaDepartamentos() {
        if (listaDepartamentos.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List departamentos = salvarAcumuladoDB.listaObjeto("Departamento", true);
            for (int i = 0; i < departamentos.size(); i++) {
                listaDepartamentos.add(new SelectItem(new Integer(i),
                        ((Departamento) departamentos.get(i)).getDescricao(),
                        Integer.toString(((Departamento) departamentos.get(i)).getId())));
            }
        }
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<SelectItem> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public List<Permissao> getListaPermissoes() {
        listaPermissoes.clear();
        PermissaoDB db = new PermissaoDBToplink();
        listaPermissoes = db.pesquisaTodosAgrupadosPorModulo(Integer.parseInt(listaModulos.get(idModulo).getDescription()));
        return listaPermissoes;
    }

    public void setListaPermissoes(List<Permissao> listaPermissoes) {
        this.listaPermissoes = listaPermissoes;
    }
}
