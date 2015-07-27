package br.com.rtools.sistema.beans;

import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.sistema.dao.SisPessoaDao;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mask;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SisPessoaBean {

    private SisPessoa sisPessoa = new SisPessoa();
    private String descPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "";
    private String masc;
    private String maxl;
    private List<SisPessoa> listaSisPessoa = new ArrayList();

    public SisPessoa getSisPessoa() {
        return sisPessoa;
    }

    public void setSisPessoa(SisPessoa sisPessoa) {
        this.sisPessoa = sisPessoa;
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

    public String getMasc() {
        return masc;
    }

    public void setMasc(String masc) {
        this.masc = masc;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
    }

    public String editar(SisPessoa sp) {
        sisPessoa = sp;
        GenericaSessao.put("sisPessoaPesquisa", sisPessoa);
        GenericaSessao.put("linkClicado", true);
        sisPessoa = new SisPessoa();
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        if (GenericaSessao.exists("urlRetorno")) {
            return (String) GenericaSessao.getString("urlRetorno");
        } else {
            return null;
        }
    }

    public String getColocarMascara() {
        if (porPesquisa.equals("telefone")) {
            masc = "telefone";
        } else {
            masc = "";
        }
        return masc;
    }

    public String getColocarMaxlenght() {
        if (porPesquisa.equals("telefone")) {
            maxl = "14";
        } else {
            maxl = "50";
        }
        return maxl;
    }

    public List<SisPessoa> getListaSisPessoa() {
        SisPessoaDao sisPessoaDao = new SisPessoaDao();
        if (descPesquisa.equals("")) {
            listaSisPessoa.clear();
            return listaSisPessoa;
        } else {
            listaSisPessoa = sisPessoaDao.pesquisarSisPessoa(descPesquisa, porPesquisa, comoPesquisa);
            return listaSisPessoa;
        }
    }

    public void setListaSisPessoa(List<SisPessoa> listaSisPessoa) {
        this.listaSisPessoa = listaSisPessoa;
    }

    public String getMascaraPesquisa() {
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }
}
