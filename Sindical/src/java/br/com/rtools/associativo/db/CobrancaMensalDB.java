package br.com.rtools.associativo.db;

import br.com.rtools.financeiro.ServicoPessoa;
import java.util.List;

public interface CobrancaMensalDB {
    public List<ServicoPessoa> listaCobrancaMensal(int id_pessoa);
    public List<ServicoPessoa> listaCobrancaMensalServico(int id_pessoa, int id_servico);
    public List<ServicoPessoa> listaCobrancaMensalFiltro(String por, String desc);
}
