package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.MatriculaConvenioMedico;
import br.com.rtools.associativo.db.MatriculaConvenioMedicoDB;
import br.com.rtools.associativo.db.MatriculaConvenioMedicoDBToplink;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class MatriculaConvenioMedicoBean implements Serializable {

    private MatriculaConvenioMedico matriculaConvenioMedico;
    private ServicoPessoaBean servicoPessoaBean;
    private String mensagem;
    private String descPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private List<MatriculaConvenioMedico> listaConvenio;

    @PostConstruct
    public void init() {
        servicoPessoaBean = null;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("servicoPessoaBean", new ServicoPessoaBean());
        servicoPessoaBean = ((ServicoPessoaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("servicoPessoaBean"));
        servicoPessoaBean.setRenderServicos(true);
        matriculaConvenioMedico = new MatriculaConvenioMedico();
        mensagem = "";
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        listaConvenio = new ArrayList();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("matriculaConvenioMedicoBean");
        GenericaSessao.remove("servicoPessoaBean");
    }

    public void save() {
        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
        MatriculaConvenioMedicoDB db = new MatriculaConvenioMedicoDBToplink();
        if (servicoPessoaBean.getServicoPessoa().getPessoa().getId() == -1) {
            mensagem = "Pesquise uma Pessoa!";
            return;
        }
        dbSalvar.abrirTransacao();
        if (servicoPessoaBean.getServicoPessoa().getId() == -1) {
            mensagem = servicoPessoaBean.salvarServicoPessoa(null, dbSalvar);
            if (!mensagem.isEmpty()) {
                dbSalvar.desfazerTransacao();
                return;
            }

            matriculaConvenioMedico.setServicoPessoa(servicoPessoaBean.getServicoPessoa());
            if (dbSalvar.inserirObjeto(matriculaConvenioMedico)) {
                mensagem = "Matricula salva com Sucesso!";
                dbSalvar.comitarTransacao();
            } else {
                mensagem = "Erro ao Salvar Matricula!";
                dbSalvar.desfazerTransacao();
            }
        } else {
            mensagem = servicoPessoaBean.atualizarServicoPessoa(null, dbSalvar);

            if (!mensagem.isEmpty()) {
                dbSalvar.desfazerTransacao();
                return;
            }

            matriculaConvenioMedico.setServicoPessoa(servicoPessoaBean.getServicoPessoa());
            if (dbSalvar.alterarObjeto(matriculaConvenioMedico)) {
                mensagem = "Matricula atualizada com Sucesso!";
                dbSalvar.comitarTransacao();
            } else {
                mensagem = "Erro ao atualizar Matricula!";
                dbSalvar.desfazerTransacao();
            }
        }
    }

    public void clear() {
        GenericaSessao.remove("matriculaConvenioMedicoBean");
    }

    public void excluir() {
        if (servicoPessoaBean.getServicoPessoa().getId() != -1) {
            DaoInterface dao = new Dao();
            dao.openTransaction();
            if (dao.delete((MatriculaConvenioMedico) dao.find("MatriculaConvenioMedico", matriculaConvenioMedico.getId()))) {
                if (dao.delete((ServicoPessoa) dao.find("ServicoPessoa", servicoPessoaBean.getServicoPessoa().getId()))) {
                    dao.commit();
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("matriculaConvenioMedicoBean", new MatriculaConvenioMedicoBean());
                    ((MatriculaConvenioMedicoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("matriculaConvenioMedicoBean")).setMensagem("Matricula Excluida com sucesso!");
                } else {
                    mensagem = "Erro ao excluir serviço pessoa!";
                    dao.rollback();
                }
            } else {
                mensagem = "Erro ao excluir Convênio médico!";
                dao.rollback();
            }
        } else {
            mensagem = "Pesquisar um registro!";
        }
    }

    public String edit(MatriculaConvenioMedico mcm) {
        matriculaConvenioMedico = mcm;
        servicoPessoaBean.setServicoPessoa(matriculaConvenioMedico.getServicoPessoa());
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        servicoPessoaBean.editar(matriculaConvenioMedico.getServicoPessoa());
        listaConvenio.clear();
        GenericaSessao.put("linkClicado", true);
        if (!GenericaSessao.exists("urlRetorno")) {
            return "convenioMedico";
        } else {
            return (String) GenericaSessao.getString("urlRetorno");
        }
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

    public MatriculaConvenioMedico getMatriculaConvenioMedico() {
        return matriculaConvenioMedico;
    }

    public void setMatriculaConvenioMedico(MatriculaConvenioMedico matriculaConvenioMedico) {
        this.matriculaConvenioMedico = matriculaConvenioMedico;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
