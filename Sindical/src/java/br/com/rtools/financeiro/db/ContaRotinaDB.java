package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.ContaRotina;
import br.com.rtools.financeiro.Plano4;
import java.util.List;
import javax.persistence.EntityManager;

public interface ContaRotinaDB {

    public EntityManager getEntityManager();

    public ContaRotina pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaPlano5Partida(int rotina);

    public List pesquisaPlano5(int p4, int rotina);

    public Plano4 pesquisaPlano4PorDescricao(String desc);

    public List pesquisaPlano4Grupo(int rotina, String pagRec);

    public ContaRotina pesquisaContaRotina(int id, int rot);

    public int pesquisaRotina(int id_plano4);

    public List pesquisaContasPorRotina(int rotina);

    public List pesquisaContasPorRotina();

    public int verificaRotinaParaConta(int id_plano5);
}
