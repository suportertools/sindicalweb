package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.MatriculaConvenioMedico;
import br.com.rtools.associativo.db.MatriculaConvenioMedicoDB;
import br.com.rtools.associativo.db.MatriculaConvenioMedicoDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
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

@ManagedBean
@SessionScoped
public class MatriculaConvenioMedicoBean implements Serializable {
    private MatriculaConvenioMedico matriculaConvenioMedico;
    private ServicoPessoaBean servicoPessoaBean;
    private String message;
    private String descPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private Boolean ativo;
    private List<MatriculaConvenioMedico> listaConvenio;

    @PostConstruct
    public void init() {
        servicoPessoaBean = null;
        GenericaSessao.put("servicoPessoaBean", new ServicoPessoaBean());
        servicoPessoaBean = ((ServicoPessoaBean) GenericaSessao.getObject("servicoPessoaBean"));
        servicoPessoaBean.setRenderServicos(true);
        matriculaConvenioMedico = new MatriculaConvenioMedico();
        message = "";
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        listaConvenio = new ArrayList();
        ativo = true;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("matriculaConvenioMedicoBean");
        GenericaSessao.remove("servicoPessoaBean");
    }

    public void save() {
        servicoPessoaBean = ((ServicoPessoaBean) GenericaSessao.getObject("servicoPessoaBean"));
        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
        NovoLog novoLog = new NovoLog();
        if (servicoPessoaBean.getTitular().getId() == -1) {
            message = "Pesquise uma Pessoa!";
            return;
        }
        
        MatriculaConvenioMedicoDB db = new MatriculaConvenioMedicoDBToplink();
        List<MatriculaConvenioMedico> result = db.listaConvenioPessoa(servicoPessoaBean.getTitular().getPessoa().getId(), Integer.valueOf(servicoPessoaBean.getListaServicos().get(servicoPessoaBean.getIdServico()).getDescription()));
        
        if (servicoPessoaBean.getServicoPessoa().getId() == -1) {
            if (result.size() >= 1){
                message = "Pessoa já contém Convênio Ativo!";
                return;
            }
            
            dbSalvar.abrirTransacao();
            message = servicoPessoaBean.salvarServicoPessoa(null, dbSalvar);
            if (!message.isEmpty()) {
                dbSalvar.desfazerTransacao();
                return;
            }
            matriculaConvenioMedico.setServicoPessoa(servicoPessoaBean.getServicoPessoa());
            if (dbSalvar.inserirObjeto(matriculaConvenioMedico)) {
                novoLog.save(""
                        + "ID: " + matriculaConvenioMedico.getId()
                        + " - Código: " + matriculaConvenioMedico.getCodigo()
                        + " - Pessoa: (" + matriculaConvenioMedico.getServicoPessoa().getPessoa().getId() + ") " + matriculaConvenioMedico.getServicoPessoa().getPessoa().getNome()
                        + " - Cobrança (Pessoa): (" + matriculaConvenioMedico.getServicoPessoa().getCobranca().getId() + ") " + matriculaConvenioMedico.getServicoPessoa().getCobranca().getNome()
                        + " - Serviço Pessoa: (" + matriculaConvenioMedico.getServicoPessoa().getId() + ") " + matriculaConvenioMedico.getServicoPessoa().getServicos().getDescricao()
                );
                message = "Matricula salva com Sucesso!";
                dbSalvar.comitarTransacao();
            } else {
                message = "Erro ao Salvar Matricula!";
                dbSalvar.desfazerTransacao();
            }
        } else {
            if (result.size() >= 1 && (servicoPessoaBean.getServicoPessoa().getServicos().getId() != result.get(0).getServicoPessoa().getServicos().getId())){
                message = "Pessoa já contém Convênio Ativo!";
                return;
            }
            dbSalvar.abrirTransacao();
            
            message = servicoPessoaBean.atualizarServicoPessoa(null, dbSalvar);

            if (!message.isEmpty()) {
                dbSalvar.desfazerTransacao();
                return;
            }
            MatriculaConvenioMedico mcm = (MatriculaConvenioMedico) dbSalvar.pesquisaObjeto(matriculaConvenioMedico.getId(), "MatriculaConvenioMedico");
            matriculaConvenioMedico.setServicoPessoa(servicoPessoaBean.getServicoPessoa());
            String beforeUpdate = ""
                    + "ID: " + mcm.getId()
                    + " - Código: " + mcm.getCodigo()
                    + " - Pessoa: (" + mcm.getServicoPessoa().getPessoa().getId() + ") " + mcm.getServicoPessoa().getPessoa().getNome()
                    + " - Cobrança (Pessoa): (" + mcm.getServicoPessoa().getCobranca().getId() + ") " + mcm.getServicoPessoa().getCobranca().getNome()
                    + " - Serviço Pessoa: (" + mcm.getServicoPessoa().getId() + ") " + mcm.getServicoPessoa().getServicos().getDescricao();
            if (dbSalvar.alterarObjeto(matriculaConvenioMedico)) {
                message = "Matricula atualizada com Sucesso!";
                novoLog.update(beforeUpdate, ""
                        + "ID: " + matriculaConvenioMedico.getId()
                        + " - Código: " + matriculaConvenioMedico.getCodigo()
                        + " - Pessoa: (" + matriculaConvenioMedico.getServicoPessoa().getPessoa().getId() + ") " + matriculaConvenioMedico.getServicoPessoa().getPessoa().getNome()
                        + " - Cobrança (Pessoa): (" + matriculaConvenioMedico.getServicoPessoa().getCobranca().getId() + ") " + matriculaConvenioMedico.getServicoPessoa().getCobranca().getNome()
                        + " - Serviço Pessoa: (" + matriculaConvenioMedico.getServicoPessoa().getId() + ") " + matriculaConvenioMedico.getServicoPessoa().getServicos().getDescricao()
                );
                dbSalvar.comitarTransacao();
            } else {
                message = "Erro ao atualizar Matricula!";
                dbSalvar.desfazerTransacao();
            }
        }
    }

