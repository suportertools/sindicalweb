package br.com.rtools.impressao;

public class ConviteClube {

    // CONVIDADO
    private String nome;
    private String emissao;
    private String validade;
    // LOGO DO CONVITE
    private String foto;
    private String barras;
    private String obs;
    private String semana;

    public ConviteClube() {
        this.nome = "";
        this.emissao = "";;
        this.validade = "";
        this.foto = "";
        this.barras = "";
        this.semana = "";
        this.obs = "";
    }

    public ConviteClube(String nome, String emissao, String validade, String foto, String barras, String obs, String semana) {
        this.nome = nome;
        this.emissao = emissao;
        this.validade = validade;
        this.foto = foto;
        this.barras = barras;
        this.obs = obs;
        this.semana = semana;
    }

    // CONVIDADO    
    public String getNome() {
        return nome;
    }

    // CONVIDADO
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    // LOGO DO CONVITE    
    public String getFoto() {
        return foto;
    }

    // LOGO DO CONVITE    
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getBarras() {
        return barras;
    }

    public void setBarras(String barras) {
        this.barras = barras;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getSemana() {
        return semana;
    }

    public void setSemana(String semana) {
        this.semana = semana;
    }

}
