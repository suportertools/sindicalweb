package br.com.rtools.seguranca.db;

import br.com.rtools.seguranca.Rotina;
import java.util.List;
import javax.persistence.EntityManager;

public interface RotinaDB {
    public boolean insert(Rotina rotina);
    public boolean update(Rotina rotina);
    public boolean delete(Rotina rotina);
    public EntityManager getEntityManager();
    public Rotina pesquisaCodigo(int id);
    public List pesquisaTodos();
    public Rotina idRotina(Rotina des_rotina);
    public List pesquisaTodosOrdenado();
    public List pesquisaTodosOrdenadoAtivo();
    public List pesquisaTodosSimples();
    public Rotina pesquisaPaginaRotina(String pagina);
    public List<Rotina> pesquisaRotina(String rotina);
    public Rotina pesquisaAcesso(String pagina);
    public List<Rotina> pesquisaAcessosOrdem();
}

