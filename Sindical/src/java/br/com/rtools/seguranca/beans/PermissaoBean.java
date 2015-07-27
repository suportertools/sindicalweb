package br.com.rtools.seguranca.beans;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.seguranca.*;
import br.com.rtools.seguranca.db.*;
import br.com.rtools.utilitarios.Dao;
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
public class PermissaoBean implements Serializable {

    private Permissao permissao;
    private Modulo modulo;
    private Rotina rotina;
    private Evento evento;
    private List<Permissao> listaPermissoes;
    private PermissaoDepartamento permissaoDepartamento;
    //private List<ListaPermissaoDepartamento> listPermissaoDepartamento;
    private String msgConfirma;
    private String indicaTab;
    private String descricaoPesquisa;
    private String tabDisabled;
//    private List listaPermissoesDisponiveis;
//    private List listaPermissoesAdicionadas;
    private List<ListaPermissaoDepartamento> listaPermissoesDisponiveis;
    private List<ListaPermissaoDepartamento> listaPermissoesAdicionadas;
    private List<SelectItem> listaRotinas;
    private List<SelectItem> listaModulos;
    private List<SelectItem> listaEventos;
    private List<SelectItem> listaDepartamentos;
    private List<SelectItem> listaNiveis;
    private int idModulo;
    private int idRotina;
    private int idEvento;
    private int idDepartamento;
    private int idNivel;
    private int idIndex;

