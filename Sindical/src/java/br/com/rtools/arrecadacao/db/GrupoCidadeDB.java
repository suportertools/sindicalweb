package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.GrupoCidade;
import java.util.List;
import javax.persistence.EntityManager;

public interface GrupoCidadeDB {

    public boolean insert(GrupoCidade grupoCidade);

    public boolean update(GrupoCidade grupoCidade);

    public boolean delete(GrupoCidade grupoCidade);

    public EntityManager getEntityManager();

    public GrupoCidade pesquisaCodigo(int id);

    public List pesquisaTodos();

    public GrupoCidade idGrupoCidade(GrupoCidade des_grupoCidade);

    public List<ConvencaoCidade> pesquisaGrupoPorConvencao(int idConvencao);
}
