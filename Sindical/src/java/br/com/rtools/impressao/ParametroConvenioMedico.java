package br.com.rtools.impressao;

import java.io.Serializable;
import java.math.BigDecimal;

public class ParametroConvenioMedico implements Serializable {

    private String subgrupo;
    private String servico;
    private String nome;
    private Integer idade;
    private String categoria;
    private String vigoracao;
    private BigDecimal valor;

    public ParametroConvenioMedico() {
        this.subgrupo = "";
        this.servico = "";
        this.nome = "";
        this.idade = 0;
        this.categoria = "";
        this.vigoracao = "";
        this.valor = new BigDecimal(0);
    }

    public ParametroConvenioMedico(String subgrupo, String servico, String nome, Integer idade, String categoria, String vigoracao, BigDecimal valor) {
        this.subgrupo = subgrupo;
        this.servico = servico;
        this.nome = nome;
        this.idade = idade;
        this.categoria = categoria;
        this.vigoracao = vigoracao;
        this.valor = valor;
    }

    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getVigoracao() {
        return vigoracao;
    }

    public void setVigoracao(String vigoracao) {
        this.vigoracao = vigoracao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
