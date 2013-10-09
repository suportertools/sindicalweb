package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Baixa;
import java.util.List;
import javax.persistence.EntityManager;

public interface LoteBaixaDB {

    public EntityManager getEntityManager();

    public Baixa pesquisaCodigo(int id);

    public List pesquisaTodos();

    public Baixa pesquisaLoteBaixaPorNumeroBoleto(String numero, int idContaCobranca);
}
