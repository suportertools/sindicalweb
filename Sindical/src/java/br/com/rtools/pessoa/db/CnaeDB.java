
package br.com.rtools.pessoa.db;

import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.PessoaEndereco;
import java.util.List;
import javax.persistence.EntityManager;


public interface CnaeDB {
    public boolean insert(Cnae cnae);
    public boolean update(Cnae cnae);
    public boolean delete(Cnae cnae);
    public EntityManager getEntityManager();    
    public Cnae pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List pesquisaCnae(String desc, String por, String como);
    public Cnae idCnae(Cnae des_cnae);
    //public List pesquisarCnaeConvencao(int id);
    //public GrupoCidades pesquisarGrupoCidadesJuridica(int id);
    public PessoaEndereco pesquisarCnaePessoaEndereco(int id);
    public Cidade pesquisarCnaeCidade(int id);
    //public GrupoCidades pesquisarGrupoCidadesJuridica(int id_cid, int id_grupoCid);
    public GrupoCidade pesquisarGrupoCidadesJuridica(int id);
     public List pesquisaCnaeSemConvencao(String desc);
     public Cnae pesquisaNumeroCnae(String nr_cnae);
}