    @PostConstruct
    public void init() {
        permissao = new Permissao();
        modulo = new Modulo();
        rotina = new Rotina();
        evento = new Evento();
        listaPermissoes = new ArrayList();
        permissaoDepartamento = new PermissaoDepartamento();
        msgConfirma = "";
        indicaTab = "permissao";
        descricaoPesquisa = "";
        tabDisabled = "true";
        listaPermissoesDisponiveis = new ArrayList<ListaPermissaoDepartamento>();
        listaPermissoesAdicionadas = new ArrayList<ListaPermissaoDepartamento>();
        listaRotinas = new ArrayList();
        listaModulos = new ArrayList();
        listaEventos = new ArrayList();
        listaDepartamentos = new ArrayList();
        listaNiveis = new ArrayList();
        // listPermissaoDepartamento = new ArrayList<ListaPermissaoDepartamento>();
        idModulo = 0;
        idRotina = 0;
        idEvento = 0;
        idDepartamento = 0;
        idNivel = 0;
        idIndex = -1;
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void clear() {
        GenericaSessao.remove("permissaoBean");
    }

    // MÓDULO / ROTINA
    public void addPermissao() {
        if (listaRotinas.isEmpty()) {
            GenericaMensagem.warn("Sistema", "Não há rotinas disponíveis para serem adicionadas a esse módulo");
            return;
        }
        PermissaoDao permissaoDao = new PermissaoDao();
        Dao dao = new Dao();
        modulo = (Modulo) dao.find(new Modulo(), Integer.valueOf(listaModulos.get(idModulo).getDescription()));
        rotina = (Rotina) dao.find(new Rotina(), Integer.valueOf(listaRotinas.get(idRotina).getDescription()));
        boolean sucesso = false;
        if (permissaoDao.pesquisaPermissaoModRot(modulo.getId(), rotina.getId()).isEmpty()) {
            dao.openTransaction();
            for (int i = 0; i < getListaEventos().size(); i++) {
                evento = (Evento) dao.find(new Evento(), Integer.valueOf(getListaEventos().get(i).getDescription()));
                permissao.setModulo(modulo);
                permissao.setRotina(rotina);
                permissao.setEvento(evento);
                if (!dao.save(permissao)) {
                    sucesso = false;
                    break;
                }
                permissao = new Permissao();
                sucesso = true;
            }
            if (sucesso) {
                NovoLog novoLog = new NovoLog();
                novoLog.save("Permissão [" + modulo.getDescricao() + " - " + rotina.getRotina() + "]");
                dao.commit();
                GenericaMensagem.info("Sucesso", "Registro adicionado com sucesso");
                listaRotinas.clear();
            } else {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Erro adicionar permissão(s)!");
            }
        } else {
            GenericaMensagem.warn("Sistema", "Permissão já existente!");
        }
        permissao = new Permissao();
    }

    public void removePermissao(Permissao p) {
        PermissaoDao permissaoDao = new PermissaoDao();
        List<Permissao> listaPermissao = (List<Permissao>) permissaoDao.pesquisaPermissaoModRot(p.getModulo().getId(), p.getRotina().getId());
        Dao dao = new Dao();
        dao.openTransaction();
        boolean sucesso = false;
        for (int i = 0; i < listaPermissao.size(); i++) {
            permissao = (Permissao) dao.find(new Permissao(), listaPermissao.get(i).getId());
            if (!dao.delete(permissao)) {
                sucesso = false;
                break;
            }
            sucesso = true;
            permissao = new Permissao();
        }
        if (sucesso) {
            NovoLog novoLog = new NovoLog();
            novoLog.save("Permissão [" + p.getModulo().getDescricao() + " - " + p.getRotina().getRotina() + "]");
            dao.commit();
            GenericaMensagem.info("Sucesso", "Permissão(s) removida(s) com sucesso");
            listaRotinas.clear();
        } else {
            dao.rollback();
            GenericaMensagem.warn("Erro", "Erro ao remover permissão(s)!");
        }
    }

    // PERMISSÃO DEPARTAMENTO   
    public String adicionarPermissaoDpto() {
        if (!listaPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            boolean temRegistros = false;
            Dao dao = new Dao();
            dao.openTransaction();
            for (int i = 0; i < listaPermissoesDisponiveis.size(); i++) {
                if (listaPermissoesDisponiveis.get(i).isSelected()) {
                    Permissao perm = listaPermissoesDisponiveis.get(i).getPermissao();
                    Departamento depto = (Departamento) dao.find(new Departamento(), Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()));
                    Nivel niv = (Nivel) dao.find(new Nivel(), Integer.parseInt(getListaNiveis().get(idNivel).getDescription()));
                    permissaoDepartamento.setPermissao(perm);
                    permissaoDepartamento.setDepartamento(depto);
                    permissaoDepartamento.setNivel(niv);

                    if (!dao.save(permissaoDepartamento)) {
                        temRegistros = false;
                        erro = true;
                        break;
                    }
                    temRegistros = true;
                    permissaoDepartamento = new PermissaoDepartamento();
                }
            }
            if (erro) {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Erro ao adicionar permissão(s)!");
            } else {
                dao.commit();
                if (temRegistros) {
                    listaPermissoesAdicionadas.clear();
                    listaPermissoesDisponiveis.clear();
                    GenericaMensagem.info("Sucesso", "Permissão(s) adicionada(s) com sucesso");
                } else {
                    GenericaMensagem.info("Sistema", "Não foi selecionada nenhuma permissão!");
                }
            }
        }
        return null;
    }

    public String adicionarPermissaoDptoDBClick(Permissao p) {
        if (!listaPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            Dao dao = new Dao();
            dao.openTransaction();
            Permissao perm = p;
            Departamento depto = (Departamento) dao.find(new Departamento(), Integer.parseInt(getListaDepartamentos().get(idDepartamento).getDescription()));
            Nivel niv = (Nivel) dao.find(new Nivel(), Integer.parseInt(getListaNiveis().get(idNivel).getDescription()));
            permissaoDepartamento.setPermissao(perm);
            permissaoDepartamento.setDepartamento(depto);
            permissaoDepartamento.setNivel(niv);
            if (!dao.save(permissaoDepartamento)) {
                erro = true;
            }
            permissaoDepartamento = new PermissaoDepartamento();
            if (erro) {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Erro ao adicionar permissão(s)!");
            } else {
                dao.commit();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                GenericaMensagem.info("Sucesso", "Permissão(s) adicionada(s) com sucesso");
            }
        }
        return null;
    }

