package br.com.rtools.impressao;


public class ParametroRelatorioFinanceiro {
    private String logo_sindicato;
    private String grupo;
    private String subgrupo;
    private String servico;
    private float valor;

    public ParametroRelatorioFinanceiro(String logo_sindicato, String grupo, String subgrupo, String servico, float valor) {
        this.logo_sindicato = logo_sindicato;
        this.grupo = grupo;
        this.subgrupo = subgrupo;
        this.servico = servico;
        this.valor = valor;
    }
    
    public String getLogo_sindicato() {
        return logo_sindicato;
    }

    public void setLogo_sindicato(String logo_sindicato) {
        this.logo_sindicato = logo_sindicato;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
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

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
