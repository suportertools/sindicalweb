package br.com.rtools.agenda.beans;

import br.com.rtools.agenda.Agenda;
import br.com.rtools.agenda.AgendaTelefone;
import br.com.rtools.agenda.GrupoAgenda;
import br.com.rtools.agenda.TipoTelefone;
import br.com.rtools.agenda.db.AgendaTelefoneDB;
import br.com.rtools.agenda.db.AgendaTelefoneDBToplink;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mask;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class AgendaTelefoneBean implements Serializable {

    private Agenda agenda = new Agenda();
    private AgendaTelefone agendaTelefone = new AgendaTelefone();
    private Pessoa pessoa = new Pessoa();
    private Endereco endereco = new Endereco();
    private List<SelectItem> listaTipoEnderecos = new ArrayList<SelectItem>();
    private List<SelectItem> listaTipoTelefones = new ArrayList<SelectItem>();
    private List<SelectItem> listaGrupoAgendas = new ArrayList<SelectItem>();
    private List<SelectItem> listaDDD = new ArrayList<SelectItem>();
    private List<AgendaTelefone> listaAgendaTelefones = new ArrayList<AgendaTelefone>();
    private List<AgendaTelefone> listaAgendas = new ArrayList();
    private int idTipoEndereco = 0;
    private int idTipoTelefone = 0;
    private int idDDD = 0;
    private int idGrupoAgenda = 0;
    private int idFiltroGrupoAgenda = 0;
    private String descricaoPesquisa = "";
    private String descricaoDDD = "";
    private String comoPesquisa = "Inicial";
    private String porPesquisa = "nome";
    private String mensagem;
    private String tipoAgenda = "agendaTelefone";
    private boolean mask = false;
    private boolean filtraPorGrupo = false;
    private boolean visibility = false;

    public void novo() {
        agenda = new Agenda();
        agendaTelefone = new AgendaTelefone();
        pessoa = new Pessoa();
        endereco = new Endereco();
        listaAgendaTelefones = new ArrayList<AgendaTelefone>();
        listaAgendas.clear();
        idTipoTelefone = 0;
        idGrupoAgenda = 0;
        idTipoEndereco = 0;
        idFiltroGrupoAgenda = 0;
        idDDD = 0;
        descricaoPesquisa = "";
        descricaoDDD = "";
        comoPesquisa = "Inicial";
        porPesquisa = "nome";
        mensagem = "";
        visibility = true;
    }

    public void openDialog() {
        visibility = true;
    }

    public void close() {
        novo();
        visibility = false;
        RequestContext.getCurrentInstance().execute("dgl_adicionar.hide()");
        RequestContext.getCurrentInstance().update("form_agenda_telefone:i_panel_adicionar");
    }

    public void salvar() {
        if (agenda.getNome().equals("")) {
            mensagem = "Informar o nome!";
            return;
        }
        agenda.setNome(agenda.getNome().toUpperCase());
        if (listaGrupoAgendas.isEmpty()) {
            mensagem = "Informar o grupo agenda!";
            return;
        }
        if (listaTipoEnderecos.isEmpty()) {
            mensagem = "Informar o tipo de endereço!";
            return;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        agenda.setGrupoAgenda((GrupoAgenda) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaGrupoAgendas.get(idGrupoAgenda).getDescription()), "GrupoAgenda"));
        agenda.setTipoEndereco((TipoEndereco) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaTipoEnderecos.get(idTipoEndereco).getDescription()), "TipoEndereco"));
        if (agenda.getId() == -1) {
            AgendaTelefoneDB agendaDB = new AgendaTelefoneDBToplink();
            if (pessoa != null) {
                if (pessoa.getId() != -1) {
                    agenda.setPessoa(pessoa);
                }
            }
            if (agenda.getPessoa().getId() == -1) {
                agenda.setPessoa(null);
            }
            if (endereco != null) {
                if (endereco.getId() != -1) {
                    agenda.setEndereco(endereco);
                }
            }
            if (agenda.getEndereco().getId() == -1) {
                agenda.setEndereco(null);
            }
            if ((agendaDB.agendaExiste(agenda)).getId() != -1) {
                mensagem = "Cadastro já existe!";
                return;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(agenda)) {
                salvarAcumuladoDB.comitarTransacao();
                mensagem = "Registro inserido com sucesso";
                listaAgendas.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao inserir esse registro!";
            }
        } else {
            if (endereco != null) {
                if (endereco.getId() != -1) {
                    agenda.setEndereco(endereco);
                }
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(agenda)) {
                salvarAcumuladoDB.comitarTransacao();
                mensagem = "Registro atualizado com sucesso";
                listaAgendas.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao atualizar esse registro!";
            }
        }
    }

    public void excluir() {
        if (agenda.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            salvarAcumuladoDB.abrirTransacao();
            for (AgendaTelefone listaAgendaTelefone1 : listaAgendaTelefones) {
                AgendaTelefone at = (AgendaTelefone) salvarAcumuladoDB.pesquisaCodigo(listaAgendaTelefone1.getId(), "AgendaTelefone");
                if (!salvarAcumuladoDB.deletarObjeto(at)) {
                    salvarAcumuladoDB.desfazerTransacao();
                    mensagem = "Erro ao excluir telefones da agenda!";
                    return;
                }
            }
            agenda = (Agenda) salvarAcumuladoDB.pesquisaCodigo(agenda.getId(), "Agenda");
            if (salvarAcumuladoDB.deletarObjeto(agenda)) {
                salvarAcumuladoDB.comitarTransacao();
                novo();
                mensagem = "Registro excluído com sucesso";
                listaAgendas.clear();
                agenda = new Agenda();
                pessoa = new Pessoa();
                endereco = new Endereco();
            } else {
                mensagem = "Erro ao excluir esse registro!";
                salvarAcumuladoDB.desfazerTransacao();
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
            RequestContext.getCurrentInstance().execute("dgl_adicionar.show()");
            RequestContext.getCurrentInstance().update("form_agenda_telefone:i_panel_adicionar");
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
            RequestContext.getCurrentInstance().execute("dgl_adicionar.show()");
            RequestContext.getCurrentInstance().update("form_agenda_telefone:i_panel_adicionar");
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

    public List<SelectItem> getListaTipoEnderecos() {
        if (listaTipoEnderecos.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<TipoEndereco> list = (List<TipoEndereco>) salvarAcumuladoDB.listaObjeto("TipoEndereco", true);
            for (int i = 0; i < list.size(); i++) {
                listaTipoEnderecos.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listaTipoEnderecos;
    }

    public void setListaTipoEnderecos(List<SelectItem> listaTipoEnderecos) {
        this.listaTipoEnderecos = listaTipoEnderecos;
    }

    public List<SelectItem> getListaTipoTelefones() {
        if (listaTipoTelefones.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<TipoTelefone> list = (List<TipoTelefone>) salvarAcumuladoDB.listaObjeto("TipoTelefone", true);
            for (int i = 0; i < list.size(); i++) {
                listaTipoTelefones.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listaTipoTelefones;
    }

    public void setListaTipoTelefones(List<SelectItem> listaTipoTelefones) {
        this.listaTipoTelefones = listaTipoTelefones;
    }

    public List<SelectItem> getListaGrupoAgendas() {
        listaGrupoAgendas.clear();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        List<GrupoAgenda> list = (List<GrupoAgenda>) salvarAcumuladoDB.listaObjeto("GrupoAgenda", true);
        for (int i = 0; i < list.size(); i++) {
            listaGrupoAgendas.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
        }
        return listaGrupoAgendas;
    }

    public void setListaGrupoAgendas(List<SelectItem> listaGrupoAgendas) {
        this.listaGrupoAgendas = listaGrupoAgendas;
    }

    public int getIdTipoEndereco() {
        return idTipoEndereco;
    }

    public void setIdTipoEndereco(int idTipoEndereco) {
        this.idTipoEndereco = idTipoEndereco;
    }

    public AgendaTelefone getAgendaTelefone() {
        return agendaTelefone;
    }

    public void setAgendaTelefone(AgendaTelefone agendaTelefone) {
        this.agendaTelefone = agendaTelefone;
    }

    public List<AgendaTelefone> getListaAgendaTelefones() {
        AgendaTelefoneDB agendaDB = new AgendaTelefoneDBToplink();
        if (agenda.getId() != -1) {
            listaAgendaTelefones = agendaDB.listaAgendaTelefone(agenda.getId());
        }
        return listaAgendaTelefones;
    }

    public void setListaAgendaTelefones(List<AgendaTelefone> listaAgendaTelefones) {
        this.listaAgendaTelefones = listaAgendaTelefones;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
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
        listaAgendas.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "Parcial";
        listaAgendas.clear();
    }

    public void editar(Agenda a) {
        visibility = true;
        agenda = a;
        pessoa = new Pessoa();
        endereco = new Endereco();
        if (agenda.getGrupoAgenda() != null) {
            for (int i = 0; i < listaGrupoAgendas.size(); i++) {
                if (Integer.valueOf(listaGrupoAgendas.get(i).getDescription()) == agenda.getGrupoAgenda().getId()) {
                    idGrupoAgenda = i;
                    break;
                }
            }
        }
        if (agenda.getTipoEndereco() != null) {
            for (int i = 0; i < listaTipoEnderecos.size(); i++) {
                if (Integer.valueOf(listaTipoEnderecos.get(i).getDescription()) == agenda.getTipoEndereco().getId()) {
                    idTipoEndereco = i;
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
        listaAgendaTelefones.clear();
        getListaAgendaTelefones();
        agendaTelefone = new AgendaTelefone();
        RequestContext.getCurrentInstance().execute("dgl_adicionar.show()");
        RequestContext.getCurrentInstance().update("form_agenda_telefone:i_panel_adicionar");
    }

    public void visualizar(AgendaTelefone at) {
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
        }
        listaAgendaTelefones.clear();
        getListaAgendaTelefones();
        RequestContext.getCurrentInstance().update("form_pesquisa_agenda_telefone:i_detalhes_contato");
        RequestContext.getCurrentInstance().execute("dgl_modal_detalhes.show();");
    }

    public int getIdTipoTelefone() {
        return idTipoTelefone;
    }

    public void setIdTipoTelefone(int idTipoTelefone) {
        this.idTipoTelefone = idTipoTelefone;
    }

    public String adicionarAgendaTelefone() {
        if (agendaTelefone.getTelefone().equals("")) {
            GenericaMensagem.warn("Validação", "Informar o número de telefone!");
            return null;
        }
        if (listaTipoTelefones.isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar o tipo de telefone!");
            return null;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        agendaTelefone.setTipoTelefone((TipoTelefone) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaTipoTelefones.get(idTipoTelefone).getDescription()), "TipoTelefone"));
        if (agenda.getId() != -1) {
            if (agendaTelefone.getId() == -1) {
                AgendaTelefoneDB agendaDB = new AgendaTelefoneDBToplink();
                agendaTelefone.setAgenda(agenda);
                if (((AgendaTelefone) agendaDB.agendaTelefoneExiste(agendaTelefone)).getId() != -1) {
                    GenericaMensagem.warn("Validação", "Telefone já existe!");
                    return null;
                }
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.inserirObjeto(agendaTelefone)) {
                    GenericaMensagem.info("Sucesso", "Registro adicionado com sucesso");
                    salvarAcumuladoDB.comitarTransacao();
                    listaAgendaTelefones.clear();
                } else {
                    GenericaMensagem.warn("Erro", "Erro ao adicionar registro");
                    salvarAcumuladoDB.desfazerTransacao();
                }
            } else {
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.alterarObjeto(agendaTelefone)) {
                    GenericaMensagem.info("Sucesso", "Registro atualizado com sucesso");
                    salvarAcumuladoDB.comitarTransacao();
                    listaAgendaTelefones.clear();
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                    GenericaMensagem.warn("Erro", "Erro ao atualizar telefone");
                }
            }
        }
        agendaTelefone = new AgendaTelefone();
        return null;
    }

    public String excluirAgendaTelefone(AgendaTelefone at) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        at = (AgendaTelefone) salvarAcumuladoDB.pesquisaCodigo(at.getId(), "AgendaTelefone");
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.deletarObjeto(at)) {
            salvarAcumuladoDB.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Registro excluído com sucesso");
            listaAgendaTelefones.clear();
            agendaTelefone = new AgendaTelefone();
        } else {
            salvarAcumuladoDB.comitarTransacao();
            GenericaMensagem.warn("Erro", "Falha ao excluír esse registro!");
        }
        return null;
    }

    public String editarAgendaTelefone(AgendaTelefone at) {
        agendaTelefone = at;
        for (int i = 0; i < listaTipoTelefones.size(); i++) {
            if (Integer.valueOf(listaTipoTelefones.get(i).getDescription()) == agendaTelefone.getTipoTelefone().getId()) {
                idTipoTelefone = i;
                break;
            }
        }
        return null;
    }

    public int getIdGrupoAgenda() {
        return idGrupoAgenda;
    }

    public void setIdGrupoAgenda(int idGrupoAgenda) {
        this.idGrupoAgenda = idGrupoAgenda;
    }

    public int getIdFiltroGrupoAgenda() {
        return idFiltroGrupoAgenda;
    }

    public void setIdFiltroGrupoAgenda(int idFiltroGrupoAgenda) {
        this.idFiltroGrupoAgenda = idFiltroGrupoAgenda;
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
        if (listaDDD.isEmpty()) {
            AgendaTelefoneDB agendaTelefoneDB = new AgendaTelefoneDBToplink();
            List list = agendaTelefoneDB.DDDAgrupado();
            int i = 0;
            listaDDD.add(new SelectItem(i, "DDD", ""));
            for (i = 0; i < list.size(); i++) {
                listaDDD.add(new SelectItem(i + 1, ((List) list.get(i)).get(0).toString(), ((List) list.get(i)).get(0).toString()));
            }
        }
        return listaDDD;
    }

    public void setListaDDD(List<SelectItem> listaDDD) {
        this.listaDDD = listaDDD;
    }

    public int getIdDDD() {
        return idDDD;
    }

    public void setIdDDD(int idDDD) {
        this.idDDD = idDDD;
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

    public List<AgendaTelefone> getListaAgendas() {
        if (listaAgendas.isEmpty()) {
            AgendaTelefoneDB agendaDB = new AgendaTelefoneDBToplink();
            int nrGrupoAgenda = 0;
            if (filtraPorGrupo) {
                nrGrupoAgenda = Integer.parseInt(listaGrupoAgendas.get(idFiltroGrupoAgenda).getDescription());
            }
            descricaoDDD = "";
            if (!listaDDD.isEmpty()) {
                descricaoDDD = getListaDDD().get(idDDD).getDescription();
                if (descricaoDDD.equals("DDD")) {
                    descricaoDDD = "";
                }
            }
            List<AgendaTelefone> listAgendaTelefones = agendaDB.pesquisaAgendaTelefone(descricaoDDD, descricaoPesquisa, porPesquisa, comoPesquisa, nrGrupoAgenda);
            for (AgendaTelefone listAgendaTelefone : listAgendaTelefones) {
                AgendaTelefone at = listAgendaTelefone;
                if (at.getAgenda().getPessoa() == null) {
                    at.getAgenda().setPessoa(new Pessoa());
                }
                if (at.getAgenda().getEndereco() == null) {
                    at.getAgenda().setEndereco(new Endereco());
                }
                listaAgendas.add(at);
            }
        }
        return listaAgendas;
    }

    public void setListaAgendas(List<AgendaTelefone> listaAgendas) {
        this.listaAgendas = listaAgendas;
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
}
