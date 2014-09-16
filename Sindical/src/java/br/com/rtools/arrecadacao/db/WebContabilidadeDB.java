package br.com.rtools.arrecadacao.db;

import br.com.rtools.pessoa.Juridica;
import java.util.List;

public interface WebContabilidadeDB {

    public List<Juridica> listaEmpresasPertContabilidade(int idContabilidade);

    public List pesquisaMovParaWebContabilidade(int idJuridica);

    public List pesquisaMovParaWebContabilidadeComRef(int idJuridica, String referencia);
}
