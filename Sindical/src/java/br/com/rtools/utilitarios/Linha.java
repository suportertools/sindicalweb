package br.com.rtools.utilitarios;

import java.util.List;

public class Linha {

    private Object valor;
    private Linha coluna;

    public Linha(Object valor, Linha coluna) {
        setValor(valor);
        setColuna(coluna);
    }

    public Linha() {
        setValor(null);
        setColuna(null);
    }

    public synchronized Object getValor() {
        return valor;
    }

    public synchronized void setValor(Object valor) {
        this.valor = valor;
    }

    public synchronized Linha getColuna() {
        return coluna;
    }

    public void setColuna(Linha coluna) {
        this.coluna = coluna;
    }

    public static Linha preencherLinha(Linha linha, List lista, int indiceLista) {
        if (indiceLista < lista.size()) {
            linha.setValor(lista.get(indiceLista));
            linha.setColuna(new Linha());
            Linha.preencherLinha(linha.getColuna(), lista, ++indiceLista);
        }
        return linha;
    }
}
