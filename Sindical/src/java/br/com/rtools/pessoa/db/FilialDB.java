package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Registro;
import java.util.List;
import javax.persistence.EntityManager;

public interface FilialDB {

    public boolean insert(Filial filial);

    public boolean update(Filial filial);

    public boolean delete(Filial filial);

    public boolean insertRegistro(Registro registro);

    public boolean updateRegistro(Registro registro);

    public boolean deleteRegistro(Registro registro);

    public EntityManager getEntityManager();

    public Filial pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaJuridicaFilial(int idMatriz);

    public Filial pesquisaFilialPertencente(int idMatriz, int idFilial);

    public List pesquisaFilial(String desc, String por, String como, int idmatriz);

    public List pesquisaRazao(int idMatriz);

    public Registro pesquisaCodigoRegistro(int id);

    public Registro pesquisaRegistroPorFilial(int id);

    public List pesquisaFilialExiste(int idFilial);

    public List pesquisaPessoaConvencaoGCidade(int idPessoa, int idConvencao, int idGCidade);

    public List pesquisaPessoaPatronal(String desc, String por, String como);
}
