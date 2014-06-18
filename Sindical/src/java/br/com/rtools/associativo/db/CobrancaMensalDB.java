package br.com.rtools.associativo.db;

import br.com.rtools.financeiro.ServicoPessoa;
import java.util.List;

public interface CobrancaMensalDB {
    public List<ServicoPessoa> listaCobrancaMensal(int id_pessoa);
}
