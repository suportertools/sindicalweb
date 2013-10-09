package br.com.rtools.impressao;

public class ParametroContrato {

    private String nomePessoa;

    public ParametroContrato(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public ParametroContrato() {
        this.nomePessoa = "João Aparecido de Paula";
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }
}
