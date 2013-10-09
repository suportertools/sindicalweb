package br.com.rtools.associativo.db;

import br.com.rtools.associativo.GrupoConvenio;
import java.util.List;
import javax.persistence.EntityManager;

public interface GrupoConvenioDB {

    public boolean insert(GrupoConvenio grupoConvenio);

    public boolean update(GrupoConvenio grupoConvenio);

    public boolean delete(GrupoConvenio grupoConvenio);

    public EntityManager getEntityManager();

    public GrupoConvenio pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaGrupoConvenioPorDescricao(String descricao);
}
