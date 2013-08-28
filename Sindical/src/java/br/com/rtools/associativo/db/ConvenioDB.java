
package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Convenio;
import java.util.List;
import javax.persistence.EntityManager;


public interface ConvenioDB {
    public boolean insert(Convenio convenio);
    public boolean update(Convenio convenio);
    public boolean delete(Convenio convenio);
    public EntityManager getEntityManager();
    public Convenio pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List pesquisaConvenioPorGrupoPessoa(int idPessoaJuridica, int idGrupoConvenio);
    public List pesquisaJuridicaPorGrupoESubGrupo(int idSubGrupoConvenio, int idGrupo);
}