    public void clear() {
        GenericaSessao.remove("matriculaConvenioMedicoBean");
        GenericaSessao.remove("servicoPessoaBean");
    }

    public void delete() {
        if (servicoPessoaBean.getServicoPessoa().getId() != -1) {
            Dao dao = new Dao();
            NovoLog novoLog = new NovoLog();
            dao.openTransaction();
            matriculaConvenioMedico.setDtInativo(DataHoje.dataHoje());
            matriculaConvenioMedico.getServicoPessoa().setAtivo(false);
            if (dao.update(matriculaConvenioMedico)) {
                if (dao.update(matriculaConvenioMedico.getServicoPessoa())) {
                    novoLog.delete(
                            "ID: " + matriculaConvenioMedico.getId()
                            + " - Código: " + matriculaConvenioMedico.getCodigo()
                            + " - Pessoa: (" + matriculaConvenioMedico.getServicoPessoa().getPessoa().getId() + ") " + matriculaConvenioMedico.getServicoPessoa().getPessoa().getNome()
                            + " - Cobrança (Pessoa): (" + matriculaConvenioMedico.getServicoPessoa().getCobranca().getId() + ") " + matriculaConvenioMedico.getServicoPessoa().getCobranca().getNome()
                            + " - Serviço Pessoa: (" + matriculaConvenioMedico.getServicoPessoa().getId() + ") " + matriculaConvenioMedico.getServicoPessoa().getServicos().getDescricao()
                    );
                    servicoPessoaBean.setServicoPessoa(matriculaConvenioMedico.getServicoPessoa());
                    dao.commit();
                    // GenericaSessao.put("matriculaConvenioMedicoBean", new MatriculaConvenioMedicoBean());
                    // ((MatriculaConvenioMedicoBean) GenericaSessao.getObject("matriculaConvenioMedicoBean")).setMessage("Matricula Excluida com sucesso!");
                    message = "Matrícula Inativada!";
                } else {
                    message = "Erro ao excluir serviço pessoa!";
                    dao.rollback();
                }
            } else {
                message = "Erro ao excluir Convênio médico!";
                dao.rollback();
            }
        } else {
            message = "Pesquisar um registro!";
        }
    }

    public String edit(MatriculaConvenioMedico mcm) {
        Dao dao = new Dao();
        mcm = (MatriculaConvenioMedico) dao.rebind(mcm);
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
        loadList();
    }

    public void acaoPesquisaParcial() {
        listaConvenio.clear();
        comoPesquisa = "P";
        loadList();
    }

    public void loadList() {
        if (!(descPesquisa.trim()).isEmpty()) {
            MatriculaConvenioMedicoDB db = new MatriculaConvenioMedicoDBToplink();
            listaConvenio = db.pesquisaConvenioMedico(descPesquisa.trim(), porPesquisa, comoPesquisa, ativo);
        }
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
