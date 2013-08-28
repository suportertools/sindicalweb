
package br.com.rtools.suporte.db;

import br.com.rtools.suporte.OrdemServico;
import java.util.List;
import javax.persistence.EntityManager;

public interface OrdemServicoDB {

    public boolean insert(OrdemServico ordemServico);
    public boolean update(OrdemServico ordemServico);
    public boolean delete(OrdemServico ordemServico);
    public OrdemServico pesquisaCodigo(int id);
    public List pesquisaTodos(String porPesquisa);
    public List<String> pesquisaOrdemServico(String des_tipo);
    public List pesquisaOrdemServicoParametros(String por,String combo,String desc);
    public OrdemServico idOrdemServico(OrdemServico des_OrdemServico);
    public EntityManager getEntityManager();

}