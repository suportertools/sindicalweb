package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.MatriculaAcademia;
import br.com.rtools.associativo.db.MatriculaAcademiaDB;
import br.com.rtools.associativo.db.MatriculaAcademiaDBToplink;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class MatriculaAcademiaJSFBean {
    private MatriculaAcademia matriculaAcademia;
    private ServicoPessoaJSFBean servicoPessoaJSFBean;
    private String descPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private String msgConfirma;
    private int idIndex;
    private List<MatriculaAcademia> listaAcademia;

    public MatriculaAcademiaJSFBean(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("servicoPessoaBean",new ServicoPessoaJSFBean());
        servicoPessoaJSFBean = ((ServicoPessoaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("servicoPessoaBean"));
        servicoPessoaJSFBean.setRenderServicos(true);
        matriculaAcademia = new MatriculaAcademia();
        descPesquisa = "";
        porPesquisa = "";
        comoPesquisa = "";
        msgConfirma = "";
        idIndex = -1;
        listaAcademia = new ArrayList();
    }

    public String salvar(){
        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
        if ( servicoPessoaJSFBean.getServicoPessoa().getPessoa().getId() == -1 ){
            msgConfirma = "Pesquise uma Pessoa!";
            return null;
        }
        dbSalvar.abrirTransacao();
        if (servicoPessoaJSFBean.getServicoPessoa().getId() == -1){
            msgConfirma = servicoPessoaJSFBean.salvarServicoPessoa(null, dbSalvar);
            if (msgConfirma.isEmpty()){
                matriculaAcademia.setServicoPessoa(servicoPessoaJSFBean.getServicoPessoa());
                matriculaAcademia.setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
                if (dbSalvar.inserirObjeto(matriculaAcademia)){
                    msgConfirma = "Matricula salva com Sucesso!";
                    dbSalvar.comitarTransacao();
                    //novoGenerico();
                }else{
                    msgConfirma = "Erro ao Salvar Matricula!";
                    dbSalvar.desfazerTransacao();
                }
            }else{
                //msgConfirma = "Erro ao Salvar serviço pessoa!";
                dbSalvar.desfazerTransacao();
            }
        }else{
            msgConfirma = servicoPessoaJSFBean.atualizarServicoPessoa(null, dbSalvar);
            if (msgConfirma.isEmpty()){
                matriculaAcademia.setServicoPessoa(servicoPessoaJSFBean.getServicoPessoa());
                matriculaAcademia.setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
                if (dbSalvar.alterarObjeto(matriculaAcademia)){
                    msgConfirma = "Matricula atualizada com Sucesso!";
                    dbSalvar.comitarTransacao();
                    //novoGenerico();
                }else{
                    msgConfirma = "Erro ao atualizar Matricula!";
                    dbSalvar.desfazerTransacao();
                }
            }else{
                //msgConfirma = "Erro ao atualizar serviço pessoa!";
                dbSalvar.desfazerTransacao();
            }
        }
        return null;
    }

    public String novo(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("matriculaAcademiaBean",new MatriculaAcademiaJSFBean());
        return "academia";
    }

    public String excluir(){
        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
        dbSalvar.abrirTransacao();
        if (servicoPessoaJSFBean.getServicoPessoa().getId() != -1){
            if (dbSalvar.deletarObjeto((MatriculaAcademia)dbSalvar.pesquisaCodigo(matriculaAcademia.getId(), "MatriculaAcademia"))){
                if (dbSalvar.deletarObjeto((ServicoPessoa)dbSalvar.pesquisaCodigo(servicoPessoaJSFBean.getServicoPessoa().getId(),"ServicoPessoa"))){
                    dbSalvar.comitarTransacao();
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("matriculaAcademiaBean",new MatriculaAcademiaJSFBean());
                    ((MatriculaAcademiaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("matriculaAcademiaBean")).setMsgConfirma("Matricula Excluida com sucesso!"); 
                }else{
                    msgConfirma = "Erro ao excluir serviço pessoa!";
                    dbSalvar.desfazerTransacao();
                }
            }else{
                msgConfirma = "Erro ao excluir Matricula!";
                dbSalvar.desfazerTransacao();
            }
        }
        return null;
    }

    public String editar(){
        matriculaAcademia = (MatriculaAcademia) listaAcademia.get(idIndex);
        servicoPessoaJSFBean.setServicoPessoa(matriculaAcademia.getServicoPessoa());
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        servicoPessoaJSFBean.editar(matriculaAcademia.getServicoPessoa());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
        listaAcademia.clear();
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null)
            return "academia";
        else
            return (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
    }

    public void refreshForm(){

    }

    public void acaoPesquisaInicial(){
        listaAcademia.clear();
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial(){
        listaAcademia.clear();
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
    
    public MatriculaAcademia getMatriculaAcademia() {
        return matriculaAcademia;
    }

    public void setMatriculaAcademia(MatriculaAcademia matriculaAcademia) {
        this.matriculaAcademia = matriculaAcademia;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public List<MatriculaAcademia> getListaAcademia() {
        if (listaAcademia.isEmpty()){
            MatriculaAcademiaDB db = new MatriculaAcademiaDBToplink();
            listaAcademia = db.pesquisaMatriculaAcademia(descPesquisa , porPesquisa, comoPesquisa);
        }
        return listaAcademia;
    }

    public void setListaAcademia(List<MatriculaAcademia> listaAcademia) {
        this.listaAcademia = listaAcademia;
    }
    
}
