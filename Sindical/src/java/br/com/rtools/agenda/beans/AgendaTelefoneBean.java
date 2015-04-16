package br.com.rtools.agenda.beans;

import br.com.rtools.agenda.Agenda;
import br.com.rtools.agenda.AgendaContato;
import br.com.rtools.agenda.AgendaFavorito;
import br.com.rtools.agenda.AgendaTelefone;
import br.com.rtools.agenda.GrupoAgenda;
import br.com.rtools.agenda.TipoTelefone;
import br.com.rtools.agenda.dao.AgendaTelefoneDao;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mask;
import br.com.rtools.utilitarios.PF;
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
public class AgendaTelefoneBean implements Serializable {

    private Agenda agenda;
    private AgendaTelefone agendaTelefone;
    private AgendaContato agendaContato;
    private Pessoa pessoa;
    private Endereco endereco;
    private Usuario usuario;
    /**
     * <strong>SelectItem</strong>
     * <ul>
     * <li>[0] Tipos de Endereço</li>
     * <li>[1] Tipos de Telefone</li>
     * <li>[2] Grupos de Agenda</li>
     * <li>[3] DDD's </li>
     * <li>[4] Filtro Grupos Agenda</li>
     * </ul>
     *
     * @return SelectItem
     */
    private List<SelectItem>[] listSelectItem;
    private List<AgendaTelefone> listAgendaTelefones;
    private List<AgendaTelefone> listAgendas;
    private List<AgendaContato> listAgendaContato;
    private Integer[] indice;
    private String descricaoPesquisa;
    private String descricaoDDD;
    private String comoPesquisa;
    private String porPesquisa;
    private String message;
    private String tipoAgenda;
    private boolean mask;
    private boolean filtraPorGrupo;
    private boolean visibility;
    private boolean favoritos;
    private boolean numeroFavorito;

