package br.com.rtools.homologacao.lista;

import br.com.rtools.homologacao.*;

public class ListaHorarios {

    private Horarios horarios;
    private String ativo;

    public ListaHorarios() {
        this.horarios = new Horarios();
        this.ativo = "";
    }

    public ListaHorarios(Horarios horarios, String ativo) {
        this.horarios = horarios;
        this.ativo = ativo;
    }

    public Horarios getHorarios() {
        return horarios;
    }

    public void setHorarios(Horarios horarios) {
        this.horarios = horarios;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }
}
