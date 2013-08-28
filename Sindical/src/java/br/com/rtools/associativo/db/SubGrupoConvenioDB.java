
package br.com.rtools.associativo.db;

import br.com.rtools.associativo.SubGrupoConvenio;
import java.util.List;
import javax.persistence.EntityManager;


public interface SubGrupoConvenioDB {
    public boolean insert(SubGrupoConvenio subGrupoConvenio);
    public boolean update(SubGrupoConvenio subGrupoConvenio);
    public boolean delete(SubGrupoConvenio subGrupoConvenio);
    public EntityManager getEntityManager();
    public SubGrupoConvenio pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List pesquisaSubGrupoConvenioPorDescricao(String descricao);
    public List pesquisaSubGrupoConvênioPorGrupo(int idGrupoConvenio);
    public List pesquisaSubGrupoConvênioComServico(int idSubGrupo);
    public List pesquisaSubGrupoConvênioSemServico(int idSubGrupo);
}

