package br.com.rtools.seguranca;

import br.com.rtools.seguranca.db.RotinaDao;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

@Entity
@Table(name = "seg_rotina")
@NamedQueries({
    @NamedQuery(name = "Rotina.pesquisaID", query = "SELECT ROT FROM Rotina AS ROT WHERE ROT.id = :pid"),
    @NamedQuery(name = "Rotina.findAll", query = "SELECT ROT FROM Rotina AS ROT ORDER BY ROT.rotina ASC, ROT.pagina ASC ")
})
public class Rotina implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_rotina", length = 50, nullable = false)
    private String rotina;
    @Column(name = "ds_nome_pagina", length = 100, nullable = false)
    private String pagina;
    @Column(name = "ds_acao", length = 100, nullable = false)
    private String acao;
    @Column(name = "ds_classe", length = 100)
    private String classe;
    @Column(name = "is_ativo")
    private boolean ativo;

    public Rotina() {
        this.id = -1;
        this.rotina = "";
        this.pagina = "";
        this.acao = "";
        this.classe = "";
        this.ativo = false;
    }

    public Rotina(int id, String rotina, String pagina, String acao, String classe, boolean ativo) {
        this.id = id;
        this.rotina = rotina;
        this.pagina = pagina;
        this.acao = acao;
        this.classe = classe;
        this.ativo = ativo;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRotina() {
        return rotina;
    }

    public void setRotina(String rotina) {
        this.rotina = rotina;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public Rotina get() {
        try {
            HttpServletRequest paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            return new RotinaDao().pesquisaRotinaPorPagina(converteURL(paginaRequerida.getRequestURI()));
        } catch (Exception e) {
            return null;
        }
    }

    public String converteURL(String urlDest) {
        return urlDest.substring(urlDest.lastIndexOf("/") + 1, urlDest.lastIndexOf("."));
    }
}
