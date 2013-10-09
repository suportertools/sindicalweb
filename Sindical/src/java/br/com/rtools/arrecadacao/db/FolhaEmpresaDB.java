package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.FolhaEmpresa;
import java.util.List;
import javax.persistence.EntityManager;

public interface FolhaEmpresaDB {

    public boolean insert(FolhaEmpresa folhaEmpresa);

    public boolean update(FolhaEmpresa folhaEmpresa);

    public boolean delete(FolhaEmpresa folhaEmpresa);

    public EntityManager getEntityManager();

    public FolhaEmpresa pesquisaCodigo(int id);

    public List pesquisaTodos();

    public FolhaEmpresa pesquisaPorPessoa(int idPessoa, int idTipoServico, String referencia);
}
