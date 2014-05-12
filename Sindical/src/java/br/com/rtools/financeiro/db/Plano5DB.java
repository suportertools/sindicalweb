package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.ContaBanco;
import br.com.rtools.financeiro.Plano4;
import br.com.rtools.financeiro.Plano5;
import java.util.List;

public interface Plano5DB {

    public List<String> pesquisaPlano5(String des_plano4, String des_plano5);

    public Plano5 idPlano5(Plano5 des_plano5);

    public Plano5 pesquisaPlano5PorDesc(String desc, String desc4);

    public Plano4 pesquisaPl4PorString(String desc, String p4);

    public ContaBanco pesquisaUltimoCheque(int desc);

    public Plano5 pesquisaPlano5PorDesc(String desc);

    public List<String> pesquisaPlano5(int id);

    public List pesquisaCaixaBanco();

    public Plano5 pesquisaPlano5IDContaBanco(int id);
    
    public List listPlano4AgrupadoPlanoVw();

    public List findPlano5ByPlano4(int idPlano4);
}