    public String adicionarTodasPermissaoDpto() {
        if (!listaPermissoesDisponiveis.isEmpty()) {
            boolean erro = false;
            Dao dao = new Dao();
            dao.openTransaction();
            for (int i = 0; i < listaPermissoesDisponiveis.size(); i++) {
                Permissao perm = listaPermissoesDisponiveis.get(i).getPermissao();
                Departamento depto = (Departamento) dao.find(new Departamento(), Integer.parseInt(listaDepartamentos.get(idDepartamento).getDescription()));
                Nivel niv = (Nivel) dao.find(new Nivel(), Integer.parseInt(listaNiveis.get(idNivel).getDescription()));
                permissaoDepartamento.setPermissao(perm);
                permissaoDepartamento.setDepartamento(depto);
                permissaoDepartamento.setNivel(niv);
                if (!dao.save(permissaoDepartamento)) {
                    erro = true;
                    break;
                }
                permissaoDepartamento = new PermissaoDepartamento();
            }
            if (erro) {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Erro ao adicionar permissão(s)!");
            } else {
                dao.commit();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                GenericaMensagem.info("Sucesso", "Permissão(s) adicionada(s) com sucesso");
            }
        }
        return null;
    }

    public String excluirPermissaoDepto() {
        if (!listaPermissoesAdicionadas.isEmpty()) {
            boolean erro = false;
            boolean temRegistros = false;
            Dao dao = new Dao();
            dao.openTransaction();
            for (int i = 0; i < listaPermissoesAdicionadas.size(); i++) {
                if (listaPermissoesAdicionadas.get(i).isSelected()) {
                    permissaoDepartamento = (PermissaoDepartamento) listaPermissoesAdicionadas.get(i).getPermissaoDepartamento();
                    if (!dao.delete(permissaoDepartamento)) {
                        erro = true;
                        temRegistros = false;
                        break;
                    }
                    temRegistros = true;
                }
                permissaoDepartamento = new PermissaoDepartamento();
            }
            if (erro) {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Erro ao remover permissão(s)!");
            } else {
                dao.commit();
                if (temRegistros) {
                    listaPermissoesAdicionadas.clear();
                    listaPermissoesDisponiveis.clear();
                    GenericaMensagem.info("Sucesso", "Permissão(s) removida(s) com sucesso");
                } else {
                    GenericaMensagem.info("Sistema", "Não foi selecionada nenhuma permissão!");
                }
            }
        }
        return null;
    }

    public String excluirPermissaoDeptoDBClick(PermissaoDepartamento pd) {
        if (!listaPermissoesAdicionadas.isEmpty()) {
            boolean erro = false;
            Dao dao = new Dao();
            dao.openTransaction();
            permissaoDepartamento = pd;
            if (!dao.delete(permissaoDepartamento)) {
                erro = true;
            }
            permissaoDepartamento = new PermissaoDepartamento();
            if (erro) {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Erro ao remover permissão(s)!");
            } else {
                dao.commit();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                GenericaMensagem.info("Sucesso", "Permissão(s) removida(s) com sucesso");
            }
        }
        return null;
    }

