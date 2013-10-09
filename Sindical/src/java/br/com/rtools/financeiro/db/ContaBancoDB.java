package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Banco;
import br.com.rtools.financeiro.ContaBanco;
import br.com.rtools.financeiro.Plano5;
import java.util.List;
import javax.persistence.EntityManager;

public interface ContaBancoDB {

    public boolean insert(ContaBanco contaBanco);

    public boolean update(ContaBanco contaBanco);

    public boolean delete(ContaBanco contaBanco);

    public EntityManager getEntityManager();

    public ContaBanco pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaContaBanco(String desc, String por, String como);

    public ContaBanco idContaBanco(ContaBanco des_contaBanco);

    public List pesquisaTodosBancos();

    public Banco pesquisaBancoNumero(String num);

    public Banco pesquisaBancoID(int id);

    public List pesquisaPlano5Conta();

    public List pesquisaPlano5ContaComID(int id);
}