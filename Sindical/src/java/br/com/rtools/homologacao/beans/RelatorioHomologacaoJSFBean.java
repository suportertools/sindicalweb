/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.homologacao.beans;

import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.utilitarios.DataObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class RelatorioHomologacaoJSFBean {
    private int idRelatorio = 0;
    private String selectAccordion = "simples";
    private List<DataObject> listaMenuRHomologacao = new ArrayList();
    private boolean booEmpresa = false;
    private boolean booFuncionario = false;
    private boolean booData = false;
    private boolean booHomologador = false;
    private Juridica juridica = new Juridica();
    private Fisica fisica = new Fisica();
    private String datai = "";
    private String dataf = "";
    
    public List<SelectItem> getListaTipoRelatorios(){
        List<SelectItem> relatorios = new Vector<SelectItem>();
        int i = 0;
        RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
        List select = db.pesquisaTipoRelatorio(171);
        while (i < select.size()){
            relatorios.add (new SelectItem( new Integer(i),
                                        (String) ((Relatorios) select.get(i)).getNome(),
                                        Integer.toString(((Relatorios) select.get(i)).getId()) ));
            i++;
        }
    return relatorios;
    }

    public int getIdRelatorio() {
        return idRelatorio;
    }

    public void setIdRelatorio(int idRelatorio) {
        this.idRelatorio = idRelatorio;
    }

    public String getSelectAccordion() {
        return selectAccordion;
    }

    public void setSelectAccordion(String selectAccordion) {
        this.selectAccordion = selectAccordion;
    }

    public List<DataObject> getListaMenuRHomologacao() {
        if (listaMenuRHomologacao.isEmpty()){
            listaMenuRHomologacao.add(new DataObject("* Nome Empresa ", "Editar", null, null, null, null));
            listaMenuRHomologacao.add(new DataObject("* Nome Funcionário ", "Editar", null, null, null, null));
            listaMenuRHomologacao.add(new DataObject("* Data Inicial/Final ", "Editar", null, null, null, null));
            listaMenuRHomologacao.add(new DataObject("* Homologador ", "Editar", null, null, null, null));
        }        
        return listaMenuRHomologacao;
    }

    public void setListaMenuRHomologacao(List<DataObject> listaMenuRHomologacao) {
        this.listaMenuRHomologacao = listaMenuRHomologacao;
    }
    
    public String editarOpcao(int index){
        if (listaMenuRHomologacao.get(index).getArgumento1().equals("Remover"))
            listaMenuRHomologacao.get(index).setArgumento1("Editar");
        else
            listaMenuRHomologacao.get(index).setArgumento1("Remover");
            
        if (index == 0){
            if (booEmpresa) booEmpresa = false; else booEmpresa = true;
        }else if (index == 1){
            if (booFuncionario) booFuncionario = false; else booFuncionario = true;
        }else if (index == 2){
            if (booData) booData = false; else booData = true;
        }else if (index == 3){
            if (booHomologador) booHomologador = false; else booHomologador = true;
        }
        return "relatorioHomologacao";
    }    

    public boolean isBooEmpresa() {
        return booEmpresa;
    }

    public void setBooEmpresa(boolean booEmpresa) {
        this.booEmpresa = booEmpresa;
    }

    public boolean isBooFuncionario() {
        return booFuncionario;
    }

    public void setBooFuncionario(boolean booFuncionario) {
        this.booFuncionario = booFuncionario;
    }

    public boolean isBooData() {
        return booData;
    }

    public void setBooData(boolean booData) {
        this.booData = booData;
    }

    public boolean isBooHomologador() {
        return booHomologador;
    }

    public void setBooHomologador(boolean booHomologador) {
        this.booHomologador = booHomologador;
    }

    public Juridica getJuridica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null){
            juridica = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Fisica getFisica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null){
            fisica = (Fisica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        }        
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public String getDatai() {
        return datai;
    }

    public void setDatai(String datai) {
        this.datai = datai;
    }

    public String getDataf() {
        return dataf;
    }

    public void setDataf(String dataf) {
        this.dataf = dataf;
    }
    
}
