package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.DescontoEmpregado;
import java.util.List;
import javax.persistence.EntityManager;

public interface DescontoEmpregadoDB {

    public boolean insert(DescontoEmpregado descontoEmpregado);

    public boolean update(DescontoEmpregado descontoEmpregado);

    public boolean delete(DescontoEmpregado descontoEmpregado);

    public DescontoEmpregado pesquisaEntreReferencias(String referencia, int idServico, int idPessoa);

    public EntityManager getEntityManager();

    public DescontoEmpregado pesquisaCodigo(int id);

    public List pesquisaTodos();
}
