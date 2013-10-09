package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.*;
import java.util.List;
import javax.persistence.EntityManager;

public interface PlanoDB {

    public boolean insertPlano(Plano plano);

    public boolean insertPlano2(Plano2 plano2);

    public boolean insertPlano3(Plano3 plan3);

    public boolean insertPlano4(Plano4 plano4);

    public boolean insertPlano5(Plano5 plano5);

    public boolean updatePlano(Plano plano);

    public boolean updatePlano2(Plano2 plano2);

    public boolean updatePlano3(Plano3 plano3);

    public boolean updatePlano4(Plano4 plano4);

    public boolean updatePlano5(Plano5 plano5);

    public boolean delete(Plano plano);

    public EntityManager getEntityManager();

    public Plano pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaPlano(String desc, String por, String como, String plano, int id);

    public List pesquisaPorPlano(String desc, String por, String como, String plano);

    public List pesquisaPlanoSimples();

    public List pesquisaPlanos(String plano);
}
