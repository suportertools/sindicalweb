package br.com.rtools.seguranca.beans;

import br.com.rtools.agenda.GrupoAgenda;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.MotivoInativacao;
import br.com.rtools.associativo.Banda;
import br.com.rtools.associativo.ConviteMotivoSuspencao;
import br.com.rtools.associativo.GrupoConvenio;
import br.com.rtools.associativo.GrupoEvento;
import br.com.rtools.associativo.Midia;
import br.com.rtools.atendimento.AteOperacao;
import br.com.rtools.endereco.Bairro;
import br.com.rtools.endereco.DescricaoEndereco;
import br.com.rtools.endereco.Logradouro;
import br.com.rtools.escola.ComponenteCurricular;
import br.com.rtools.estoque.ProdutoGrupo;
import br.com.rtools.estoque.ProdutoUnidade;
import br.com.rtools.financeiro.Indice;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.locadoraFilme.Genero;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.MalaDiretaGrupo;
import br.com.rtools.pessoa.Nacionalidade;
import br.com.rtools.pessoa.TipoCentroComercial;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.dao.MalaDiretaDao;
import br.com.rtools.pessoa.dao.MalaDiretaGrupoDao;
import br.com.rtools.seguranca.*;
import br.com.rtools.sistema.Cor;
import br.com.rtools.suporte.ProStatus;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.Tables;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.persistence.Query;

@ManagedBean
@SessionScoped
public class SimplesBean implements Serializable {

    private Rotina rotina;
    private Object objeto;
    private List<SelectItem> listaRotinaCombo;
    private List listImports = new ArrayList();
    private List<Rotina> listaRotina;
    private List lista;
    private String nomeRotina;
    private Boolean ativo;
    private String pesquisaLista;
    private String mensagem;
    private String descricao;
    private String[] sessoes;
    private int id;
    private int idRotina;
    private boolean linkRetorno;

    public SimplesBean() {
        rotina = new Rotina();
        idRotina = 0;
        listaRotinaCombo = new ArrayList<>();
        listaRotina = new ArrayList<>();
        nomeRotina = "";
        pesquisaLista = "";
        mensagem = "";
        descricao = "";
        sessoes = null;
        lista = new ArrayList();
        objeto = null;
        id = -1;
        linkRetorno = false;
        ativo = null;
    }

    public List<SelectItem> getListaRotinaCombo() {
        int i = 0;
        if (listaRotinaCombo.isEmpty()) {
            listaRotina = new Dao().list(new Rotina(), true);
            while (i < getListaRotina().size()) {
                listaRotinaCombo.add(new SelectItem(i, getListaRotina().get(i).getRotina()));
                i++;
            }
        }
        return listaRotinaCombo;
    }

