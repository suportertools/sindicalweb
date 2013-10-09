package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Indice;
import br.com.rtools.financeiro.IndiceMensal;
import java.util.List;
import javax.persistence.EntityManager;

public interface IndiceMensalDB {

    public boolean insert(IndiceMensal indiceMensal);

    public boolean update(IndiceMensal indiceMensal);

    public boolean delete(IndiceMensal indiceMensal);

    public EntityManager getEntityManager();

    public List pesquisaTodos();

    public IndiceMensal pesquisaCodigo(int id);

    public Indice pesquisaCodigoIndice(int id);

    public List pesquisaTodosIndices();

    public List pesquisaIndiceMensalPorIDIndice(int idIndice);

    public List pesquisaIndMensalExistente(int idIndice, int ano, int mes);
}
