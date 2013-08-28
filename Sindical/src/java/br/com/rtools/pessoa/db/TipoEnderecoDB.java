
package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.TipoEndereco;
import java.util.List;
import javax.persistence.EntityManager;


public interface TipoEnderecoDB {
    public boolean insert(TipoEndereco tipoEndereco);
    public boolean update(TipoEndereco tipoEndereco);
    public boolean delete(TipoEndereco tipoEndereco);
    public EntityManager getEntityManager();
    public List<String> pesquisaTipoEndereco(String des_tipoEndereco);
    public TipoEndereco idTipoEndereco(TipoEndereco d_tipoEndereco);
    public TipoEndereco pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List pesquisaTipoEndereco();
    public List<String> pesquisaTipoEnderecoParaJuridica(String des_tipoEndereco);
    public List<String> pesquisaTipoEnderecoParaFisica(String des_tipoEndereco);
    public List<String> listaTipoEnderecoParaJuridica();
    public List<String> listaTipoEnderecoParaFisica();

}

