package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.TipoEndereco;
import java.util.List;

public interface TipoEnderecoDB {

    public List<String> pesquisaTipoEndereco(String des_tipoEndereco);

    public TipoEndereco idTipoEndereco(TipoEndereco d_tipoEndereco);

    public List<String> pesquisaTipoEnderecoParaJuridica(String des_tipoEndereco);

    public List<String> pesquisaTipoEnderecoParaFisica(String des_tipoEndereco);

    public List<String> listaTipoEnderecoParaJuridica();

    public List<String> listaTipoEnderecoParaFisica();
}
