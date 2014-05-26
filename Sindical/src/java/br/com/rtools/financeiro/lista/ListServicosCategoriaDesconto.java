package br.com.rtools.financeiro.lista;

import br.com.rtools.associativo.CategoriaDesconto;
import br.com.rtools.utilitarios.Moeda;

public class ListServicosCategoriaDesconto {

    private CategoriaDesconto categoriaDesconto;
    private float valorDesconto;

    public ListServicosCategoriaDesconto() {
        this.categoriaDesconto = new CategoriaDesconto();
        this.valorDesconto = 0;
    }

    public ListServicosCategoriaDesconto(CategoriaDesconto categoriaDesconto, float valorDesconto) {
        this.categoriaDesconto = categoriaDesconto;
        this.valorDesconto = valorDesconto;
    }

    public CategoriaDesconto getCategoriaDesconto() {
        return categoriaDesconto;
    }

    public void setCategoriaDesconto(CategoriaDesconto categoriaDesconto) {
        this.categoriaDesconto = categoriaDesconto;
    }

    public float getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(float valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public String getValorDescontoString() {
        return Moeda.converteR$Float(valorDesconto);
    }

    public void setValorDescontoString(String valorDescontoString) {
        this.valorDesconto = Moeda.converteUS$(valorDescontoString);
    }

}
