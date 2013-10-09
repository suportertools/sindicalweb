package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.GrupoCidades;
import java.util.List;
import javax.persistence.EntityManager;

public interface GrupoCidadesDB {

    public boolean insert(GrupoCidades grupoCidades);

    public boolean update(GrupoCidades grupoCidades);

    public boolean delete(GrupoCidades grupoCidades);

    public EntityManager getEntityManager();

    public GrupoCidades pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaTodosCidadeAgrupada();

    public GrupoCidades idGrupoCidades(GrupoCidades des_grupoCidades);

    public List pesquisaPorGrupo(int idGrupoCidade);

    public GrupoCidade grupoCidadesPorPessoa(int idPessoa, int idConvencao);

    public List pesquisaPorCidade(int idCidade);

    public List pesquisaCidadesBase();
}
