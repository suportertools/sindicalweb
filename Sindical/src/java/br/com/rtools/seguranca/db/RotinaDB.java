package br.com.rtools.seguranca.db;

import br.com.rtools.seguranca.Rotina;
import java.util.List;
import javax.persistence.EntityManager;

public interface RotinaDB {

    public EntityManager getEntityManager();

    public List pesquisaTodos();

    public boolean existeRotina(Rotina rotina);

    public List pesquisaTodosOrdenado();

    public List pesquisaTodosOrdenadoAtivo();

    public List pesquisaTodosSimples();

    public Rotina pesquisaPaginaRotina(String pagina);

    public List<Rotina> pesquisaRotina(String rotina);

    public Rotina pesquisaAcesso(String pagina);

    public List<Rotina> pesquisaAcessosOrdem();

    public List pesquisaRotinasDisponiveisModulo(int idModulo);

    public Rotina pesquisaRotinaPorPagina(String pagina);
    
    public List<Rotina> pesquisaRotinaPorDescricao(String descricaoRotina);
}
