package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.MatriculaConvenioMedico;
import br.com.rtools.associativo.db.MatriculaConvenioMedicoDB;
import br.com.rtools.associativo.db.MatriculaConvenioMedicoDBToplink;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class MatriculaConvenioMedicoJSFBean {

    private MatriculaConvenioMedico matriculaConvenioMedico;
    private ServicoPessoaBean servicoPessoaJSFBean = null;
    private String msgConfirma;
    private String descPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private int idIndex;
    private List<MatriculaConvenioMedico> listaConvenio;

    public MatriculaConvenioMedicoJSFBean() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("servicoPessoaBean", new ServicoPessoaBean());
        servicoPessoaJSFBean = ((ServicoPessoaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("servicoPessoaBean"));
        servicoPessoaJSFBean.setRenderServicos(true);
        matriculaConvenioMedico = new MatriculaConvenioMedico();
        msgConfirma = "";
        descPesquisa = "";
        porPesquisa = "";
        comoPesquisa = "";
        idIndex = -1;
        listaConvenio = new ArrayList();
    }

    public String salvar() {
        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
        MatriculaConvenioMedicoDB db = new MatriculaConvenioMedicoDBToplink();
        if (servicoPessoaJSFBean.getServicoPessoa().getPessoa().getId() == -1) {
            msgConfirma = "Pesquise uma Pessoa!";
            return null;
        }
        dbSalvar.abrirTransacao();
        if (servicoPessoaJSFBean.getServicoPessoa().getId() == -1) {
            msgConfirma = servicoPessoaJSFBean.salvarServicoPessoa(null, dbSalvar);
            if (!msgConfirma.isEmpty()) {
                dbSalvar.desfazerTransacao();
                return null;
            }

            matriculaConvenioMedico.setServicoPessoa(servicoPessoaJSFBean.getServicoPessoa());
            if (dbSalvar.inserirObjeto(matriculaConvenioMedico)) {
                msgConfirma = "Matricula salva com Sucesso!";
                dbSalvar.comitarTransacao();
                //novoGenerico();
            } else {
                msgConfirma = "Erro ao Salvar Matricula!";
                dbSalvar.desfazerTransacao();
            }
        } else {
            msgConfirma = servicoPessoaJSFBean.atualizarServicoPessoa(null, dbSalvar);

            if (!msgConfirma.isEmpty()) {
                dbSalvar.desfazerTransacao();
                return null;
            }

            matriculaConvenioMedico.setServicoPessoa(servicoPessoaJSFBean.getServicoPessoa());
            if (dbSalvar.alterarObjeto(matriculaConvenioMedico)) {
                msgConfirma = "Matricula atualizada com Sucesso!";
                dbSalvar.comitarTransacao();
                //novoGenerico();
            } else {
                msgConfirma = "Erro ao atualizar Matricula!";
                dbSalvar.desfazerTransacao();
            }
        }
        return null;
    }

    public String novo() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("matriculaConvenioMedicoBean", new MatriculaConvenioMedicoJSFBean());
        return "convenioMedico";
    }

    public String excluir() {
        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
        dbSalvar.abrirTransacao();
        if (servicoPessoaJSFBean.getServicoPessoa().getId() != -1) {
            if (dbSalvar.deletarObjeto((MatriculaConvenioMedico) dbSalvar.pesquisaCodigo(matriculaConvenioMedico.getId(), "MatriculaConvenioMedico"))) {
                if (dbSalvar.deletarObjeto((ServicoPessoa) dbSalvar.pesquisaCodigo(servicoPessoaJSFBean.getServicoPessoa().getId(), "ServicoPessoa"))) {
                    dbSalvar.comitarTransacao();
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("matriculaConvenioMedicoBean", new MatriculaConvenioMedicoJSFBean());
                    ((MatriculaConvenioMedicoJSFBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("matriculaConvenioMedicoBean")).setMsgConfirma("Matricula Excluida com sucesso!");
                } else {
                    msgConfirma = "Erro ao excluir serviço pessoa!";
                    dbSalvar.desfazerTransacao();
                }
            } else {
                msgConfirma = "Erro ao excluir Convênio médico!";
                dbSalvar.desfazerTransacao();
            }
        }
        return null;
    }

    public String editar() {
        matriculaConvenioMedico = (MatriculaConvenioMedico) listaConvenio.get(idIndex);
        servicoPessoaJSFBean.setServicoPessoa(matriculaConvenioMedico.getServicoPessoa());
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        servicoPessoaJSFBean.editar(matriculaConvenioMedico.getServicoPessoa());
        listaConvenio.clear();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "convenioMedico";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public void refreshForm() {
    }

    public void acaoPesquisaInicial() {
        listaConvenio.clear();
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        listaConvenio.clear();
        comoPesquisa = "P";
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public List<MatriculaConvenioMedico> getListaConvenio() {
        if (listaConvenio.isEmpty()) {
            MatriculaConvenioMedicoDB db = new MatriculaConvenioMedicoDBToplink();
            listaConvenio = db.pesquisaConvenioMedico(descPesquisa, porPesquisa, comoPesquisa);
        }
        return listaConvenio;
    }

    public void setListaConvenio(List<MatriculaConvenioMedico> listaConvenio) {
        this.listaConvenio = listaConvenio;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public MatriculaConvenioMedico getMatriculaConvenioMedico() {
        return matriculaConvenioMedico;
    }

    public void setMatriculaConvenioMedico(MatriculaConvenioMedico matriculaConvenioMedico) {
        this.matriculaConvenioMedico = matriculaConvenioMedico;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
}
