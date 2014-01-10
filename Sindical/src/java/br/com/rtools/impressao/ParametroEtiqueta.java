/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.rtools.impressao;

/**
 *
 * @author rtools
 */
public class ParametroEtiqueta {
    private int jurId;
    private String jurNome;
    private String jurEndereco;
    private String jurLogradouro;
    private String jurNumero;
    private String jurComplemento;
    private String jurBairro;
    private String jurCep;
    private String jurCidade;
    private String jurUF;
    private String jurTelefone;
    private String jurEmail;
    private String jurTipoDocumento;
    private String jurDocumento;
    
    public ParametroEtiqueta(int jurId, String jurNome, String jurEndereco, String jurLogradouro, String jurNumero, String jurComplemento, String jurBairro, String jurCep, String jurCidade, String jurUF, String jurTelefone, String jurEmail, String jurTipoDocumento, String jurDocumento) {
        this.jurId = jurId;
        this.jurNome = jurNome;
        this.jurEndereco = jurEndereco;
        this.jurLogradouro = jurLogradouro;
        this.jurNumero = jurNumero;
        this.jurComplemento = jurComplemento;
        this.jurBairro = jurBairro;
        this.jurCep = jurCep;
        this.jurCidade = jurCidade;
        this.jurUF = jurUF;
        this.jurTelefone = jurTelefone;
        this.jurEmail = jurEmail;
        this.jurTipoDocumento = jurTipoDocumento;
        this.jurDocumento = jurDocumento;
    }

    public int getJurId() {
        return jurId;
    }

    public void setJurId(int jurId) {
        this.jurId = jurId;
    }

    public String getJurNome() {
        return jurNome;
    }

    public void setJurNome(String jurNome) {
        this.jurNome = jurNome;
    }

    public String getJurEndereco() {
        return jurEndereco;
    }

    public void setJurEndereco(String jurEndereco) {
        this.jurEndereco = jurEndereco;
    }

    public String getJurLogradouro() {
        return jurLogradouro;
    }

    public void setJurLogradouro(String jurLogradouro) {
        this.jurLogradouro = jurLogradouro;
    }

    public String getJurNumero() {
        return jurNumero;
    }

    public void setJurNumero(String jurNumero) {
        this.jurNumero = jurNumero;
    }

    public String getJurComplemento() {
        return jurComplemento;
    }

    public void setJurComplemento(String jurComplemento) {
        this.jurComplemento = jurComplemento;
    }

    public String getJurBairro() {
        return jurBairro;
    }

    public void setJurBairro(String jurBairro) {
        this.jurBairro = jurBairro;
    }

    public String getJurCep() {
        return jurCep;
    }

    public void setJurCep(String jurCep) {
        this.jurCep = jurCep;
    }

    public String getJurCidade() {
        return jurCidade;
    }

    public void setJurCidade(String jurCidade) {
        this.jurCidade = jurCidade;
    }

    public String getJurUF() {
        return jurUF;
    }

    public void setJurUF(String jurUF) {
        this.jurUF = jurUF;
    }

    public String getJurTelefone() {
        return jurTelefone;
    }

    public void setJurTelefone(String jurTelefone) {
        this.jurTelefone = jurTelefone;
    }

    public String getJurEmail() {
        return jurEmail;
    }

    public void setJurEmail(String jurEmail) {
        this.jurEmail = jurEmail;
    }

    public String getJurTipoDocumento() {
        return jurTipoDocumento;
    }

    public void setJurTipoDocumento(String jurTipoDocumento) {
        this.jurTipoDocumento = jurTipoDocumento;
    }

    public String getJurDocumento() {
        return jurDocumento;
    }

    public void setJurDocumento(String jurDocumento) {
        this.jurDocumento = jurDocumento;
    }
}
