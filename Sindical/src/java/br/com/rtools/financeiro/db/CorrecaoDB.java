package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Correcao;
import br.com.rtools.financeiro.Servicos;
import java.util.List;

public interface CorrecaoDB {

    public List pesquisaRefValida(Servicos servicos, String refInicial, String refFinal);

    public Correcao pesquisaCorrecao(Servicos servicos, String referencia);
}
