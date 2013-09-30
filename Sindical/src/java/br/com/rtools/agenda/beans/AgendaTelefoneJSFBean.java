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
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class AgendaTelefoneJSFBean implements java.io.Serializable {

    private Agenda agenda = new Agenda();
    private AgendaTelefone agendaTelefone = new AgendaTelefone();
    private Pessoa pessoa = new Pessoa();
    private Endereco endereco = new Endereco();
    private List<TipoEndereco> listaTipoEnderecos = new ArrayList<TipoEndereco>();
    private List<TipoTelefone> listaTipoTelefones = new ArrayList<TipoTelefone>();
    private List<GrupoAgenda> listaGrupoAgendas = new ArrayList<GrupoAgenda>();
    private List<SelectItem> listaDDD = new ArrayList<SelectItem>();
    private List<AgendaTelefone> listaAgendaTelefones = new ArrayList<AgendaTelefone>();
    private List listaAgendas = new ArrayList();
    private List listaAgendaTelefone = new ArrayList();
    private int idTipoEndereco = 0;
    private int idTipoTelefone = 0;
    private int idDDD = 0;
    private int idGrupoAgenda = 0;
    private int idFiltroGrupoAgenda = 0;
    private int idIndexAgendaTelefone = 0;
    private int idIndexAgenda = 0;
    private String descricaoPesquisa = "";
    private String descricaoDDD = "";
    private String comoPesquisa = "Inicial";
    private String porPesquisa = "nome";
    private String msgConfirma;
    private String msgAgendaTelefone = "";
    private String tipoAgenda = "agendaTelefone";
    private boolean filtraPorGrupo = false;

    public void limpar() {
        agenda = new Agenda();
        agendaTelefone = new AgendaTelefone();
        pessoa = new Pessoa();
        endereco = new Endereco();
        listaAgendaTelefones = new ArrayList<AgendaTelefone>();
        idTipoEndereco = 0;
        idTipoTelefone = 0;
        idGrupoAgenda = 0;
        idFiltroGrupoAgenda = 0;
        idDDD = 0;
        idIndexAgendaTelefone = 0;
        idIndexAgenda = 0;
        descricaoPesquisa = "";
        descricaoDDD = "";
        comoPesquisa = "Inicial";
        porPesquisa = "nome";
        msgConfirma = "";
        msgAgendaTelefone = "";
    }

    public String novo() {
        limpar();
        return "agendaTelefone";
    }

    public String salvar() {
        if (agenda.getNome().equals("")) {
            msgConfirma = "Informar o nome!";
            return null;
        }
        if (listaGrupoAgendas.isEmpty()) {
            msgConfirma = "Informar o grupo agenda!";
            return null;
        }
        if (listaTipoEnderecos.isEmpty()) {
            msgConfirma = "Informar o tipo de endereço!";
            return null;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        agenda.setGrupoAgenda((GrupoAgenda) salvarAcumuladoDB.pesquisaCodigo(idGrupoAgenda, "GrupoAgenda"));
        agenda.setTipoEndereco((TipoEndereco) salvarAcumuladoDB.pesquisaCodigo(idTipoEndereco, "TipoEndereco"));
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
            if (((Agenda) agendaDB.agendaExiste(agenda)).getId() != -1) {
                msgConfirma = "Cadastro já existe!";
                return null;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(agenda)) {
                salvarAcumuladoDB.comitarTransacao();
                msgConfirma = "Registro inserido com sucesso";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao inserir esse registro!";
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
                msgConfirma = "Registro atualizado com sucesso";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao atualizar esse registro!";
            }
        }
        listaAgendas.clear();
        getListaAgendas();
        return null;
    }

    public String excluir() {
        msgConfirma = "";
        if (agenda.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            salvarAcumuladoDB.abrirTransacao();
            for (int i = 0; i < listaAgendaTelefones.size(); i++) {
                AgendaTelefone at = (AgendaTelefone) salvarAcumuladoDB.pesquisaCodigo(listaAgendaTelefones.get(i).getId(), "AgendaTelefone");
                if (!salvarAcumuladoDB.deletarObjeto(at)) {
                    salvarAcumuladoDB.desfazerTransacao();
                    msgConfirma = "Erro ao excluir telefones da agenda!";
                    return null;
                }
            }
            agenda = (Agenda) salvarAcumuladoDB.pesquisaCodigo(agenda.getId(), "Agenda");
            if (salvarAcumuladoDB.deletarObjeto(agenda)) {
                salvarAcumuladoDB.comitarTransacao();
                limpar();
                msgConfirma = "Registro excluído com sucesso";
                listaAgendas.clear();
                agenda = new Agenda();
                pessoa = new Pessoa();
                endereco = new Endereco();
            } else {
                msgConfirma = "Erro ao excluir esse registro!";
                salvarAcumuladoDB.desfazerTransacao();
            }
        }
        return null;
    }

    public Pessoa getPessoa() {
        if ((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
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
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Endereco getEndereco() {
        if ((Endereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa") != null) {
            endereco = (Endereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa");
            agenda.setEndereco(endereco);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        }
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Agenda getAgenda() {
        getTipoAgenda();
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public List<TipoEndereco> getListaTipoEnderecos() {
        if (listaTipoEnderecos.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaTipoEnderecos = salvarAcumuladoDB.listaObjetoGenericoOrdem("TipoEndereco");
//            for (int i = 0; i < tipoEnderecos.size(); i++) {
//                listaTipoEnderecos.add(
//                        new SelectItem(
//                        new Integer(i),
//                        tipoEnderecos.get(i).getDescricao(),
//                        Integer.toString(tipoEnderecos.get(i).getId())));
//            }
        }
        return listaTipoEnderecos;
    }

    public void setListaTipoEnderecos(List<TipoEndereco> listaTipoEnderecos) {
        this.listaTipoEnderecos = listaTipoEnderecos;
    }

    public List<TipoTelefone> getListaTipoTelefones() {
        if (listaTipoTelefones.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaTipoTelefones = salvarAcumuladoDB.listaObjetoGenericoOrdem("TipoTelefone");
//            for (int i = 0; i < tipoTelefones.size(); i++) {
//                listaTipoTelefones.add(
//                        new SelectItem(
//                        new Integer(i),
//                        tipoTelefones.get(i).getDescricao(),
//                        Integer.toString(tipoTelefones.get(i).getId())));
//            }
        }
        return listaTipoTelefones;
    }

    public void setListaTipoTelefones(List<TipoTelefone> listaTipoTelefones) {
        this.listaTipoTelefones = listaTipoTelefones;
    }

    public List<GrupoAgenda> getListaGrupoAgendas() {
        if (listaGrupoAgendas.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaGrupoAgendas = salvarAcumuladoDB.listaObjetoGenericoOrdem("GrupoAgenda");
//            for (int i = 0; i < grupoAgendas.size(); i++) {
//                listaGrupoAgendas.add(
//                        new SelectItem(
//                        new Integer(i),
//                        grupoAgendas.get(i).getDescricao(),
//                        Integer.toString(grupoAgendas.get(i).getId())));
//            }
        }
        return listaGrupoAgendas;
    }

    public void setListaGrupoAgendas(List<GrupoAgenda> listaGrupoAgendas) {
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

    public List getListaAgendas() {
        AgendaTelefoneDB agendaDB = new AgendaTelefoneDBToplink();
        listaAgendas.clear();
        DataObject dtObj;
        if (listaAgendas.isEmpty()) {
            int nrGrupoAgenda = 0;
            if (filtraPorGrupo) {
                // nrGrupoAgenda = Integer.parseInt(getListaGrupoAgendas().get(idFiltroGrupoAgenda).getDescription());
                nrGrupoAgenda = idFiltroGrupoAgenda;
            }
            descricaoDDD = "";
            if (!listaDDD.isEmpty()) {
                descricaoDDD = getListaDDD().get(idDDD).getDescription();
                if (descricaoDDD.equals("DDD")) {
                    descricaoDDD = "";
                }
            }
            List<Agenda> listAgenda = agendaDB.pesquisaAgenda(descricaoDDD, descricaoPesquisa, porPesquisa, comoPesquisa, nrGrupoAgenda);
            for (int i = 0; i < listAgenda.size(); i++) {
                String enderecoString = "";
                if (listAgenda.get(i).getEndereco() != null) {
                    enderecoString = listAgenda.get(i).getEndereco().getCidade().getCidade() + " / " + listAgenda.get(i).getEndereco().getCidade().getUf();
                }
                String pessoaString = "";
                if (listAgenda.get(i).getPessoa() != null) {
                    pessoaString = " - " + listAgenda.get(i).getPessoa().getNome();
                }

                dtObj = new DataObject(
                        i, // ARGUMENTO 0 - Indice
                        listAgenda.get(i).getId(), // ARGUMENTO 1 - Id
                        listAgenda.get(i).getGrupoAgenda().getDescricao(), // ARGUMENTO 2 - Grupo Agenda
                        listAgenda.get(i).getNome(), // ARGUMENTO 3 - Nome
                        enderecoString, // ARGUMENTO 4 - Cidade / Estado
                        pessoaString // ARGUMENTO 5 - Pessoa
                        );

                listaAgendas.add(dtObj);
            }
        }
        return listaAgendas;
    }

    public List getListaAgendaTelefone() {
        AgendaTelefoneDB agendaDB = new AgendaTelefoneDBToplink();
        listaAgendaTelefone.clear();
        DataObject dtObj;
        if (listaAgendaTelefone.isEmpty()) {
            int nrGrupoAgenda = 0;
            if (filtraPorGrupo) {
                //nrGrupoAgenda = Integer.parseInt(getListaGrupoAgendas().get(idFiltroGrupoAgenda).getDescription());
                nrGrupoAgenda = idFiltroGrupoAgenda;
            }
            descricaoDDD = "";
            if (!listaDDD.isEmpty()) {
                descricaoDDD = getListaDDD().get(idDDD).getDescription();
                if (descricaoDDD.equals("DDD")) {
                    descricaoDDD = "";
                }
            }
            List<AgendaTelefone> listAgendaTelefones = agendaDB.pesquisaAgendaTelefone(descricaoDDD, descricaoPesquisa, porPesquisa, comoPesquisa, nrGrupoAgenda);
            for (int i = 0; i < listAgendaTelefones.size(); i++) {
                String enderecoString = "";
                String enderecoCompletoString = "";
                if (listAgendaTelefones.get(i).getAgenda().getEndereco() != null) {
                    enderecoString = listAgendaTelefones.get(i).getAgenda().getEndereco().getCidade().getCidade() + " / " + listAgendaTelefones.get(i).getAgenda().getEndereco().getCidade().getUf();
                }
                String pessoaString = "";
                if (listAgendaTelefones.get(i).getAgenda().getPessoa() != null) {
                    pessoaString = " - " + listAgendaTelefones.get(i).getAgenda().getPessoa().getNome();
                }
                if (listAgendaTelefones.get(i).getAgenda().getEndereco() != null) {
                    enderecoCompletoString = listAgendaTelefones.get(i).getAgenda().getEndereco().getEnderecoSimplesToString() + ", " + listAgendaTelefones.get(i).getAgenda().getNumero();
                }
                dtObj = new DataObject(
                        i, // ARGUMENTO 0 - Indice
                        listAgendaTelefones.get(i).getId(), // ARGUMENTO 1 - Id
                        listAgendaTelefones.get(i).getAgenda().getGrupoAgenda().getDescricao(), // ARGUMENTO 2 - Grupo Agenda
                        listAgendaTelefones.get(i).getAgenda().getNome(), // ARGUMENTO 3 - Nome
                        enderecoString, // ARGUMENTO 4 - Cidade / Estado
                        pessoaString, // ARGUMENTO 5 - Pessoa
                        listAgendaTelefones.get(i).getTipoTelefone().getDescricao(), // ARGUMENTO 6 - Tipo Telefone
                        " + " + listAgendaTelefones.get(i).getDdi() + " (" + listAgendaTelefones.get(i).getDdd() + ") " + listAgendaTelefones.get(i).getTelefone(), // ARGUMENTO 7 - Telefone
                        listAgendaTelefones.get(i).getContato(), // ARGUMENTO 8 - Contato
                        enderecoCompletoString // ARGUMENTO 9 - Endereço Completo
                        );
                listaAgendaTelefone.add(dtObj);
            }
        }
        return listaAgendaTelefone;
    }

    public void setListaAgendas(List<Agenda> listaAgendas) {
        this.listaAgendas = listaAgendas;
    }

    public int getIdIndexAgendaTelefone() {
        return idIndexAgendaTelefone;
    }

    public void setIdIndexAgendaTelefone(int idIndexAgendaTelefone) {
        this.idIndexAgendaTelefone = idIndexAgendaTelefone;
    }

    public int getIdIndexAgenda() {
        return idIndexAgenda;
    }

    public void setIdIndexAgenda(int idIndexAgenda) {
        this.idIndexAgenda = idIndexAgenda;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
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

    public String editar(int index) {
        agenda = new Agenda();
        pessoa = new Pessoa();
        endereco = new Endereco();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        agenda = (Agenda) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(((DataObject) listaAgendas.get(index)).getArgumento1().toString()), "Agenda");
        idGrupoAgenda = agenda.getGrupoAgenda().getId();
        idTipoEndereco = agenda.getTipoEndereco().getId();
        if (agenda.getEndereco() != null) {
            endereco = agenda.getEndereco();
        }
        if (agenda.getPessoa() != null) {
            pessoa = agenda.getPessoa();
        }
        listaAgendaTelefones.clear();
        getListaAgendaTelefones();
        return "agendaTelefone";
    }

    public String visualizar(int index) {
        agendaTelefone = new AgendaTelefone();
        pessoa = new Pessoa();
        endereco = new Endereco();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        agendaTelefone = (AgendaTelefone) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(((DataObject) listaAgendaTelefone.get(index)).getArgumento1().toString()), "AgendaTelefone");
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
        return null;
    }

    public int getIdTipoTelefone() {
        return idTipoTelefone;
    }

    public void setIdTipoTelefone(int idTipoTelefone) {
        this.idTipoTelefone = idTipoTelefone;
    }

    public String adicionarAgendaTelefone() {
        msgAgendaTelefone = "";
        if (agendaTelefone.getTelefone().equals("")) {
            msgConfirma = "Informar o número de telefone!";
            return "agenda";
        }
        if (listaTipoTelefones.isEmpty()) {
            msgConfirma = "Informar o tipo de telefone!";
            return "agenda";
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        // agendaTelefone.setTipoTelefone((TipoTelefone) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaTipoTelefones.get(idTipoTelefone).getDescription()), "TipoTelefone"));
        agendaTelefone.setTipoTelefone((TipoTelefone) salvarAcumuladoDB.pesquisaCodigo(idTipoTelefone, "TipoTelefone"));
        if (agenda.getId() != -1) {
            if (agendaTelefone.getId() == -1) {
                AgendaTelefoneDB agendaDB = new AgendaTelefoneDBToplink();
                agendaTelefone.setAgenda(agenda);
                if (((AgendaTelefone) agendaDB.agendaTelefoneExiste(agendaTelefone)).getId() != -1) {
                    msgConfirma = "Telefone já existe!";
                    return "agenda";
                }
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.inserirObjeto(agendaTelefone)) {
                    msgAgendaTelefone = "Telefone adicionado com sucesso";
                    listaAgendaTelefones.clear();
                    salvarAcumuladoDB.comitarTransacao();
                } else {
                    msgAgendaTelefone = "Erro ao adicionar telefone!";
                    salvarAcumuladoDB.desfazerTransacao();
                }
            } else {
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.alterarObjeto(agendaTelefone)) {
                    msgAgendaTelefone = "Telefone atualizado com sucesso";
                    listaAgendaTelefones.clear();
                    salvarAcumuladoDB.comitarTransacao();
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                    msgAgendaTelefone = "Erro ao atualizar telefone!";
                }
            }
        }
        agendaTelefone = new AgendaTelefone();
        return "agendaTelefone";
    }

    public String excluirAgendaTelefone(int index) {
        msgAgendaTelefone = "";
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        AgendaTelefone at = (AgendaTelefone) salvarAcumuladoDB.pesquisaCodigo(listaAgendaTelefones.get(index).getId(), "AgendaTelefone");
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.deletarObjeto(at)) {
            salvarAcumuladoDB.comitarTransacao();
            msgAgendaTelefone = "Telefone excluído com sucesso";
            listaAgendaTelefones.clear();
        } else {
            salvarAcumuladoDB.comitarTransacao();
            msgAgendaTelefone = "Falha ao excluír esse telefone!";
        }
        return "agenda";
    }

    public String editarAgendaTelefone(int index) {
        agendaTelefone = listaAgendaTelefones.get(index);
        return "agendaTelefone";
    }

    public int getIdGrupoAgenda() {
        return idGrupoAgenda;
    }

    public void setIdGrupoAgenda(int idGrupoAgenda) {
        this.idGrupoAgenda = idGrupoAgenda;
    }

    public String getMsgAgendaTelefone() {
        return msgAgendaTelefone;
    }

    public void setMsgAgendaTelefone(String msgAgendaTelefone) {
        this.msgAgendaTelefone = msgAgendaTelefone;
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
            listaDDD.add(new SelectItem(new Integer(i), "DDD", ""));
            for (i = 0; i < list.size(); i++) {
                listaDDD.add(
                        new SelectItem(
                        new Integer(i + 1),
                        ((List) list.get(i)).get(0).toString(),
                        ((List) list.get(i)).get(0).toString()));
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
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipoAgendaTelefone") != null) {
            tipoAgenda = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipoAgendaTelefone");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("tipoAgendaTelefone");
        }
        return tipoAgenda;
    }

    public void setTipoAgenda(String tipoAgenda) {
        this.tipoAgenda = tipoAgenda;
    }
}