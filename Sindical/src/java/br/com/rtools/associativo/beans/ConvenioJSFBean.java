package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Convenio;
import br.com.rtools.associativo.SubGrupoConvenio;
import br.com.rtools.associativo.db.ConvenioDB;
import br.com.rtools.associativo.db.ConvenioDBToplink;
import br.com.rtools.associativo.db.SubGrupoConvenioDB;
import br.com.rtools.associativo.db.SubGrupoConvenioDBToplink;
import br.com.rtools.pessoa.Juridica;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class ConvenioJSFBean {

    private Convenio convenio = new Convenio();
    private Juridica juridica = new Juridica();
    private SubGrupoConvenio subGrupoConvenio = new SubGrupoConvenio();
    private String msgConfirma;
    private int idIndex = -1;
    private int idIndexSub = -1;
    private List<SubGrupoConvenio> listaSubGrupo = new ArrayList();
    private List<Convenio> listaConvenio = new ArrayList();

    public void salvar() {
        ConvenioDB db = new ConvenioDBToplink();
        if (db.pesquisaConvenioPorGrupoPessoa(getJuridica().getPessoa().getId(), subGrupoConvenio.getGrupoConvenio().getId()).isEmpty()) {
            if (getJuridica().getId() != -1) {
                this.convenio.setJuridica(juridica);
                if (subGrupoConvenio.getGrupoConvenio().getId() != -1) {
                    this.convenio.setSubGrupoConvenio(subGrupoConvenio);
                    if (getConvenio().getId() == -1) {

                        if (db.insert(getConvenio())) {
                            setMsgConfirma("Convênio cadastrado com sucesso!");
                        } else {
                            setMsgConfirma("Erro ao cadastrar convênio!");
                        }
                    } else {
                        db.getEntityManager().getTransaction().begin();
                        if (db.update(getConvenio())) {
                            db.getEntityManager().getTransaction().commit();
                            setMsgConfirma("Convênio atualizado com Sucesso!");
                        } else {
                            db.getEntityManager().getTransaction().rollback();
                            setMsgConfirma("Erro ao atualizar convênio!");
                        }
                    }

                } else {
                    setMsgConfirma("Grupo de convênio inválido!");
                }
            } else {
                setMsgConfirma("Pessoa jurídica inválida!");
            }
        } else {
            setMsgConfirma("Convẽnio já existe!");
        }
    }

    public String novo() {
        setConvenio(new Convenio());
        setJuridica(new Juridica());
        setSubGrupoConvenio(new SubGrupoConvenio());
        setMsgConfirma("");
        return "convenio";
    }

    public String novoJuridica() {
        setJuridica(new Juridica());
        return "convenio";
    }

    public String novoGrupoConvenio() {
        setSubGrupoConvenio(new SubGrupoConvenio());
        return "convenio";
    }

    public String excluir() {
        ConvenioDB db = new ConvenioDBToplink();
        if (convenio.getId() != -1) {
            db.getEntityManager().getTransaction().begin();
            setConvenio(db.pesquisaCodigo(getConvenio().getId()));
            if (db.delete(getConvenio())) {
                db.getEntityManager().getTransaction().commit();
                setMsgConfirma("Convênio excluido com sucesso!");
            } else {
                db.getEntityManager().getTransaction().rollback();
                setMsgConfirma("Convênio não pode ser Excluido!");
            }
        }
        return null;
    }

    public String editar() {
        convenio = (Convenio) listaConvenio.get(idIndex);
        juridica = convenio.getJuridica();
        subGrupoConvenio = convenio.getSubGrupoConvenio();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return "convenio";
    }

    public String editarSubGrupoConvenio() {
        subGrupoConvenio = (SubGrupoConvenio) getListaSubGrupo().get(idIndexSub);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("subGrupoConvenioPesquisa", subGrupoConvenio);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "convenio";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public void refreshForm() {
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public Juridica getJuridica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            juridica = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public SubGrupoConvenio getSubGrupoConvenio() {
        return subGrupoConvenio;
    }

    public void setSubGrupoConvenio(SubGrupoConvenio subGrupoConvenio) {
        this.subGrupoConvenio = subGrupoConvenio;
    }

    public int getIdIndexSub() {
        return idIndexSub;
    }

    public void setIdIndexSub(int idIndexSub) {
        this.idIndexSub = idIndexSub;
    }

    public List<SubGrupoConvenio> getListaSubGrupo() {
        SubGrupoConvenioDB db = new SubGrupoConvenioDBToplink();
        listaSubGrupo = db.pesquisaTodos();
        return listaSubGrupo;
    }

    public void setListaSubGrupo(List<SubGrupoConvenio> listaSubGrupo) {
        this.listaSubGrupo = listaSubGrupo;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public List<Convenio> getListaConvenio() {
        ConvenioDB db = new ConvenioDBToplink();
        listaConvenio = db.pesquisaTodos();
        return listaConvenio;
    }

    public void setListaConvenio(List<Convenio> listaConvenio) {
        this.listaConvenio = listaConvenio;
    }
}