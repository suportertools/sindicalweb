package br.com.rtools.seguranca.db;

import br.com.rtools.seguranca.Permissao;
import br.com.rtools.seguranca.PermissaoDepartamento;
import java.util.List;
import javax.persistence.EntityManager;

public interface PermissaoDepartamentoDB {

    public EntityManager getEntityManager();

    public List pesquisaTodos();

    public List pesquisaPermissaoDptoIdEvento(int id);

    public List pesquisaPermissaDisponivel(String ids);

    public List pesquisaPermissaoAdc(int idDepto, int idNivel);

    public List pesquisaPermissaDepto(String ids);

    public List<Permissao> listaPermissaoDepartamentoDisponivel(int idDepartamento, int idNivel, String descricaoPesquisa);

    public List<PermissaoDepartamento> listaPermissaoDepartamentoAdicionada(int idDepartamento, int idNivel, String descricaoPesquisa);
}
