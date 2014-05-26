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
import br.com.rtools.pessoa.TipoCentroComercial;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.seguranca.*;
import br.com.rtools.seguranca.db.RotinaDB;
import br.com.rtools.seguranca.db.RotinaDBToplink;
import br.com.rtools.sistema.Cor;
import br.com.rtools.suporte.ProStatus;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

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

    public SimplesBean() {
        rotina = new Rotina();
        idRotina = 0;
        listaRotinaCombo = new ArrayList<SelectItem>();
        listaRotina = new ArrayList<Rotina>();
        nomeRotina = "";
        pesquisaLista = "";
        mensagem = "";
        descricao = "";
        sessoes = null;
        lista = new ArrayList();
        objeto = null;
        id = -1;
    }

    public List<SelectItem> getListaRotinaCombo() {
        int i = 0;
        RotinaDB db = new RotinaDBToplink();
        if (listaRotinaCombo.isEmpty()) {
            listaRotina = db.pesquisaTodosSimples();
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
                    lista.clear();
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
                    lista.clear();
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

    public String editar(Object o) {
        objeto = o;
        editaObjeto(objeto);
        GenericaSessao.put("linkClicado", true);
        if (GenericaSessao.exists("urlRetorno") && !((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno")).substring(0, 4).equals("menu")) {
            GenericaSessao.put("simplesPesquisa", objeto);
            return (String) GenericaSessao.getString("urlRetorno");
        }
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
            lista.clear();
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
//        for (int i = 0; i < getListImports().size(); i++) {
//           try {
//                String o = getListImports().get(i).toString()+"."+sessoes[0];
//                Class cls = Class.forName(o);
//                Class partypes[] = new Class[2];
//                Method m[] = cls.getDeclaredMethods();
//                partypes[0] = (Class) m[0].getGenericReturnType();
//                partypes[1] = (Class) m[2].getGenericReturnType();
//                Constructor ct = cls.getConstructor(partypes);
//                Object arglist[] = new Object[2];
//                arglist[0] = -1;
//                arglist[1] = descricao;
//                objeto = ct.newInstance(arglist);
//                if (objeto != null) {
//                    break;
//                }
//            } catch (Exception e) {
//                 return null;
//            }
//        }

        if (tipo.equals("Bairro")) {
            objeto = (Bairro) new Bairro(id, descricao);
        } else if (tipo.equals("Logradouro")) {
            objeto = (Logradouro) new Logradouro(id, descricao);
        } else if (tipo.equals("GrupoCidade")) {
            objeto = (GrupoCidade) new GrupoCidade(id, descricao);
        } else if (tipo.equals("DescricaoEndereco")) {
            objeto = (DescricaoEndereco) new DescricaoEndereco(id, descricao);
        } else if (tipo.equals("TipoEndereco")) {
            objeto = (TipoEndereco) new TipoEndereco(id, descricao);
        } else if (tipo.equals("TipoDocumento")) {
            objeto = (TipoDocumento) new TipoDocumento(id, descricao);
        } else if (tipo.equals("GrupoAgenda")) {
            objeto = (GrupoAgenda) new GrupoAgenda(id, descricao);
        } else if (tipo.equals("Evento")) {
            objeto = (Evento) new Evento(id, descricao);
        } else if (tipo.equals("Modulo")) {
            objeto = (Modulo) new Modulo(id, descricao);
        } else if (tipo.equals("Departamento")) {
            objeto = (Departamento) new Departamento(id, descricao);
        } else if (tipo.equals("Genero")) {
            objeto = (Genero) new Genero(id, descricao);
        } else if (tipo.equals("Indice")) {
            objeto = (Indice) new Indice(id, descricao);
        } else if (tipo.equals("TipoCentroComercial")) {
            objeto = (TipoCentroComercial) new TipoCentroComercial(id, descricao);
        } else if (tipo.equals("GrupoConvenio")) {
            objeto = (GrupoConvenio) new GrupoConvenio(id, descricao);
        } else if (tipo.equals("ComponenteCurricular")) {
            objeto = (ComponenteCurricular) new ComponenteCurricular(id, descricao);
        } else if (tipo.equals("GrupoEvento")) {
            objeto = (GrupoEvento) new GrupoEvento(id, descricao);
        } else if (tipo.equals("Banda")) {
            objeto = (Banda) new Banda(id, descricao);
        } else if (tipo.equals("Midia")) {
            objeto = (Midia) new Midia(id, descricao);
        } else if (tipo.equals("Nivel")) {
            objeto = (Nivel) new Nivel(id, descricao);
        } else if (tipo.equals("MotivoInativacao")) {
            objeto = (MotivoInativacao) new MotivoInativacao(id, descricao);
        } else if (tipo.equals("TipoServico")) {
            objeto = (TipoServico) new TipoServico(id, descricao);
        } else if (tipo.equals("AteOperacao")) {
            objeto = (AteOperacao) new AteOperacao(id, descricao);
        } else if (tipo.equals("ConviteMotivoSuspencao")) {
            objeto = (ConviteMotivoSuspencao) new ConviteMotivoSuspencao(id, descricao);
        } else if (tipo.equals("ProdutoUnidade")) {
            objeto = (ProdutoUnidade) new ProdutoUnidade(id, descricao);
        } else if (tipo.equals("ProdutoGrupo")) {
            objeto = (ProdutoGrupo) new ProdutoGrupo(id, descricao);
        } else if (tipo.equals("Cor")) {
            objeto = (Cor) new Cor(id, descricao);
        }
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
        }
        return false;
    }

    public void limpaLista() {
        // lista.clear();
    }

    public synchronized List getLista() {
        if (sessoes != null) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            if (!pesquisaLista.isEmpty()) {
                lista = sv.pesquisaObjetoPorDescricao(sessoes[0], pesquisaLista, "p");
            }
        }
        return lista;
    }

    public void setLista(List lista) {
        this.lista = lista;
    }

    public String getPesquisaLista() {
        return pesquisaLista;
    }

    public void setPesquisaLista(String pesquisaLista) {
        this.pesquisaLista = pesquisaLista;
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
