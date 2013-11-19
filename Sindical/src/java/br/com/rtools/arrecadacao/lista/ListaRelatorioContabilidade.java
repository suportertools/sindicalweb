package br.com.rtools.arrecadacao.lista;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;

public class ListaRelatorioContabilidade {

    private Juridica juridica;
    private PessoaEndereco pessoaEndereco;
    private int quantidade;

    public ListaRelatorioContabilidade() {
        this.juridica = new Juridica();
        this.pessoaEndereco = new PessoaEndereco();
        this.quantidade = 0;
    }

    public ListaRelatorioContabilidade(Juridica juridica, PessoaEndereco pessoaEndereco, int quantidade) {
        this.juridica = juridica;
        this.pessoaEndereco = pessoaEndereco;
        this.quantidade = quantidade;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public PessoaEndereco getPessoaEndereco() {
        return pessoaEndereco;
    }

    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
