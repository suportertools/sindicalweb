package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Correcao;
import br.com.rtools.financeiro.Servicos;
import java.util.List;
import javax.persistence.EntityManager;

public interface CorrecaoDB {
    public EntityManager getEntityManager();
    public boolean insert(Correcao correcao);
    public boolean update(Correcao correcao);
    public boolean delete(Correcao correcao);
    public List pesquisaTodos();
    public Correcao pesquisaCodigo(int id);
    public List pesquisaRefValida(Servicos servicos, String refInicial, String refFinal);
    public Correcao pesquisaCorrecao(Servicos servicos, String referencia);
}