    @PostConstruct
    public void init() {
        agenda = new Agenda();
        agendaTelefone = new AgendaTelefone();
        agendaContato = new AgendaContato();
        pessoa = new Pessoa();
        endereco = new Endereco();
        usuario = new Usuario();
        listSelectItem = new ArrayList[4];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        listSelectItem[2] = new ArrayList<>();
        listSelectItem[3] = new ArrayList<>();
        indice = new Integer[5];
        indice[0] = 0;
        indice[1] = 0;
        indice[2] = 0;
        indice[3] = 0;
        indice[4] = 0;
        listAgendaTelefones = new ArrayList<>();
        listAgendaContato = new ArrayList<>();
        listAgendas = new ArrayList();
        descricaoPesquisa = "";
        descricaoDDD = "";
        comoPesquisa = "Inicial";
        porPesquisa = "nome";
        message = "";
        tipoAgenda = "agendaTelefone";
        mask = false;
        filtraPorGrupo = false;
        visibility = false;
        favoritos = false;
        numeroFavorito = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("agendaTelefoneBean");
        GenericaSessao.remove("agendaTelefonePesquisa");
        GenericaSessao.remove("pessoaPesquisa");
        GenericaSessao.remove("enderecoPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("agendaTelefoneBean");
    }

    public void putType(String type) {
        GenericaSessao.put("tipoAgendaTelefone", type);
    }

    public void openDialog() {
        visibility = true;
    }

    public void close() {
        clear();
        visibility = false;
        PF.closeDialog("dgl_adicionar");
        PF.update("form_agenda_telefone:i_panel_adicionar");
    }

    public void save() {
        if (agenda.getNome().equals("")) {
            message = "Informar o nome!";
            return;
        }
        agenda.setNome(agenda.getNome().toUpperCase());
        if (getListGrupoAgendas().isEmpty()) {
            message = "Informar o grupo agenda!";
            return;
        }
        if (getListTipoEnderecos().isEmpty()) {
            message = "Informar o tipo de endereço!";
            return;
        }
        Dao di = new Dao();
        agenda.setGrupoAgenda((GrupoAgenda) di.find(new GrupoAgenda(), Integer.parseInt(getListGrupoAgendas().get(indice[2]).getDescription())));
        agenda.setTipoEndereco((TipoEndereco) di.find(new TipoEndereco(), Integer.parseInt(getListTipoEnderecos().get(indice[0]).getDescription())));
        if (pessoa != null) {
            if (pessoa.getId() != -1) {
                agenda.setPessoa(pessoa);
            }
        }
        if (agenda.getPessoa().getId() == -1) {
            agenda.setPessoa(null);
        }
        NovoLog novoLog = new NovoLog();
        if (agenda.getId() == -1) {
            AgendaTelefoneDao agendaDB = new AgendaTelefoneDao();
            if (endereco != null) {
                if (endereco.getId() != -1) {
                    agenda.setEndereco(endereco);
                }
            }
            if (agenda.getEndereco().getId() == -1) {
                agenda.setEndereco(null);
            }
            if ((agendaDB.agendaExiste(agenda)).getId() != -1) {
                message = "Cadastro já existe!";
                return;
            }
            di.openTransaction();
            if (di.save(agenda)) {
                novoLog.save("ID: " + agenda.getId() + " - Grupo Agenda: " + agenda.getGrupoAgenda().getDescricao() + " - Nome: " + agenda.getNome() + " - Email(s): " + agenda.getEmail1() + " - " + agenda.getEmail2());
                di.commit();
                message = "Registro inserido com sucesso";
                listAgendas.clear();
            } else {
                di.rollback();
                message = "Erro ao inserir esse registro!";
            }
        } else {
            if (endereco != null) {
                if (endereco.getId() != -1) {
                    agenda.setEndereco(endereco);
                } else {
                    agenda.setEndereco(null);
                }
            } else {
                agenda.setEndereco(null);
            }
            Agenda a = (Agenda) di.find(agenda);
            String beforeUpdate = "ID: " + a.getId() + " - Grupo Agenda: " + a.getGrupoAgenda().getDescricao() + " - Nome: " + a.getNome() + " - Email(s): " + a.getEmail1() + " - " + agenda.getEmail2();

            di.openTransaction();
            if (di.update(agenda)) {
                novoLog.update(beforeUpdate, "ID: " + agenda.getId() + " - Grupo Agenda: " + agenda.getGrupoAgenda().getDescricao() + " - Nome: " + agenda.getNome() + " - Email(s): " + agenda.getEmail1() + " - " + agenda.getEmail2());
                di.commit();
                message = "Registro atualizado com sucesso";
                listAgendas.clear();
            } else {
                di.rollback();
                message = "Erro ao atualizar esse registro!";
            }
        }
    }

    public void delete() {
        if (agenda.getId() != -1) {
            Dao di = new Dao();
            AgendaTelefoneDao atdb = new AgendaTelefoneDao();
            List<AgendaFavorito> agendaFavoritos = (List<AgendaFavorito>) atdb.listaFavoritoPorAgenda(agenda.getId());
            di.openTransaction();
            for (AgendaFavorito f : agendaFavoritos) {
                AgendaFavorito af = (AgendaFavorito) di.find(new AgendaFavorito(), f.getId());
                if (!di.delete(af)) {
                    di.rollback();
                    message = "Erro ao excluir favorito da agenda!";
                    return;
                }
            }
            for (AgendaTelefone listaAgendaTelefone1 : listAgendaTelefones) {
                AgendaTelefone at = (AgendaTelefone) di.find(new AgendaTelefone(), listaAgendaTelefone1.getId());
                if (!di.delete(at)) {
                    di.rollback();
                    message = "Erro ao excluir telefones da agenda!";
                    return;
                }
            }
            agenda = (Agenda) di.find(new Agenda(), agenda.getId());
            if (di.delete(agenda)) {
                NovoLog novoLog = new NovoLog();
                novoLog.delete("ID: " + agenda.getId() + " - Grupo Agenda: " + agenda.getGrupoAgenda().getDescricao() + " - Nome: " + agenda.getNome() + " - Email(s): " + agenda.getEmail1() + " - " + agenda.getEmail2());
                di.commit();
                clear();
                message = "Registro excluído com sucesso";
                listAgendas.clear();
                agenda = new Agenda();
                pessoa = new Pessoa();
                endereco = new Endereco();
            } else {
                message = "Erro ao excluir esse registro!";
                di.rollback();
            }
        }
    }

    public Pessoa getPessoa() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            pessoa = (Pessoa) GenericaSessao.getObject("pessoaPesquisa", true);
            String nomeMemoria = "";
            String email1Memoria = "";
            String email2Memoria = "";
            if (agenda.getPessoa() != null) {
                if (agenda.getPessoa().getId() != -1) {
                    nomeMemoria = agenda.getNome();
                    if (!agenda.getEmail1().equals("")) {
                        email1Memoria = agenda.getEmail1();
                    }
                    if (!agenda.getEmail2().equals("")) {
                        email2Memoria = agenda.getEmail2();
                    }
                }
            }
            agenda.setPessoa(pessoa);
            if (!agenda.getPessoa().getNome().equals(nomeMemoria)) {
                agenda.setNome(nomeMemoria);
            }
            if (agenda.getPessoa().getEmail1() != null) {
                if (!agenda.getPessoa().getEmail1().equals(email1Memoria)) {
                    agenda.setNome(email1Memoria);
                }
            }
            if (agenda.getPessoa().getEmail2() != null) {
                if (!agenda.getPessoa().getEmail2().equals(email2Memoria)) {
                    agenda.setNome(email2Memoria);
                }
            }
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
            List<PessoaEndereco> pessoaEnderecos = pessoaEnderecoDB.pesquisaEndPorPessoa(pessoa.getId());
            if (!pessoaEnderecos.isEmpty()) {
                endereco = pessoaEnderecos.get(0).getEndereco();
            }
            PF.openDialog("dgl_adicionar");
            PF.update("form_agenda_telefone:i_panel_adicionar");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Endereco getEndereco() {
        if (GenericaSessao.exists("enderecoPesquisa")) {
            endereco = (Endereco) GenericaSessao.getObject("enderecoPesquisa", true);
            agenda.setEndereco(endereco);
            PF.openDialog("dgl_adicionar");
            PF.update("form_agenda_telefone:i_panel_adicionar");
        }
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Agenda getAgenda() {
        if (agenda.getId() != -1) {
            visibility = true;
        }
        getTipoAgenda();
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public List<SelectItem> getListTipoEnderecos() {
        if (listSelectItem[0].isEmpty()) {
            Dao di = new Dao();
            List<TipoEndereco> list = (List<TipoEndereco>) di.list("TipoEndereco", true);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<>();
            }
        }
        return listSelectItem[0];
    }

    public List<SelectItem> getListTipoTelefones() {
        if (listSelectItem[1].isEmpty()) {
            Dao di = new Dao();
            List<TipoTelefone> list = (List<TipoTelefone>) di.list("TipoTelefone", true);
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[1].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
            if (listSelectItem[1].isEmpty()) {
                listSelectItem[1] = new ArrayList<SelectItem>();
            }
        }
        return listSelectItem[1];
    }

    public List<SelectItem> getListGrupoAgendas() {
        listSelectItem[2].clear();
        List<GrupoAgenda> list;
        if (getTipoAgenda().equals("pesquisaAgendaTelefone")) {
            AgendaTelefoneDao telefoneDB = new AgendaTelefoneDao();
            list = (List<GrupoAgenda>) telefoneDB.listaGrupoAgendaPorUsuario(getUsuario().getId());
        } else {
            Dao di = new Dao();
            list = (List<GrupoAgenda>) di.list("GrupoAgenda", true);
        }
        for (int i = 0; i < list.size(); i++) {
            listSelectItem[2].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
        }
        if (listSelectItem[2].isEmpty()) {
            listSelectItem[2] = new ArrayList<>();
        }
        return listSelectItem[2];
    }

    public AgendaTelefone getAgendaTelefone() {
        return agendaTelefone;
    }

    public void setAgendaTelefone(AgendaTelefone agendaTelefone) {
        this.agendaTelefone = agendaTelefone;
    }

    public List<AgendaTelefone> getListAgendaTelefones() {
        AgendaTelefoneDao agendaDB = new AgendaTelefoneDao();
        if (agenda.getId() != -1) {
            listAgendaTelefones = agendaDB.listaAgendaTelefone(agenda.getId());
        }
        return listAgendaTelefones;
    }

    public void setListAgendaTelefones(List<AgendaTelefone> listAgendaTelefones) {
        this.listAgendaTelefones = listAgendaTelefones;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "Inicial";
        listAgendas.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "Parcial";
        listAgendas.clear();
    }

    public void edit(Agenda a) {
        visibility = true;
        agenda = a;
        pessoa = new Pessoa();
        endereco = new Endereco();
        if (agenda.getGrupoAgenda() != null) {
            for (int i = 0; i < getListGrupoAgendas().size(); i++) {
                if (Integer.valueOf(getListGrupoAgendas().get(i).getDescription()) == agenda.getGrupoAgenda().getId()) {
                    indice[2] = i;
                    break;
                }
            }
        }
        if (agenda.getTipoEndereco() != null) {
            for (int i = 0; i < getListTipoEnderecos().size(); i++) {
                if (Integer.valueOf(getListTipoEnderecos().get(i).getDescription()) == agenda.getTipoEndereco().getId()) {
                    indice[0] = i;
                    break;
                }
            }
        }
        if (agenda.getEndereco() != null) {
            endereco = agenda.getEndereco();
        }
        if (agenda.getPessoa() != null) {
            pessoa = agenda.getPessoa();
        }
        listAgendaTelefones.clear();
        getListAgendaTelefones();
        agendaTelefone = new AgendaTelefone();
        getListAgendaContato();
        PF.openDialog("dgl_adicionar");
        PF.update("form_agenda_telefone:i_panel_adicionar");
    }

    public void view(AgendaTelefone at) {
        pessoa = new Pessoa();
        endereco = new Endereco();
        agendaTelefone = at;
        if (agendaTelefone.getAgenda().getEndereco() != null) {
            endereco = agendaTelefone.getAgenda().getEndereco();
        }
        if (agendaTelefone.getAgenda().getPessoa() != null) {
            pessoa = agendaTelefone.getAgenda().getPessoa();
        }
        if (agendaTelefone.getId() != -1) {
            agenda = agendaTelefone.getAgenda();
        } else {
            agenda = agendaTelefone.getAgenda();
        }
        listAgendaTelefones.clear();
        getListAgendaTelefones();
        PF.update("form_pesquisa_agenda_telefone:i_detalhes_contato");
        PF.openDialog("dgl_modal_detalhes");
    }

    public String addAgendaTelefone() {
        if (agendaTelefone.getTelefone().equals("")) {
            GenericaMensagem.warn("Validação", "Informar o número de telefone!");
            return null;
        }
        if (getListTipoTelefones().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar o tipo de telefone!");
            return null;
        }
        Dao di = new Dao();
        NovoLog novoLog = new NovoLog();
        agendaTelefone.setTipoTelefone((TipoTelefone) di.find(new TipoTelefone(), Integer.parseInt(getListTipoTelefones().get(indice[1]).getDescription())));
        if (agenda.getId() != -1) {
            if (agendaTelefone.getId() == -1) {
                AgendaTelefoneDao agendaDB = new AgendaTelefoneDao();
                agendaTelefone.setAgenda(agenda);
                if (((AgendaTelefone) agendaDB.agendaTelefoneExiste(agendaTelefone)).getId() != -1) {
                    GenericaMensagem.warn("Validação", "Telefone já existe!");
                    return null;
                }
                di.openTransaction();
                if (di.save(agendaTelefone)) {
                    novoLog.save("Telefone ID: " + agendaTelefone.getId() + " - Tipo Telefone: " + agendaTelefone.getTipoTelefone().getDescricao() + " - Telefone: (" + agendaTelefone.getDdi() + ") " + agendaTelefone.getDdd() + " " + agendaTelefone.getTelefone() + " - Agenda (" + agendaTelefone.getAgenda().getId() + ")");
                    GenericaMensagem.info("Sucesso", "Registro adicionado com sucesso");
                    di.commit();
                    listAgendaTelefones.clear();
                } else {
                    GenericaMensagem.warn("Erro", "Erro ao adicionar registro");
                    di.rollback();
                }
            } else {
                AgendaTelefone at = (AgendaTelefone) di.find(agendaTelefone);
                String beforeUpdate = "Telefone ID: " + at.getId() + " - Tipo Telefone: " + at.getTipoTelefone().getDescricao() + " - Telefone: (" + at.getDdi() + ") " + at.getDdd() + " " + at.getTelefone() + " - Agenda (" + at.getAgenda().getId() + ")";
                di.openTransaction();
                if (di.update(agendaTelefone)) {
                    novoLog.update(beforeUpdate, "Telefone ID: " + agendaTelefone.getId() + " - Tipo Telefone: " + agendaTelefone.getTipoTelefone().getDescricao() + " - Telefone: (" + agendaTelefone.getDdi() + ") " + agendaTelefone.getDdd() + " " + agendaTelefone.getTelefone() + " - Agenda (" + agendaTelefone.getAgenda().getId() + ")");
                    GenericaMensagem.info("Sucesso", "Registro atualizado com sucesso");
                    di.commit();
                    listAgendaTelefones.clear();
                } else {
                    di.rollback();
                    GenericaMensagem.warn("Erro", "Erro ao atualizar telefone");
                }
            }
        }
        agendaTelefone = new AgendaTelefone();
        return null;
    }

    public String removeAgendaTelefone(AgendaTelefone at) {
        Dao di = new Dao();
        at = (AgendaTelefone) di.find(at);
        di.openTransaction();
        if (di.delete(at)) {
            NovoLog novoLog = new NovoLog();
            novoLog.delete("Telefone ID: " + at.getId() + " - Tipo Telefone: " + at.getTipoTelefone().getDescricao() + " - Telefone: (" + at.getDdi() + ") " + at.getDdd() + " " + at.getTelefone() + " - Agenda (" + at.getAgenda().getId() + ")");
            di.commit();
            GenericaMensagem.info("Sucesso", "Registro excluído com sucesso");
            listAgendaTelefones.clear();
            agendaTelefone = new AgendaTelefone();
        } else {
            di.rollback();
            GenericaMensagem.warn("Erro", "Falha ao excluír esse registro!");
        }
        return null;
    }

    public String editAgendaTelefone(AgendaTelefone at) {
        agendaTelefone = at;
        for (int i = 0; i < getListTipoTelefones().size(); i++) {
            if (Integer.valueOf(getListTipoTelefones().get(i).getDescription()) == agendaTelefone.getTipoTelefone().getId()) {
                indice[1] = i;
                break;
            }
        }
        return null;
    }

    public boolean isFiltraPorGrupo() {
        return filtraPorGrupo;
    }

    public void setFiltraPorGrupo(boolean filtraPorGrupo) {
        this.filtraPorGrupo = filtraPorGrupo;
    }

    public String getDescricaoDDD() {
        return descricaoDDD;
    }

    public void setDescricaoDDD(String descricaoDDD) {
        this.descricaoDDD = descricaoDDD;
    }

    public List<SelectItem> getListaDDD() {
        if (listSelectItem[3].isEmpty()) {
            AgendaTelefoneDao atd = new AgendaTelefoneDao();
            List list = atd.DDDAgrupado();
            int i = 0;
            listSelectItem[3].add(new SelectItem(i, "DDD", ""));
            for (i = 0; i < list.size(); i++) {
                listSelectItem[3].add(new SelectItem(i + 1, ((List) list.get(i)).get(0).toString(), ((List) list.get(i)).get(0).toString()));
            }
        }
        return listSelectItem[3];
    }

    public String getTipoAgenda() {
        if (GenericaSessao.exists("tipoAgendaTelefone")) {
            tipoAgenda = (String) GenericaSessao.getObject("tipoAgendaTelefone", true);
        }
        return tipoAgenda;
    }

    public void setTipoAgenda(String tipoAgenda) {
        this.tipoAgenda = tipoAgenda;
    }

    public String getMascaraPesquisa() {
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }

    public boolean isMask() {
        mask = !porPesquisa.equals("telefone");
        return mask;
    }

    public void setMask(boolean mask) {
        this.mask = mask;
    }

    public List<AgendaTelefone> getListAgendas() {
        if (listAgendas.isEmpty()) {
            AgendaTelefoneDao agendaDB = new AgendaTelefoneDao();
            int nrGrupoAgenda = 0;
            if (filtraPorGrupo) {
                nrGrupoAgenda = Integer.parseInt(getListGrupoAgendas().get(indice[4]).getDescription());
            }
            descricaoDDD = "";
            if (!getListaDDD().isEmpty()) {
                descricaoDDD = getListaDDD().get(indice[3]).getDescription();
                if (descricaoDDD.equals("DDD")) {
                    descricaoDDD = "";
                }
            }
            int idUsuario = 0;
            if (getTipoAgenda().equals("pesquisaAgendaTelefone")) {
                idUsuario = getUsuario().getId();
            }
            List<AgendaTelefone> listAgendaTelefonesx = agendaDB.pesquisaAgendaTelefonex(descricaoDDD, descricaoPesquisa, porPesquisa, comoPesquisa, nrGrupoAgenda, favoritos, idUsuario);
            for (AgendaTelefone listAgendaTelefone : listAgendaTelefonesx) {
                AgendaTelefone at = listAgendaTelefone;
                if (at.getAgenda().getPessoa() == null) {
                    at.getAgenda().setPessoa(new Pessoa());
                }
                if (at.getAgenda().getEndereco() == null) {
                    at.getAgenda().setEndereco(new Endereco());
                }
                listAgendas.add(at);
            }
        }
        return listAgendas;
    }

    public void setListAgendas(List<AgendaTelefone> listAgendas) {
        this.listAgendas = listAgendas;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public String getMascara() {
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }

    public boolean isFavoritos() {
        return favoritos;
    }

    public void setFavoritos(boolean favoritos) {
        this.favoritos = favoritos;
    }

    public Usuario getUsuario() {
        if (usuario.getId() == -1) {
            usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isNFavorito(int idAgenda) {
        if (idAgenda == -1 || idAgenda == 0) {
            return false;
        }
        AgendaTelefoneDao atd = new AgendaTelefoneDao();
        AgendaFavorito af = atd.favorito(idAgenda, getUsuario().getId());
        return af != null;
    }

    public void addOrRemoveFavorito() {
        AgendaTelefoneDao atd = new AgendaTelefoneDao();
        AgendaFavorito af = atd.favorito(agendaTelefone.getAgenda().getId(), usuario.getId());
        Dao di = new Dao();
        if (af == null) {
            af = new AgendaFavorito();
            af.setAgenda(agendaTelefone.getAgenda());
            af.setUsuario(usuario);
            di.openTransaction();
            if (di.save(af)) {
                di.commit();
                getListAgendas().clear();
            } else {
                di.rollback();
            }
        } else {
            di.openTransaction();
            if (di.delete(di.find(af))) {
                getListAgendas().clear();
                di.commit();
            } else {
                di.rollback();
            }
        }
    }

    public boolean isNumeroFavorito() {
        return isNFavorito(agenda.getId());
    }

    public void setNumeroFavorito(boolean numeroFavorito) {
        this.numeroFavorito = numeroFavorito;
    }

    public List<AgendaContato> getListAgendaContato() {
        if (agenda.getId() != -1) {
            if (listAgendaContato.isEmpty()) {
                Dao di = new Dao();
                listAgendaContato = (List<AgendaContato>) di.listQuery(new AgendaContato(), "findByAgenda", new Object[]{agenda.getId()});
            }
        }
        return listAgendaContato;
    }

    public void setListAgendaContato(List<AgendaContato> listAgendaContato) {
        this.listAgendaContato = listAgendaContato;
    }

    public Integer[] getIndice() {
        return indice;
    }

    public void setIndice(Integer[] indice) {
        this.indice = indice;
    }

    public AgendaContato getAgendaContato() {
        return agendaContato;
    }

    public void setAgendaContato(AgendaContato agendaContato) {
        this.agendaContato = agendaContato;
    }

    public void remove(String type) {
        switch (type) {
            case "pessoa":
                pessoa = new Pessoa();
                break;
            case "endereco":
                endereco = null;
                agenda.setComplemento("");
                agenda.setNumero("");
                Dao di = new Dao();
                agenda.setTipoEndereco((TipoEndereco) di.find(new TipoEndereco(), 1));
                indice[0] = 0;
                break;
        }
        PF.update("form_agenda_telefone:i_panel_adicionar");
    }

    public void addAgendaContato() {
        if (agendaContato.getContato().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar o nome do contato!");
            return;
        }
        if (agendaContato.getNascimento() == null) {
            GenericaMensagem.warn("Validação", "Informar data de nascimento!");
            return;
        }
        agendaContato.setAgenda(agenda);
        Dao di = new Dao();
        if (agendaContato.getId() == -1) {
            for (AgendaContato lac : listAgendaContato) {
                if (agendaContato.getContato().equals(lac.getContato())) {
                    GenericaMensagem.warn("Validação", "Contato já cadastrado!");
                    return;
                }
            }
            if (di.save(agendaContato, true)) {
                GenericaMensagem.info("Sucesso", "Contato adicionado");
                agendaContato = new AgendaContato();
                listAgendaContato.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao adicionar contato!");
            }
        } else {
            updateAgendaContato(agendaContato);
        }
    }

    public void editAgendaContato(AgendaContato ac) {
        Dao di = new Dao();
        agendaContato = (AgendaContato) di.rebind(ac);
    }

    public void removeAgendaContato(AgendaContato ac) {
        if (ac.getId() != -1) {
            Dao di = new Dao();
            if (di.delete(ac, true)) {
                GenericaMensagem.info("Sucesso", "Contato removido");
                agendaContato = new AgendaContato();
                listAgendaContato.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao remover contato!");
            }
        }
    }

    public void updateAgendaContato(AgendaContato ac) {
        if (ac.getId() != -1) {
            Dao di = new Dao();
            if (di.update(ac, true)) {
                GenericaMensagem.info("Sucesso", "Contato atualiado");
                listAgendaContato.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar contato!");
            }
        }
    }

    public void clearAgendaContato() {
        agendaContato = new AgendaContato();
    }
}
