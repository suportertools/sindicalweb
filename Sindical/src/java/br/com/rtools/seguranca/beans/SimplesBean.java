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
import br.com.rtools.pessoa.Nacionalidade;
import br.com.rtools.pessoa.TipoCentroComercial;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.TipoEndereco;
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
        if (tipo.equals("Bairro")) {
            o = (Bairro) new Bairro(id, descricao, true);
        } else if (tipo.equals("Logradouro")) {
            o = (Logradouro) new Logradouro(id, descricao);
        } else if (tipo.equals("GrupoCidade")) {
            o = (GrupoCidade) new GrupoCidade(id, descricao);
        } else if (tipo.equals("DescricaoEndereco")) {
            o = (DescricaoEndereco) new DescricaoEndereco(id, descricao, true);
        } else if (tipo.equals("TipoEndereco")) {
            o = (TipoEndereco) new TipoEndereco(id, descricao);
        } else if (tipo.equals("TipoDocumento")) {
            o = (TipoDocumento) new TipoDocumento(id, descricao);
        } else if (tipo.equals("GrupoAgenda")) {
            o = (GrupoAgenda) new GrupoAgenda(id, descricao);
        } else if (tipo.equals("Evento")) {
            o = (Evento) new Evento(id, descricao);
        } else if (tipo.equals("Modulo")) {
            o = (Modulo) new Modulo(id, descricao);
        } else if (tipo.equals("Departamento")) {
            o = (Departamento) new Departamento(id, descricao);
        } else if (tipo.equals("Genero")) {
            o = (Genero) new Genero(id, descricao);
        } else if (tipo.equals("Indice")) {
            o = (Indice) new Indice(id, descricao);
        } else if (tipo.equals("TipoCentroComercial")) {
            o = (TipoCentroComercial) new TipoCentroComercial(id, descricao);
        } else if (tipo.equals("GrupoConvenio")) {
            o = (GrupoConvenio) new GrupoConvenio(id, descricao);
        } else if (tipo.equals("ComponenteCurricular")) {
            o = (ComponenteCurricular) new ComponenteCurricular(id, descricao);
        } else if (tipo.equals("GrupoEvento")) {
            o = (GrupoEvento) new GrupoEvento(id, descricao);
        } else if (tipo.equals("Banda")) {
            o = (Banda) new Banda(id, descricao);
        } else if (tipo.equals("Midia")) {
            o = (Midia) new Midia(id, descricao);
        } else if (tipo.equals("Nivel")) {
            o = (Nivel) new Nivel(id, descricao);
        } else if (tipo.equals("MotivoInativacao")) {
            o = (MotivoInativacao) new MotivoInativacao(id, descricao);
        } else if (tipo.equals("TipoServico")) {
            o = (TipoServico) new TipoServico(id, descricao);
        } else if (tipo.equals("AteOperacao")) {
            o = (AteOperacao) new AteOperacao(id, descricao);
        } else if (tipo.equals("ConviteMotivoSuspencao")) {
            o = (ConviteMotivoSuspencao) new ConviteMotivoSuspencao(id, descricao);
        } else if (tipo.equals("ProdutoUnidade")) {
            o = (ProdutoUnidade) new ProdutoUnidade(id, descricao);
        } else if (tipo.equals("ProdutoGrupo")) {
            o = (ProdutoGrupo) new ProdutoGrupo(id, descricao);
        } else if (tipo.equals("Nacionalidade")) {
            o = (Nacionalidade) new Nacionalidade(id, descricao);
        } else if (tipo.equals("Cor")) {
            o = (Cor) new Cor(id, descricao);
        }
        return o;
    }

    public void atualizaObjeto(String tipo) {
        if (tipo.equals("Bairro")) {
            ((Bairro) objeto).setDescricao(descricao);
        } else if (tipo.equals("Logradouro")) {
            ((Logradouro) objeto).setDescricao(descricao);
        } else if (tipo.equals("GrupoCidade")) {
            ((GrupoCidade) objeto).setDescricao(descricao);
        } else if (tipo.equals("DescricaoEndereco")) {
            ((DescricaoEndereco) objeto).setDescricao(descricao);
        } else if (tipo.equals("TipoEndereco")) {
            ((TipoEndereco) objeto).setDescricao(descricao);
        } else if (tipo.equals("TipoDocumento")) {
            ((TipoDocumento) objeto).setDescricao(descricao);
        } else if (tipo.equals("GrupoAgenda")) {
            ((GrupoAgenda) objeto).setDescricao(descricao);
        } else if (tipo.equals("Evento")) {
            ((Evento) objeto).setDescricao(descricao);
        } else if (tipo.equals("Modulo")) {
            ((Modulo) objeto).setDescricao(descricao);
        } else if (tipo.equals("Departamento")) {
            ((Departamento) objeto).setDescricao(descricao);
        } else if (tipo.equals("Genero")) {
            ((Genero) objeto).setDescricao(descricao);
        } else if (tipo.equals("Indice")) {
            ((Indice) objeto).setDescricao(descricao);
        } else if (tipo.equals("TipoCentroComercial")) {
            ((TipoCentroComercial) objeto).setDescricao(descricao);
        } else if (tipo.equals("GrupoConvenio")) {
            ((GrupoConvenio) objeto).setDescricao(descricao);
        } else if (tipo.equals("ComponenteCurricular")) {
            ((ComponenteCurricular) objeto).setDescricao(descricao);
        } else if (tipo.equals("GrupoEvento")) {
            ((GrupoEvento) objeto).setDescricao(descricao);
        } else if (tipo.equals("Banda")) {
            ((Banda) objeto).setDescricao(descricao);
        } else if (tipo.equals("Midia")) {
            ((Midia) objeto).setDescricao(descricao);
        } else if (tipo.equals("Nivel")) {
            ((Nivel) objeto).setDescricao(descricao);
        } else if (tipo.equals("MotivoInativacao")) {
            ((MotivoInativacao) objeto).setDescricao(descricao);
        } else if (tipo.equals("TipoServico")) {
            ((TipoServico) objeto).setDescricao(descricao);
        } else if (tipo.equals("AteOperacao")) {
            ((AteOperacao) objeto).setDescricao(descricao);
        } else if (tipo.equals("ConviteMotivoSuspencao")) {
            ((ConviteMotivoSuspencao) objeto).setDescricao(descricao);
        } else if (tipo.equals("ProStatus")) {
            ((ProStatus) objeto).setDescricao(descricao);
        } else if (tipo.equals("ProdutoUnidade")) {
            ((ProdutoUnidade) objeto).setDescricao(descricao);
        } else if (tipo.equals("ProdutoGrupo")) {
            ((ProdutoGrupo) objeto).setDescricao(descricao);
        } else if (tipo.equals("Cor")) {
            ((Cor) objeto).setDescricao(descricao);
        } else if (tipo.equals("Nacionalidade")) {
            ((Nacionalidade) objeto).setDescricao(descricao);
        }
    }

    public void editaObjeto(Object obj) {
        if (obj.getClass().getSimpleName().equals("Bairro")) {
            descricao = ((Bairro) obj).getDescricao();
            id = ((Bairro) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Logradouro")) {
            descricao = ((Logradouro) obj).getDescricao();
            id = ((Logradouro) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("GrupoCidade")) {
            descricao = ((GrupoCidade) obj).getDescricao();
            id = ((GrupoCidade) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("DescricaoEndereco")) {
            descricao = ((DescricaoEndereco) obj).getDescricao();
            id = ((DescricaoEndereco) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("TipoEndereco")) {
            descricao = ((TipoEndereco) obj).getDescricao();
            id = ((TipoEndereco) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("TipoDocumento")) {
            descricao = ((TipoDocumento) obj).getDescricao();
            id = ((TipoDocumento) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("GrupoAgenda")) {
            descricao = ((GrupoAgenda) obj).getDescricao();
            id = ((GrupoAgenda) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Evento")) {
            descricao = ((Evento) obj).getDescricao();
            id = ((Evento) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Modulo")) {
            descricao = ((Modulo) obj).getDescricao();
            id = ((Modulo) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Departamento")) {
            descricao = ((Departamento) obj).getDescricao();
            id = ((Departamento) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Genero")) {
            descricao = ((Genero) obj).getDescricao();
            id = ((Genero) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Indice")) {
            descricao = ((Indice) obj).getDescricao();
            id = ((Indice) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("TipoCentroComercial")) {
            descricao = ((TipoCentroComercial) obj).getDescricao();
            id = ((TipoCentroComercial) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("GrupoConvenio")) {
            descricao = ((GrupoConvenio) obj).getDescricao();
            id = ((GrupoConvenio) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("ComponenteCurricular")) {
            descricao = ((ComponenteCurricular) obj).getDescricao();
            id = ((ComponenteCurricular) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("GrupoEvento")) {
            descricao = ((GrupoEvento) obj).getDescricao();
            id = ((GrupoEvento) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Banda")) {
            descricao = ((Banda) obj).getDescricao();
            id = ((Banda) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Midia")) {
            descricao = ((Midia) obj).getDescricao();
            id = ((Midia) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Nivel")) {
            descricao = ((Nivel) obj).getDescricao();
            id = ((Nivel) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("MotivoInativacao")) {
            descricao = ((MotivoInativacao) obj).getDescricao();
            id = ((MotivoInativacao) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("TipoServico")) {
            descricao = ((TipoServico) obj).getDescricao();
            id = ((TipoServico) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("AteOperacao")) {
            descricao = ((AteOperacao) obj).getDescricao();
            id = ((AteOperacao) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("ConviteMotivoSuspencao")) {
            descricao = ((ConviteMotivoSuspencao) obj).getDescricao();
            id = ((ConviteMotivoSuspencao) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("ProStatus")) {
            descricao = ((ProStatus) obj).getDescricao();
            id = ((ProStatus) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("ProdutoUnidade")) {
            descricao = ((ProdutoUnidade) obj).getDescricao();
            id = ((ProdutoUnidade) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("ProdutoGrupo")) {
            descricao = ((ProdutoGrupo) obj).getDescricao();
            id = ((ProdutoGrupo) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Cor")) {
            descricao = ((Cor) obj).getDescricao();
            id = ((Cor) objeto).getId();
        } else if (obj.getClass().getSimpleName().equals("Nacionalidade")) {
            descricao = ((Nacionalidade) obj).getDescricao();
            id = ((Nacionalidade) objeto).getId();
        }
        Dao dao = new Dao();
        objeto = dao.rebind(objeto);
    }

    public boolean comparaObjeto(Object obj) {
        if (obj.getClass().getSimpleName().equals("Bairro")) {
            if (((Bairro) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Logradouro")) {
            if (((Logradouro) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("GrupoCidade")) {
            if (((GrupoCidade) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("DescricaoEndereco")) {
            if (((DescricaoEndereco) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("TipoEndereco")) {
            if (((TipoEndereco) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("TipoDocumento")) {
            if (((TipoDocumento) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("GrupoAgenda")) {
            if (((GrupoAgenda) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Evento")) {
            if (((Evento) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Modulo")) {
            if (((Modulo) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Departamento")) {
            if (((Departamento) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Genero")) {
            if (((Genero) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Indice")) {
            if (((Indice) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("TipoCentroComercial")) {
            if (((TipoCentroComercial) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("GrupoConvenio")) {
            if (((GrupoConvenio) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("ComponenteCurricular")) {
            if (((ComponenteCurricular) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("GrupoEvento")) {
            if (((GrupoEvento) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Banda")) {
            if (((Banda) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Midia")) {
            if (((Midia) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Nivel")) {
            if (((Nivel) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("MotivoInativacao")) {
            if (((MotivoInativacao) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("TipoServico")) {
            if (((TipoServico) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("AteOperacao")) {
            if (((AteOperacao) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("ConviteMotivoSuspencao")) {
            if (((ConviteMotivoSuspencao) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("ProStatus")) {
            if (((ProStatus) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("ProdutoUnidade")) {
            if (((ProdutoUnidade) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("ProdutoGrupo")) {
            if (((ProdutoGrupo) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Cor")) {
            if (((Cor) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
        } else if (obj.getClass().getSimpleName().equals("Nacionalidade")) {
            if (((Nacionalidade) obj).getDescricao().contains(pesquisaLista)) {
                return true;
            }
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
