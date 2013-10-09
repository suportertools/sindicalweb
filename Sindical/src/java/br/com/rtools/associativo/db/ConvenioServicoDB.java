package br.com.rtools.associativo.db;

import br.com.rtools.associativo.ConvenioServico;
import java.util.List;
import javax.persistence.EntityManager;

public interface ConvenioServicoDB {

    public boolean insert(ConvenioServico convenioServico);

    public boolean update(ConvenioServico convenioServico);

    public boolean delete(ConvenioServico convenioServico);

    public EntityManager getEntityManager();

    public ConvenioServico pesquisaCodigo(int id);

    public List pesquisaTodos();

    public ConvenioServico pesquisaConvenioServicoPorServESubGrupo(int idServico, int idSubGrupo);

    public List pesquisaServicosSubGrupoConvenio(int idSubGrupo);
}
