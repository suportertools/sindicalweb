
package br.com.rtools.endereco.db;

import br.com.rtools.endereco.Cidade;
import java.util.List;


public interface CidadeDB {
    public Cidade pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List<String> pesquisaCidade(String des_cidade, String des_uf);
    public List pesquisaCidadeObj(String des_uf);
    public Cidade idCidade(Cidade des_cidade);
    public List pesquisaCidade(String uf, String cidade, String como);

}

