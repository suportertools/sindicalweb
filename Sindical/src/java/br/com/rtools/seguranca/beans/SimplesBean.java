package br.com.rtools.seguranca.beans;

import br.com.rtools.agenda.GrupoAgenda;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.MotivoInativacao;
import br.com.rtools.associativo.Banda;
import br.com.rtools.associativo.GrupoConvenio;
import br.com.rtools.associativo.GrupoEvento;
import br.com.rtools.associativo.Midia;
import br.com.rtools.atendimento.AteOperacao;
import br.com.rtools.endereco.Bairro;
import br.com.rtools.endereco.DescricaoEndereco;
import br.com.rtools.endereco.Logradouro;
import br.com.rtools.escola.ComponenteCurricular;
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
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class SimplesBean {

    private Rotina rotina;
    private Object objeto;
    private List<SelectItem> listaRotinaCombo;
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
                listaRotinaCombo.add(new SelectItem(
                        new Integer(i),
                        getListaRotina().get(i).getRotina()));
                i++;
            }
        }
        return listaRotinaCombo;
    }

    public void salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        NovoLog log = new NovoLog();
        if (sessoes != null) {
            if (descricao.equals("")) {
                mensagem = "Campo não pode ser vázio!";
                return;
            }
            if (id == -1) {
                converteObjeto(sessoes[0]);
                if (sv.descricaoExiste(descricao, "descricao", objeto.getClass().getSimpleName())) {
                    mensagem = "Essa descrição já existe " + nomeRotina + " !";
                    return;

                }
                sv.abrirTransacao();
                if (sv.inserirObjeto(objeto)) {
                    sv.comitarTransacao();
                    log.novo("Registro de " + objeto.getClass().getSimpleName() + " inserido", "ID: " + id + " DESCRICAO: " + descricao);
                    mensagem = "Registro salvo com sucesso";
                    descricao = "";
                    objeto = null;
                    lista.clear();
                    id = -1;
                } else {
                    mensagem = "Erro ao salvar " + nomeRotina + " ";
                    sv.desfazerTransacao();
                }
            } else {
                atualizaObjeto(sessoes[0]);
                sv.abrirTransacao();
                if (sv.alterarObjeto(objeto)) {
                    sv.comitarTransacao();
                    log.novo("Registro de " + objeto.getClass().getSimpleName() + " alterado", "ID: " + id + " DESCRICAO: " + descricao);
                    mensagem = "Registro atualizado com sucesso";
                    descricao = "";
                    lista.clear();
                    objeto = null;
                    id = -1;
                } else {
                    mensagem = "Erro ao atualizar " + nomeRotina + " ";
                    sv.desfazerTransacao();
                }
            }
        } else {
            mensagem = "Não há tipo de cadastro definido!";
        }
    }

    public String editar(Object o) {
        objeto = o;
        editaObjeto(objeto);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("simplesPesquisa", objeto);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") != null
                && !((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno")).substring(0, 4).equals("menu")) {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
        return null;
    }

    public void excluir(Object o) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        NovoLog log = new NovoLog();
        sv.abrirTransacao();
        objeto = o;
        editaObjeto(objeto);
        objeto = sv.pesquisaObjeto(id, objeto.getClass().getSimpleName());
        if (!sv.deletarObjeto(objeto)) {
            sv.desfazerTransacao();
            mensagem = "Erro ao excluir registro";
        } else {
            sv.comitarTransacao();
            log.novo("Registro de " + objeto.getClass().getSimpleName() + " excluido", "ID: " + id + " DESCRICAO: " + descricao);
            mensagem = "Registro excluído com sucesso!";
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
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cadastroSimples") != null) {
            sessoes = (String[]) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cadastroSimples");
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaSimples") != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("chamadaPaginaSimples");
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("chamadaPaginaSimples", sessoes);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cadastroSimples");
        }
        return sessoes;
    }

    public void setSessoes(String[] sessoes) {
        this.sessoes = sessoes;
    }

    public void converteObjeto(String tipo) {
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
        }
        if (tipo.equals("Nivel")) {
            objeto = (Nivel) new Nivel(id, descricao);
        }
        if (tipo.equals("MotivoInativacao")) {
            objeto = (MotivoInativacao) new MotivoInativacao(id, descricao);
        }
        if (tipo.equals("TipoServico")) {
            objeto = (TipoServico) new TipoServico(id, descricao);
        }
        if (tipo.equals("AteOperacao")) {
            objeto = (AteOperacao) new AteOperacao(id, descricao);
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
        }
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