    public String excluirTodasPermissaoDepto() {
        if (!listaPermissoesAdicionadas.isEmpty()) {
            boolean erro = false;
            Dao dao = new Dao();
            dao.openTransaction();
            for (int i = 0; i < listaPermissoesAdicionadas.size(); i++) {
                permissaoDepartamento = listaPermissoesAdicionadas.get(i).getPermissaoDepartamento();
                if (!dao.delete(permissaoDepartamento)) {
                    erro = true;
                    break;
                }
            }
            if (erro) {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Erro ao remover permissão(s)!");
            } else {
                dao.commit();
                listaPermissoesAdicionadas.clear();
                listaPermissoesDisponiveis.clear();
                GenericaMensagem.info("Sucesso", "Permissão(s) removidas com sucesso");
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
            Dao dao = new Dao();
            List modulos = dao.list(new Modulo(), true);
            for (int i = 0; i < modulos.size(); i++) {
                listaModulos.add(new SelectItem(i,
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
            RotinaDao rotinaDao = new RotinaDao();
            List list = rotinaDao.pesquisaRotinasDisponiveisModulo(Integer.parseInt(listaModulos.get(idModulo).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listaRotinas.add(new SelectItem(i,
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
            Dao dao = new Dao();
            List eventos = dao.list(new Evento(), true);
            for (int i = 0; i < eventos.size(); i++) {
                listaEventos.add(new SelectItem(i, ((Evento) eventos.get(i)).getDescricao(), Integer.toString(((Evento) eventos.get(i)).getId())));
            }
        }
        return listaEventos;
    }

    public void setListaEventos(List<SelectItem> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public List<SelectItem> getListaNiveis() {
        if (listaNiveis.isEmpty()) {
            Dao dao = new Dao();
            List niveis = dao.list(new Nivel(), true);
            for (int i = 0; i < niveis.size(); i++) {
                listaNiveis.add(new SelectItem(i,
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
            Dao dao = new Dao();
            List departamentos = dao.list(new Departamento(), true);
            for (int i = 0; i < departamentos.size(); i++) {
                listaDepartamentos.add(new SelectItem(i,
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
        PermissaoDao permissaoDao = new PermissaoDao();
        listaPermissoes = permissaoDao.pesquisaTodosAgrupadosPorModulo(Integer.parseInt(listaModulos.get(idModulo).getDescription()));
        return listaPermissoes;
    }

    public void setListaPermissoes(List<Permissao> listaPermissoes) {
        this.listaPermissoes = listaPermissoes;
    }

    public List<ListaPermissaoDepartamento> getListaPermissoesDisponiveis() {
        if (listaPermissoesDisponiveis.isEmpty()) {
            //listaPermissoesDisponiveis.clear();
            PermissaoDepartamentoDB permissaoDepartamentoDB = new PermissaoDepartamentoDBToplink();
            int idDepto = Integer.parseInt(listaDepartamentos.get(idDepartamento).getDescription());
            int idNiv = Integer.parseInt(listaNiveis.get(idNivel).getDescription());
            List<Permissao> list = permissaoDepartamentoDB.listaPermissaoDepartamentoDisponivel(idDepto, idNiv, descricaoPesquisa);
            for (Permissao list1 : list) {
                listaPermissoesDisponiveis.add(new ListaPermissaoDepartamento(null, list1, false));
            }
        }
        return listaPermissoesDisponiveis;
    }

    public void setListaPermissoesDisponiveis(List<ListaPermissaoDepartamento> listaPermissoesDisponiveis) {
        this.listaPermissoesDisponiveis = listaPermissoesDisponiveis;
    }

    public List<ListaPermissaoDepartamento> getListaPermissoesAdicionadas() {
        if (listaPermissoesAdicionadas.isEmpty()) {
            PermissaoDepartamentoDB permissaoDepartamentoDB = new PermissaoDepartamentoDBToplink();
            int idDepto = Integer.parseInt(listaDepartamentos.get(idDepartamento).getDescription());
            int idNiv = Integer.parseInt(listaNiveis.get(idNivel).getDescription());
            List<PermissaoDepartamento> list = permissaoDepartamentoDB.listaPermissaoDepartamentoAdicionada(idDepto, idNiv, descricaoPesquisa);
            for (PermissaoDepartamento list1 : list) {
                listaPermissoesAdicionadas.add(new ListaPermissaoDepartamento(list1, list1.getPermissao(), false));
            }
        }
        return listaPermissoesAdicionadas;
    }

    public void setListaPermissoesAdicionadas(List<ListaPermissaoDepartamento> listaPermissoesAdicionadas) {
        this.listaPermissoesAdicionadas = listaPermissoesAdicionadas;
    }
}
