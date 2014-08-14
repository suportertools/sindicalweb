package br.com.rtools.homologacao;

import br.com.rtools.homologacao.db.HomologacaoDB;
import br.com.rtools.homologacao.db.HomologacaoDBToplink;

public class ListaAgendamento {

    private Agendamento agendamento;
    private Senha senha;
    private boolean habilitaAlteracao;
    private int quantidade;
    private String usuarioAgendador;
    private String tblEstilo;

    public ListaAgendamento() {
        this.agendamento = new Agendamento();
        this.senha = new Senha();
        this.habilitaAlteracao = false;
        this.quantidade = 0;
        this.usuarioAgendador = "";
        this.tblEstilo = "";
    }

    public ListaAgendamento(Agendamento agendamento, Senha senha, boolean habilitaAlteracao, int quantidade, String usuarioAgendador, String tblEstilo) {
        this.agendamento = agendamento;
        this.senha = senha;
        this.habilitaAlteracao = habilitaAlteracao;
        this.quantidade = quantidade;
        this.usuarioAgendador = usuarioAgendador;
        this.tblEstilo = tblEstilo;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public Senha getSenha() {
        return senha;
    }

    public void setSenha(Senha senha) {
        this.senha = senha;
    }

    public Senha mostrarSenha() {
        Senha senha2 = null;
        if (agendamento.getId() != -1) {
            HomologacaoDB db = new HomologacaoDBToplink();
            senha2 = db.pesquisaSenhaAgendamento(agendamento.getId());
        }
        return senha2;
    }

    public boolean isHabilitaAlteracao() {
        return habilitaAlteracao;
    }

    public void setHabilitaAlteracao(boolean habilitaAlteracao) {
        this.habilitaAlteracao = habilitaAlteracao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getUsuarioAgendador() {
        return usuarioAgendador;
    }

    public void setUsuarioAgendador(String usuarioAgendador) {
        this.usuarioAgendador = usuarioAgendador;
    }

    public String getTblEstilo() {
        return tblEstilo;
    }

    public void setTblEstilo(String tblEstilo) {
        this.tblEstilo = tblEstilo;
    }
}