    public void salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        DaoInterface di = new Dao();
        NovoLog log = new NovoLog();
        log.setCadastroSimples(true);
        if (sessoes != null) {
            if (descricao.equals("")) {
                mensagem = "Informar descrição!";
                GenericaMensagem.warn("Erro", mensagem);
                return;
            }
            if (id == -1) {
                converteObjeto(sessoes[0]);
                if (sv.descricaoExiste(descricao, "descricao", objeto.getClass().getSimpleName())) {
                    mensagem = "Descrição já existe " + nomeRotina + " !";
                    GenericaMensagem.info("Sucesso", mensagem);
                    return;

                }
                if (di.save(objeto, true)) {
                    editaObjeto(objeto);
                    log.save("ID: " + id + " - DESCRICAO: " + descricao);
                    mensagem = "Registro salvo com sucesso";
                    GenericaMensagem.info("Sucesso", mensagem);
                    descricao = "";
                    objeto = null;
                    limpaLista();
                    id = -1;
                } else {
                    mensagem = "Erro ao salvar " + nomeRotina + " ";
                    GenericaMensagem.warn("Erro", mensagem);
                }
            } else {
                Object o = di.find(objeto);
                atualizaObjeto(sessoes[0]);
                if (di.update(objeto, true)) {
                    log.update(o.toString(), "ID: " + id + " - DESCRICAO: " + descricao);
                    mensagem = "Registro atualizado com sucesso";
                    GenericaMensagem.info("Sucesso", mensagem);
                    limpaLista();
                    // descricao = "";
                    // objeto = null;
                    // id = -1;
                } else {
                    mensagem = "Erro ao atualizar " + nomeRotina + " ";
                    GenericaMensagem.warn("Erro", mensagem);
                }
            }
        } else {
            mensagem = "Não há tipo de cadastro definido!";
        }
    }

    public String selecionar(Object o) {
        objeto = o;
        editaObjeto(objeto);
        GenericaSessao.put("linkClicado", true);
        if (GenericaSessao.exists("urlRetorno") && !((String) GenericaSessao.getObject("urlRetorno")).substring(0, 4).equals("menu")) {
            GenericaSessao.put("simplesPesquisa", objeto);
            return (String) GenericaSessao.getString("urlRetorno");
        }
        return null;
    }

    public String editar(Object o) {
        objeto = o;
        editaObjeto(objeto);
        return null;
    }

    public void excluir(Object o) {
        DaoInterface di = new Dao();
        NovoLog log = new NovoLog();
        log.setCadastroSimples(true);
        objeto = o;
        editaObjeto(objeto);
        if (!di.delete(objeto, true)) {
            mensagem = "Erro ao excluir registro";
            GenericaMensagem.warn("Erro", mensagem);
        } else {
            log.delete("ID: " + id + " - DESCRICAO: " + descricao);
            mensagem = "Registro excluído com sucesso!";
            GenericaMensagem.info("Sucesso", mensagem);
            limpaLista();
            objeto = null;
            id = -1;
            descricao = "";
        }
    }

    public void novo() {
        rotina = new Rotina();
        mensagem = "";
        id = -1;
        objeto = null;
        descricao = "";
        ativo = null;
    }

    public String limpar() {
        rotina = new Rotina();
        mensagem = "";
        objeto = null;
        descricao = "";
        return "simples";
    }

    public void setListaRotinaCombo(List<SelectItem> listaRotinaCombo) {
        this.listaRotinaCombo = listaRotinaCombo;
    }

    public List<Rotina> getListaRotina() {
        return listaRotina;
    }

    public void setListaRotina(List<Rotina> listaRotina) {
        this.listaRotina = listaRotina;
    }

    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeRotina() {
        if (sessoes != null) {
            nomeRotina = sessoes[1];
        }
        return nomeRotina;
    }

    public void setNomeRotina(String nomeRotina) {
        this.nomeRotina = nomeRotina;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String[] getSessoes() {
        if (GenericaSessao.exists("cadastroSimples")) {
            sessoes = (String[]) GenericaSessao.getStringVector("cadastroSimples");
            if (GenericaSessao.exists("chamadaPaginaSimples")) {
                GenericaSessao.remove("chamadaPaginaSimples");
            }
            GenericaSessao.put("chamadaPaginaSimples", sessoes);
            GenericaSessao.remove("cadastroSimples");
            habilitaAtivo();
        }
        return sessoes;
    }

    public void setSessoes(String[] sessoes) {
        this.sessoes = sessoes;
    }

    public void converteObjeto(String tipo) {
        objeto = convertToObject(tipo);
    }

    public Object convertToObject(String tipo) {
        Object o = null;
        switch (tipo) {
            case "Bairro":
                o = (Bairro) new Bairro(id, descricao, true);
                break;
            case "Logradouro":
                o = (Logradouro) new Logradouro(id, descricao);
                break;
            case "GrupoCidade":
                o = (GrupoCidade) new GrupoCidade(id, descricao);
                break;
            case "DescricaoEndereco":
                o = (DescricaoEndereco) new DescricaoEndereco(id, descricao, true);
                break;
            case "TipoEndereco":
                o = (TipoEndereco) new TipoEndereco(id, descricao);
                break;
            case "TipoDocumento":
                o = (TipoDocumento) new TipoDocumento(id, descricao);
                break;
            case "GrupoAgenda":
                o = (GrupoAgenda) new GrupoAgenda(id, descricao);
                break;
            case "Evento":
                o = (Evento) new Evento(id, descricao);
                break;
            case "Modulo":
                o = (Modulo) new Modulo(id, descricao);
                break;
            case "Departamento":
                o = (Departamento) new Departamento(id, descricao);
                break;
            case "Genero":
                o = (Genero) new Genero(id, descricao);
                break;
            case "Indice":
                o = (Indice) new Indice(id, descricao);
                break;
            case "TipoCentroComercial":
                o = (TipoCentroComercial) new TipoCentroComercial(id, descricao);
                break;
            case "GrupoConvenio":
                o = (GrupoConvenio) new GrupoConvenio(id, descricao);
                break;
            case "ComponenteCurricular":
                o = (ComponenteCurricular) new ComponenteCurricular(id, descricao);
                break;
            case "GrupoEvento":
                o = (GrupoEvento) new GrupoEvento(id, descricao);
                break;
            case "Banda":
                o = (Banda) new Banda(id, descricao);
                break;
            case "Midia":
                o = (Midia) new Midia(id, descricao);
                break;
            case "Nivel":
                o = (Nivel) new Nivel(id, descricao);
                break;
            case "MotivoInativacao":
                o = (MotivoInativacao) new MotivoInativacao(id, descricao);
                break;
            case "TipoServico":
                o = (TipoServico) new TipoServico(id, descricao);
                break;
            case "AteOperacao":
                o = (AteOperacao) new AteOperacao(id, descricao);
                break;
            case "ConviteMotivoSuspencao":
                o = (ConviteMotivoSuspencao) new ConviteMotivoSuspencao(id, descricao);
                break;
            case "ProdutoUnidade":
                o = (ProdutoUnidade) new ProdutoUnidade(id, descricao);
                break;
            case "ProdutoGrupo":
                o = (ProdutoGrupo) new ProdutoGrupo(id, descricao);
                break;
            case "Nacionalidade":
                o = (Nacionalidade) new Nacionalidade(id, descricao);
                break;
            case "Cor":
                o = (Cor) new Cor(id, descricao);
                break;
            case "MalaDiretaGrupo":
                o = (MalaDiretaGrupo) new MalaDiretaGrupo(id, descricao, true);
                break;
        }
        return o;
    }

    public void atualizaObjeto(String tipo) {
        switch (tipo) {
            case "Bairro":
                ((Bairro) objeto).setDescricao(descricao);
                break;
            case "Logradouro":
                ((Logradouro) objeto).setDescricao(descricao);
                break;
            case "GrupoCidade":
                ((GrupoCidade) objeto).setDescricao(descricao);
                break;
            case "DescricaoEndereco":
                ((DescricaoEndereco) objeto).setDescricao(descricao);
                break;
            case "TipoEndereco":
                ((TipoEndereco) objeto).setDescricao(descricao);
                break;
            case "TipoDocumento":
                ((TipoDocumento) objeto).setDescricao(descricao);
                break;
            case "GrupoAgenda":
                ((GrupoAgenda) objeto).setDescricao(descricao);
                break;
            case "Evento":
                ((Evento) objeto).setDescricao(descricao);
                break;
            case "Modulo":
                ((Modulo) objeto).setDescricao(descricao);
                break;
            case "Departamento":
                ((Departamento) objeto).setDescricao(descricao);
                break;
            case "Genero":
                ((Genero) objeto).setDescricao(descricao);
                break;
            case "Indice":
                ((Indice) objeto).setDescricao(descricao);
                break;
            case "TipoCentroComercial":
                ((TipoCentroComercial) objeto).setDescricao(descricao);
                break;
            case "GrupoConvenio":
                ((GrupoConvenio) objeto).setDescricao(descricao);
                break;
            case "ComponenteCurricular":
                ((ComponenteCurricular) objeto).setDescricao(descricao);
                break;
            case "GrupoEvento":
                ((GrupoEvento) objeto).setDescricao(descricao);
                break;
            case "Banda":
                ((Banda) objeto).setDescricao(descricao);
                break;
            case "Midia":
                ((Midia) objeto).setDescricao(descricao);
                break;
            case "Nivel":
                ((Nivel) objeto).setDescricao(descricao);
                break;
            case "MotivoInativacao":
                ((MotivoInativacao) objeto).setDescricao(descricao);
                break;
            case "TipoServico":
                ((TipoServico) objeto).setDescricao(descricao);
                break;
            case "AteOperacao":
                ((AteOperacao) objeto).setDescricao(descricao);
                break;
            case "ConviteMotivoSuspencao":
                ((ConviteMotivoSuspencao) objeto).setDescricao(descricao);
                break;
            case "ProStatus":
                ((ProStatus) objeto).setDescricao(descricao);
                break;
            case "ProdutoUnidade":
                ((ProdutoUnidade) objeto).setDescricao(descricao);
                break;
            case "ProdutoGrupo":
                ((ProdutoGrupo) objeto).setDescricao(descricao);
                break;
            case "Cor":
                ((Cor) objeto).setDescricao(descricao);
                break;
            case "Nacionalidade":
                ((Nacionalidade) objeto).setDescricao(descricao);
                break;
            case "MalaDiretaGrupo":
                ((MalaDiretaGrupo) objeto).setDescricao(descricao);
                break;
        }
    }

    public void editaObjeto(Object obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Bairro":
                descricao = ((Bairro) obj).getDescricao();
                id = ((Bairro) objeto).getId();
                break;
            case "Logradouro":
                descricao = ((Logradouro) obj).getDescricao();
                id = ((Logradouro) objeto).getId();
                break;
            case "GrupoCidade":
                descricao = ((GrupoCidade) obj).getDescricao();
                id = ((GrupoCidade) objeto).getId();
                break;
            case "DescricaoEndereco":
                descricao = ((DescricaoEndereco) obj).getDescricao();
                id = ((DescricaoEndereco) objeto).getId();
                break;
            case "TipoEndereco":
                descricao = ((TipoEndereco) obj).getDescricao();
                id = ((TipoEndereco) objeto).getId();
                break;
            case "TipoDocumento":
                descricao = ((TipoDocumento) obj).getDescricao();
                id = ((TipoDocumento) objeto).getId();
                break;
            case "GrupoAgenda":
                descricao = ((GrupoAgenda) obj).getDescricao();
                id = ((GrupoAgenda) objeto).getId();
                break;
            case "Evento":
                descricao = ((Evento) obj).getDescricao();
                id = ((Evento) objeto).getId();
                break;
            case "Modulo":
                descricao = ((Modulo) obj).getDescricao();
                id = ((Modulo) objeto).getId();
                break;
            case "Departamento":
                descricao = ((Departamento) obj).getDescricao();
                id = ((Departamento) objeto).getId();
                break;
            case "Genero":
                descricao = ((Genero) obj).getDescricao();
                id = ((Genero) objeto).getId();
                break;
            case "Indice":
                descricao = ((Indice) obj).getDescricao();
                id = ((Indice) objeto).getId();
                break;
            case "TipoCentroComercial":
                descricao = ((TipoCentroComercial) obj).getDescricao();
                id = ((TipoCentroComercial) objeto).getId();
                break;
            case "GrupoConvenio":
                descricao = ((GrupoConvenio) obj).getDescricao();
                id = ((GrupoConvenio) objeto).getId();
                break;
            case "ComponenteCurricular":
                descricao = ((ComponenteCurricular) obj).getDescricao();
                id = ((ComponenteCurricular) objeto).getId();
                break;
            case "GrupoEvento":
                descricao = ((GrupoEvento) obj).getDescricao();
                id = ((GrupoEvento) objeto).getId();
                break;
            case "Banda":
                descricao = ((Banda) obj).getDescricao();
                id = ((Banda) objeto).getId();
                break;
            case "Midia":
                descricao = ((Midia) obj).getDescricao();
                id = ((Midia) objeto).getId();
                break;
            case "Nivel":
                descricao = ((Nivel) obj).getDescricao();
                id = ((Nivel) objeto).getId();
                break;
            case "MotivoInativacao":
                descricao = ((MotivoInativacao) obj).getDescricao();
                id = ((MotivoInativacao) objeto).getId();
                break;
            case "TipoServico":
                descricao = ((TipoServico) obj).getDescricao();
                id = ((TipoServico) objeto).getId();
                break;
            case "AteOperacao":
                descricao = ((AteOperacao) obj).getDescricao();
                id = ((AteOperacao) objeto).getId();
                break;
            case "ConviteMotivoSuspencao":
                descricao = ((ConviteMotivoSuspencao) obj).getDescricao();
                id = ((ConviteMotivoSuspencao) objeto).getId();
                break;
            case "ProStatus":
                descricao = ((ProStatus) obj).getDescricao();
                id = ((ProStatus) objeto).getId();
                break;
            case "ProdutoUnidade":
                descricao = ((ProdutoUnidade) obj).getDescricao();
                id = ((ProdutoUnidade) objeto).getId();
                break;
            case "ProdutoGrupo":
                descricao = ((ProdutoGrupo) obj).getDescricao();
                id = ((ProdutoGrupo) objeto).getId();
                break;
            case "Cor":
                descricao = ((Cor) obj).getDescricao();
                id = ((Cor) objeto).getId();
                break;
            case "Nacionalidade":
                descricao = ((Nacionalidade) obj).getDescricao();
                id = ((Nacionalidade) objeto).getId();
                break;
            case "MalaDiretaGrupo":
                descricao = ((MalaDiretaGrupo) obj).getDescricao();
                id = ((MalaDiretaGrupo) objeto).getId();
                ativo = ((MalaDiretaGrupo) objeto).getAtivo();
                break;
        }
        Dao dao = new Dao();
        objeto = dao.rebind(objeto);
    }

    public boolean comparaObjeto(Object obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Bairro":
                if (((Bairro) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "Logradouro":
                if (((Logradouro) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "GrupoCidade":
                if (((GrupoCidade) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
            }   break;
            case "DescricaoEndereco":
                if (((DescricaoEndereco) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
            }   break;
            case "TipoEndereco":
                if (((TipoEndereco) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
            }   break;
            case "TipoDocumento":
                if (((TipoDocumento) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
            }   break;
            case "GrupoAgenda":
                if (((GrupoAgenda) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
            }   break;
            case "Evento":
                if (((Evento) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
            }   break;
            case "Modulo":
                if (((Modulo) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
            }   break;
            case "Departamento":
                if (((Departamento) obj).getDescricao().contains(pesquisaLista)) {
                return true;
                }   break;
            case "Genero":
                if (((Genero) obj).getDescricao().contains(pesquisaLista)) {
                return true;
                }   break;
            case "Indice":
                if (((Indice) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }   break;
            case "TipoCentroComercial":
                if (((TipoCentroComercial) obj).getDescricao().contains(pesquisaLista)) {
                return true;
                }   break;
            case "GrupoConvenio":
                if (((GrupoConvenio) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }   break;
            case "ComponenteCurricular":
                if (((ComponenteCurricular) obj).getDescricao().contains(pesquisaLista)) {
                return true;
                }   break;
            case "GrupoEvento":
                if (((GrupoEvento) obj).getDescricao().contains(pesquisaLista)) {
                return true;
                }   break;
            case "Banda":
                if (((Banda) obj).getDescricao().contains(pesquisaLista)) {
                return true;
                }   break;
            case "Midia":
                if (((Midia) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "Nivel":
                if (((Nivel) obj).getDescricao().contains(pesquisaLista)) {
                return true;
                }   break;
            case "MotivoInativacao":
                if (((MotivoInativacao) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "TipoServico":
                if (((TipoServico) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "AteOperacao":
                if (((AteOperacao) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "ConviteMotivoSuspencao":
                if (((ConviteMotivoSuspencao) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "ProStatus":
                if (((ProStatus) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "ProdutoUnidade":
                if (((ProdutoUnidade) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "ProdutoGrupo":
                if (((ProdutoGrupo) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "Cor":
                if (((Cor) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "Nacionalidade":
                if (((Nacionalidade) obj).getDescricao().contains(pesquisaLista)) {
                    return true;
                }   break;
            case "MalaDiretaGrupo":
                if (((MalaDiretaGrupo) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }   break;
        }
        return false;
    }

    public void limpaLista() {
        lista.clear();
        if (sessoes != null) {
            if (!pesquisaLista.isEmpty()) {
                if (lista.isEmpty()) {
                    String desc = AnaliseString.removerAcentos(pesquisaLista.trim().toUpperCase());
                    Object o = (Object) convertToObject(sessoes[0]);
                    String tableName = Tables.name(o);
                    if (o == null) {
                        lista = new ArrayList();
                    }
                    Dao dao = new Dao();
                    int maxResults = 500;
                    if (desc.length() == 1) {
                        maxResults = 100;
                    } else if (desc.length() == 2) {
                        maxResults = 150;
                    } else if (desc.length() == 3) {
                        maxResults = 200;
                    }

                    String queryString = "";

                    if (tableName.equals("end_bairro") || tableName.equals("end_descricao_endereco")) {
                        queryString = " SELECT t.* FROM " + tableName + "  AS t WHERE upper(func_translate(ds_descricao)) LIKE '%" + desc + "%' AND is_ativo = true ORDER BY t.ds_descricao ASC LIMIT " + maxResults;
                    } else {
                        queryString = " SELECT t.* FROM " + tableName + "  AS t WHERE upper(func_translate(ds_descricao)) LIKE '%" + desc + "%'  ORDER BY t.ds_descricao ASC LIMIT " + maxResults;
                    }

                    try {
                        Query query = dao.getEntityManager().createNativeQuery(queryString, o.getClass());
                        lista = query.getResultList();
                    } catch (Exception e) {
                        Logger.getLogger(SimplesBean.class.getName()).log(Level.SEVERE, null, e);
                        lista = new ArrayList();
                    }
                }
            }
        }
    }

//    public synchronized List getLista() throws ClassNotFoundException {
//        if (sessoes != null) {
//            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//            if (!pesquisaLista.isEmpty()) {
//                String table_name = "", string_class = pacoteDaClasse(sessoes[0]);
//
//                if (!string_class.isEmpty()) {
//                    Class cls = Class.forName(string_class);
//
////                    for (Annotation ann : cls.getAnnotations()) {
////                        if(!ann.annotationType().equals(javax.persistence.Table.class)) continue;
////
////                        javax.persistence.Table t = (javax.persistence.Table) ann;
////                        table_name  = t.name();
////                    }
//                    SelectTranslate st = new SelectTranslate();
//                    String value = "%" + pesquisaLista + "%";
//                    try {
//                        return lista = st.select(cls.newInstance()).where("descricao", value).find();
//                    } catch (InstantiationException ex) {
//                        Logger.getLogger(SimplesBean.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (IllegalAccessException ex) {
//                        Logger.getLogger(SimplesBean.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//        }
//        return lista;
//    }
    public String pacoteDaClasse(String classe) {
        List<String> list_class = new ArrayList();

        list_class.add("br.com.rtools.academia");
        list_class.add("br.com.rtools.agenda");
        list_class.add("br.com.rtools.arrecadacao");
        list_class.add("br.com.rtools.associativo");
        list_class.add("br.com.rtools.atendimento");
        list_class.add("br.com.rtools.endereco");
        list_class.add("br.com.rtools.escola");
        list_class.add("br.com.rtools.estoque");
        list_class.add("br.com.rtools.financeiro");
        list_class.add("br.com.rtools.homologacao");
        list_class.add("br.com.rtools.locadoraFilme");
        list_class.add("br.com.rtools.pessoa");
        list_class.add("br.com.rtools.relatorios");
        list_class.add("br.com.rtools.seguranca");
        list_class.add("br.com.rtools.sistema");
        list_class.add("br.com.rtools.suporte");

        for (String list_clas : list_class) {
            try {
                Class.forName(list_clas + "." + classe);
                return list_clas + "." + classe;
            } catch (ClassNotFoundException e) {
                //my class isn't there!
            }
        }
        return "";
    }

    public String getPesquisaLista() {
        return pesquisaLista;
    }

    public void setPesquisaLista(String pesquisaLista) {
        this.pesquisaLista = pesquisaLista;
    }

    public List getLista() {
        return lista;
    }

    public void setLista(List lista) {
        this.lista = lista;
    }

    public boolean isLinkRetorno() {
        linkRetorno = GenericaSessao.exists("urlRetorno") && !((String) GenericaSessao.getString("urlRetorno")).substring(0, 4).equals("menu");
        return linkRetorno;
    }

    public void setLinkRetorno(boolean linkRetorno) {
        this.linkRetorno = linkRetorno;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void habilitaAtivo() {
        if (sessoes[0].equals("MalaDiretaGrupo")) {
            if(ativo == null) {
                ativo = true;                
            }
        }
    }

    public void listener(Object object) {
        if (object.equals("MalaDiretaGrupo")) {
            new MalaDiretaGrupoDao().inative(((MalaDiretaGrupo) object).getId());
        }
    }

}
//                Class cls = Class.forName(sessoes[0]);
//
//                Class partypes[] = new Class[2];
//                Method m[] = cls.getDeclaredMethods();
//                partypes[0] = (Class) m[0].getGenericReturnType();
//                partypes[1] = (Class) m[2].getGenericReturnType();
//
//                Constructor ct = cls.getConstructor(partypes);
//
//                Object arglist[] = new Object[2];
//
//                arglist[0] = -1;
//                arglist[1] = descricao;
//
//                objeto = ct.newInstance(arglist);
